import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterModule } from '@angular/router';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { Product, ProductService, ProductType } from '../services/product.service';
import { InvestmentService } from '../services/investment.service';
import { ToastService } from '../shared/toast.service';

@Component({
  selector: 'app-investment-options',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterModule],
  template: `
    <div class="investment-options-container">
      <div class="header" style="display:flex;align-items:center;justify-content:space-between;margin-bottom:12px;gap:8px;">
        <h2 style="margin:0;">Opções de Investimento</h2>
        <div class="nav-arrows" style="display:flex;gap:8px;">
          <a routerLink="/cliente/dashboard" class="nav-btn" style="text-decoration:none;background:#f1f5f9;padding:6px 10px;border-radius:6px;color:#0f172a;display:inline-flex;align-items:center;gap:6px;"><span class="arrow">←</span> Dashboard</a>
          <a routerLink="/cliente/investimentos" class="nav-btn" style="text-decoration:none;background:#f1f5f9;padding:6px 10px;border-radius:6px;color:#0f172a;display:inline-flex;align-items:center;gap:6px;">Meus investimentos <span class="arrow">→</span></a>
        </div>
      </div>
      <div *ngIf="loading">Carregando produtos...</div>
      <div *ngIf="!loading && (!products || !products.length)" class="empty">Nenhum produto disponível.</div>

      <div class="options-grid" *ngIf="!loading && products?.length">
        <div class="option-card" *ngFor="let p of products" (click)="selectProduct(p)">
          <div class="card-icon" [ngClass]="riskClass(p.riskLevel)"><i [class]="iconClassFor(p.type)"></i></div>
          <h3 class="text-ellipsis" title="{{ p.name }}">{{ p.name }}</h3>
          <p>
            <span class="badge">{{ p.type }}</span>
            <span class="badge" [class.badge-low]="p.riskLevel==='BAIXO'" [class.badge-med]="p.riskLevel==='MEDIO'" [class.badge-high]="p.riskLevel==='ALTO'">Risco: {{ p.riskLevel }}</span>
          </p>
          <div class="return-rate">Retorno mensal: {{ (p.monthlyReturn * 100) | number:'1.0-2' }}%</div>
          <div class="meta">Mínimo: {{ formatCurrency(p.minimumInvestment) }} • Prazo: {{ p.minimumTermMonths }}m</div>
        </div>
      </div>

      <!-- Formulário de investimento -->
      <div class="investment-form" *ngIf="showInvestmentForm && selectedProduct">
        <h3>Investir em {{ selectedProduct?.name }}</h3>

        <div class="product-summary">
          <div class="summary-item">
            <span class="label">Rentabilidade mensal:</span>
            <span class="value">{{ ((selectedProduct?.monthlyReturn || 0) * 100) | number:'1.0-2' }}%</span>
          </div>
          <div class="summary-item">
            <span class="label">Prazo mínimo:</span>
            <span class="value">{{ selectedProduct?.minimumTermMonths }} meses</span>
          </div>
          <div class="summary-item">
            <span class="label">Investimento mínimo:</span>
            <span class="value">{{ formatCurrency(selectedProduct?.minimumInvestment || 0) }}</span>
          </div>
        </div>

        <form [formGroup]="investmentForm" (ngSubmit)="confirmInvestment()">
          <div class="form-group">
            <label for="amount">Valor do investimento:</label>
            <input id="amount" type="number" formControlName="amount" [placeholder]="'Valor mínimo: ' + formatCurrency(selectedProduct?.minimumInvestment || 0)" [min]="selectedProduct?.minimumInvestment || 0">
            <div class="error" *ngIf="investmentForm.get('amount')?.invalid && investmentForm.get('amount')?.touched">{{ getAmountErrorMessage() }}</div>
          </div>

          <div class="form-buttons">
            <button type="button" class="cancel-btn" (click)="cancelInvestment()" [disabled]="saving">Cancelar</button>
            <button type="submit" class="confirm-btn" [disabled]="investmentForm.invalid || saving">
              <span *ngIf="saving" class="spinner" style="margin-right:8px"></span>
              Confirmar Investimento
            </button>
          </div>
        </form>
      </div>
    </div>
  `,
  styleUrls: ['./investment-options.component.scss']
})
export class InvestmentOptionsComponent {
  products: Product[] = [];
  loading = false;
  error = '';

  selectedProduct: Product | null = null;
  showInvestmentForm = false;
  investmentForm: FormGroup;
  saving = false;

  private router = inject(Router);
  private fb = inject(FormBuilder);
  private service = inject(ProductService);
  private investmentApi = inject(InvestmentService);
  private toast = inject(ToastService);

  constructor() {
    this.investmentForm = this.fb.group({ amount: [null, [Validators.required, Validators.min(0)]] });
    this.load();
  }

  riskClass(risk: 'BAIXO'|'MEDIO'|'ALTO'): string {
    switch (risk) {
      case 'BAIXO': return 'icon-low';
      case 'MEDIO': return 'icon-med';
      case 'ALTO': return 'icon-high';
      default: return '';
    }
  }

  iconClassFor(type: ProductType): string {
    switch (type) {
      case 'ACAO': return 'pi pi-chart-line';
      case 'FII': return 'pi pi-building';
      case 'ETF': return 'pi pi-chart-pie';
      case 'FUNDO': return 'pi pi-briefcase';
      case 'TESOURO': return 'pi pi-shield';
  case 'RENDA_FIXA': return 'pi pi-dollar';
  case 'CDB': return 'pi pi-wallet';
      case 'LCI': return 'pi pi-home';
  case 'LCA': return 'pi pi-globe';
      case 'DEBENTURE': return 'pi pi-file';
  case 'OURO': return 'pi pi-star-fill';
  case 'CAMBIO': return 'pi pi-refresh';
  case 'CRIPTO': return 'pi pi-chart-bar';
      default: return 'pi pi-chart-bar';
    }
  }

  load() {
    this.loading = true;
    this.service.list().subscribe({
      next: (res) => { this.products = (res || []).filter(p => p.active !== false); this.loading = false; },
      error: (err) => { this.error = 'Falha ao carregar produtos'; this.loading = false; this.toast.error(this.error); }
    });
  }

  formatCurrency(v: number): string {
    return v.toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' });
  }

  selectProduct(p: Product) {
    this.selectedProduct = p;
    this.showInvestmentForm = true;
    const min = p.minimumInvestment || 0;
    this.investmentForm.get('amount')?.setValidators([Validators.required, Validators.min(min)]);
    this.investmentForm.get('amount')?.updateValueAndValidity();
  }

  cancelInvestment() {
    this.showInvestmentForm = false;
    this.selectedProduct = null;
    this.investmentForm.reset();
  }

  getAmountErrorMessage(): string {
    const min = this.selectedProduct?.minimumInvestment || 0;
    return `O valor mínimo é ${this.formatCurrency(min)}`;
  }

  confirmInvestment() {
    if (!(this.investmentForm.valid && this.selectedProduct)) return;
    const amount = Number(this.investmentForm.value.amount);
    const prazo = this.selectedProduct?.minimumTermMonths || 0;
    const payload = {
      valor: amount,
      prazoMeses: prazo,
      rentabilidadeMensal: this.selectedProduct!.monthlyReturn,
  modalidade: 'MENSAL',
  optionId: this.selectedProduct!.id!,
    } as const;
    this.saving = true;
    this.investmentApi.create(payload).subscribe({
      next: () => {
        this.toast.success('Investimento criado com sucesso.');
  this.router.navigate(['/cliente/investimentos']);
        this.saving = false;
      },
      error: () => {
        this.toast.error('Falha ao criar investimento.');
        this.saving = false;
      }
    });
  }
}
