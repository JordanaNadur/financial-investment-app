import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';

@Component({
  selector: 'app-investments',
  standalone: true,
  imports: [
    CommonModule
  ],
  template: `
    <div class="header">
      <h1>Financial Investment App</h1>
      <button class="logout-btn" (click)="logout()">
        🚪 Logout
      </button>
    </div>

    <div class="investments-container">
      <div class="investments-header">
        <h2>Meus Investimentos</h2>
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
          </div>
        </div>
      </div>
    </div>
  `,
  styleUrls: ['./investments.component.scss']
})
export class InvestmentsComponent {
  investments = [
    {
      name: 'Tesouro Direto',
      type: 'Renda Fixa',
      value: 10000,
      return: 12.5,
      date: new Date('2024-01-15')
    },
    {
      name: 'Ações PETR4',
      type: 'Renda Variável',
      value: 5000,
      return: 8.2,
      date: new Date('2024-02-20')
    },
    {
      name: 'CDB Banco XYZ',
      type: 'Renda Fixa',
      value: 15000,
      return: 11.8,
      date: new Date('2024-03-10')
    }
  ];

  constructor(private router: Router) {}

  logout() {
    localStorage.removeItem('token');
    this.router.navigate(['/login']);
  }
}
