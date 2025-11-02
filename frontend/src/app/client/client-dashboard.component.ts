import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { WalletService } from '../services/wallet.service';
import { AuthService } from '../services/auth.service';
import { ChartModule } from 'primeng/chart';
import { RouterModule } from '@angular/router';
import { InvestmentService } from '../services/investment.service';

@Component({
  selector: 'app-client-dashboard',
  standalone: true,
  imports: [CommonModule, ChartModule, RouterModule],
  template: `
    <section class="page">
      <div class="header">
        <h1>Área do Cliente</h1>
        <div class="nav-arrows">
          <a routerLink="/cliente/investimentos" class="nav-btn"><span class="arrow">←</span> Investimentos</a>
        </div>
      </div>
      <div class="grid">
        <div class="card">
          <h3>Distribuição por tipo de investimento</h3>
          <p *ngIf="chartLoading">Carregando gráfico...</p>
          <p *ngIf="!chartLoading && (pieData?.datasets?.[0]?.data?.length || 0) === 0">Sem dados de investimentos.</p>
          <div class="chart-box" *ngIf="!chartLoading && (pieData?.datasets?.[0]?.data?.length || 0) > 0">
            <p-chart type="pie" [data]="pieData" [options]="pieOptions" [style]="{width:'100%',height:'100%'}"></p-chart>
          </div>
        </div>

        <div class="card">
          <h3>Dicas rápidas</h3>
          <ul>
            <li>Use "Meus Investimentos" para sacar e acompanhar seus aportes.</li>
            <li>O gráfico resume sua distribuição por tipo de produto.</li>
          </ul>
        </div>
      </div>
    </section>
  `,
  styles: [`
    .grid { display: grid; grid-template-columns: 1fr; gap: 16px; }
    @media (min-width: 900px) { .grid { grid-template-columns: 2fr 1fr; } }
    .card { background: #fff; padding: 16px; border-radius: 8px; box-shadow: 0 1px 4px rgba(0,0,0,.08); }
  .header { display: flex; align-items: center; justify-content: space-between; gap: 12px; margin-bottom: 12px; }
  .nav-arrows { display: flex; gap: 8px; }
  .nav-btn { display: inline-flex; align-items: center; gap: 6px; padding: 6px 10px; border-radius: 6px; background: #f1f5f9; color: #0f172a; text-decoration: none; font-size: 0.95rem; }
  .nav-btn:hover { background: #e2e8f0; }
  .arrow { font-size: 1.1rem; }
  .chart-box { width: 100%; max-width: 420px; height: 260px; margin: 8px auto 0; }
  `]
})
export class ClientDashboardComponent {
  private wallet = inject(WalletService);
  private auth = inject(AuthService);
  private investments = inject(InvestmentService);
  balance = 0;
  loading = false;
  chartLoading = false;
  pieData: any;
  pieOptions: any;

  ngOnInit() {
    this.refresh();
    this.loadChart();
  }

  refresh() {
    const userId = this.auth.getUserId();
    if (!userId) return;
    this.loading = true;
    this.wallet.getBalance(userId).subscribe({
      next: (res) => { this.balance = res.balance ?? 0; this.loading = false; },
      error: () => { this.loading = false; }
    });
  }

  loadChart() {
    this.chartLoading = true;
  this.investments.reportSummary().subscribe({
      next: (res) => {
        const labels = Object.keys(res || {});
        const data = Object.values(res || {});
        this.pieData = {
          labels,
          datasets: [
            {
              data,
              backgroundColor: this.buildColors(labels.length),
            }
          ]
        };
        this.pieOptions = {
          responsive: true,
          maintainAspectRatio: false,
          plugins: {
            legend: { position: 'bottom' }
          }
        };
        this.chartLoading = false;
      },
      error: () => { this.chartLoading = false; }
    });
  }

  private buildColors(n: number): string[] {
    const palette = ['#3b82f6','#10b981','#f59e0b','#ef4444','#8b5cf6','#06b6d4','#84cc16','#f97316','#a855f7','#14b8a6'];
    const out: string[] = [];
    for (let i=0;i<n;i++) out.push(palette[i % palette.length]);
    return out;
  }
}
