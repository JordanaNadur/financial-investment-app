import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterModule } from '@angular/router';
import { AuthService } from '../services/auth.service';
import { InvestmentService, InvestmentResponseDto } from '../services/investment.service';
import { ToastService } from '../shared/toast.service';

@Component({
  selector: 'app-investments',
  standalone: true,
  imports: [
  CommonModule,
  RouterModule
  ],
  template: `
    <div class="investments-container">
      <div class="investments-header">
        <h2 style="margin:0;">Meus Investimentos</h2>
        <div class="nav-arrows" style="display:flex;gap:8px;">
          <a routerLink="/cliente/opcoes" class="nav-btn" style="text-decoration:none;background:#f1f5f9;padding:6px 10px;border-radius:6px;color:#0f172a;display:inline-flex;align-items:center;gap:6px;"><span class="arrow">←</span> Investir</a>
        </div>
      </div>

      <div *ngIf="loading">Carregando...</div>
      <div *ngIf="!loading && investments?.length===0" class="empty">Você ainda não possui investimentos.</div>

      <div class="investments-grid" *ngIf="!loading && investments?.length">
  <div class="investment-card" *ngFor="let investment of investments" [ngClass]="typeClass(investment.productType)">
          <div class="card-header" [ngClass]="typeClass(investment.productType)">
            <h3>{{ formatName(investment) }}</h3>
            <span class="investment-type">{{ investment.modalidade }}</span>
          </div>
          <div class="card-content">
            <p *ngIf="investment.productType" style="color:#475569;margin:.25rem 0 .5rem;">
              <strong>Produto:</strong>
              <span><em>{{ investment.productType }}</em></span>
            </p>
            <p><strong>Valor:</strong> {{ investment.valor | currency:'BRL':'symbol':'1.2-2' }}</p>
            <p><strong>Ganhos simulados:</strong> {{ investment.retornoSimulado | currency:'BRL':'symbol':'1.2-2' }}</p>
            <p><strong>Data:</strong> {{ (investment.dataCriacao || '') | date:'dd/MM/yyyy' }}</p>
            <button class="withdraw-btn" [ngClass]="typeClass(investment.productType)" (click)="openConfirm(investment)" [disabled]="withdrawingId===investment.id">
              <span *ngIf="withdrawingId===investment.id" class="spinner" style="margin-right:8px"></span>
              💰 Sacar
            </button>
          </div>
        </div>
      </div>
    </div>

    <!-- Modal de confirmação de saque -->
    <div class="overlay" *ngIf="confirmVisible">
      <div class="modal">
        <h3 style="margin-top:0;">Confirmar saque</h3>
        <p>
          Deseja realmente sacar o investimento
          <strong>#{{ selectedInvestment?.id }}</strong>?
        </p>
        <p style="color:#475569; font-size:.95rem;">
          Valor investido:
          <strong>{{ selectedInvestment?.valor | currency:'BRL':'symbol':'1.2-2' }}</strong>
        </p>
        <div class="modal-actions">
          <button class="btn btn-light" (click)="cancelConfirm()" [disabled]="withdrawingId===selectedInvestment?.id">Cancelar</button>
          <button class="btn btn-danger" (click)="confirmWithdraw()" [disabled]="withdrawingId===selectedInvestment?.id">
            <span *ngIf="withdrawingId===selectedInvestment?.id" class="spinner" style="margin-right:8px"></span>
            Confirmar saque
          </button>
        </div>
      </div>
    </div>
  `,
  styleUrls: ['./investments.component.scss']
})
export class InvestmentsComponent {
  investments: InvestmentResponseDto[] = [];
  loading = false;
  withdrawingId: number | null = null;
  confirmVisible = false;
  selectedInvestment: InvestmentResponseDto | null = null;

  private router = inject(Router);
  private auth = inject(AuthService);
  private investmentApi = inject(InvestmentService);
  private toast = inject(ToastService);

  constructor() {
    this.load();
  }

  load() {
    this.loading = true;
    this.investmentApi.list().subscribe({
      next: (items) => { this.investments = items || []; this.loading = false; },
      error: () => { this.toast.error('Falha ao carregar investimentos'); this.loading = false; }
    });
  }

  logout() { this.auth.logout(); }

  openConfirm(inv: InvestmentResponseDto) {
    this.selectedInvestment = inv;
    this.confirmVisible = true;
  }

  cancelConfirm() {
    this.confirmVisible = false;
    this.selectedInvestment = null;
  }

  confirmWithdraw() {
    if (!this.selectedInvestment) return;
    const inv = this.selectedInvestment;
    this.withdrawingId = inv.id;
    this.investmentApi.withdraw(inv.id).subscribe({
      next: () => {
        this.toast.info('Saque solicitado.');
        this.withdrawingId = null;
        this.cancelConfirm();
        this.load();
      },
      error: () => {
        this.toast.error('Falha ao sacar investimento');
        this.withdrawingId = null;
      }
    });
  }

  formatName(inv: InvestmentResponseDto): string {
    return `Investimento #${inv.id}`;
  }

  typeClass(type?: string | null): string {
    const t = (type || '').toUpperCase();
    const allowed = ['ACAO','FII','ETF','FUNDO','TESOURO','RENDA_FIXA','CDB','LCI','LCA','DEBENTURE','OURO','CAMBIO','CRIPTO'];
    return allowed.includes(t) ? `type-${t}` : 'type-DEFAULT';
  }
}
