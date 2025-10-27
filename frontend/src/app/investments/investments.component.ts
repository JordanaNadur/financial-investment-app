import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { AuthService } from '../services/auth.service';

@Component({
  selector: 'app-investments',
  standalone: true,
  imports: [
    CommonModule
  ],
  template: `
    <div class="investments-container">
      <div class="investments-header">
        <h2>Meus Investimentos</h2>
        <button class="invest-btn" (click)="invest()">📈 Investir</button>
      </div>
      
      <div class="investments-grid">
        <div class="investment-card" *ngFor="let investment of investments">
          <div class="card-header">
            <h3>{{ investment.name }}</h3>
            <span class="investment-type">{{ investment.type }}</span>
          </div>
          <div class="card-content">
            <p><strong>Valor:</strong> {{ investment.value | currency:'BRL':'symbol':'1.2-2' }}</p>
            <p><strong>Rendimento:</strong> {{ investment.return }}%</p>
            <p><strong>Data:</strong> {{ investment.date | date:'dd/MM/yyyy' }}</p>
            <button class="withdraw-btn" (click)="withdraw(investment)">💰 Sacar</button>
          </div>
        </div>
      </div>
    </div>
  `,
  styleUrls: ['./investments.component.scss']
})
export class InvestmentsComponent {
  investments: any[] = [];
  username: string | null = null;

  constructor(private router: Router, private auth: AuthService) {
    this.username = localStorage.getItem('username');
    this.loadInvestments();
  }

  private loadInvestments() {
    // Carrega investimentos do localStorage ou usa dados padrão
    const savedInvestments = localStorage.getItem('investments');
    if (savedInvestments) {
      this.investments = JSON.parse(savedInvestments);
      // Converte datas de string para Date
      this.investments.forEach(inv => {
        if (typeof inv.date === 'string') {
          inv.date = new Date(inv.date);
        }
      });
    } else {
      // Investimentos padrão para demonstração
      this.investments = [
        { name: 'Tesouro Direto', type: 'Renda Fixa', value: 10000, return: 12.5, date: new Date('2024-01-15') },
        { name: 'Ações PETR4', type: 'Renda Variável', value: 5000, return: 8.2, date: new Date('2024-02-20') },
        { name: 'CDB Banco XYZ', type: 'Renda Fixa', value: 15000, return: 11.8, date: new Date('2024-03-10') }
      ];
      this.saveInvestments();
    }
  }

  private saveInvestments() {
    localStorage.setItem('investments', JSON.stringify(this.investments));
  }

  logout() { this.auth.logout(); }

  withdraw(investment: any) {
    const confirmWithdraw = confirm(`Deseja realmente sacar o investimento "${investment.name}"?\n\nValor: ${investment.value.toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' })}`);
    if (confirmWithdraw) {
      this.investments = this.investments.filter(inv => inv !== investment);
      this.saveInvestments();
      alert(`Saque realizado com sucesso!\n\nInvestimento: ${investment.name}\nValor sacado: ${investment.value.toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' })}\n\nO valor será creditado em sua conta em até 2 dias úteis.`);
    }
  }

  invest() { this.router.navigate(['/investment-options']); }
}
