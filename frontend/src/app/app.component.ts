import { Component, inject } from '@angular/core';
import { NavigationEnd, Router, RouterOutlet } from '@angular/router';
import { filter } from 'rxjs/operators';
import { CommonModule } from '@angular/common';
import { AuthService } from './services/auth.service';
import { ToastService } from './shared/toast.service';
import { WalletService, WalletBalance } from './services/wallet.service';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, CommonModule],
  template: `
    <div *ngIf="!isAuthPage()" class="global-header">
      <div class="brand">Financial Investment App</div>
      <div class="actions">
        <div *ngIf="isCliente()" class="wallet-info" [class.loading]="walletLoading">
          💰 {{ walletDisplay() }}
        </div>
        <button *ngIf="isCliente()" (click)="goToInvest()">📈 Investir</button>
        <span class="user">👤 {{ getUsername() }}</span>
        <button (click)="logout()">Sair</button>
      </div>
    </div>
    <div class="toast-container">
      <div class="toast" *ngFor="let t of toast.toasts()" [class]="'toast ' + t.type" (click)="toast.dismiss(t.id)">
        {{ t.message }}
      </div>
    </div>
    <router-outlet></router-outlet>
  `,
  styleUrls: ['./app.component.scss']
})
export class AppComponent {
  title = 'Investimentos Financeiros';
  private router = inject(Router);
  private auth = inject(AuthService);
  private wallet = inject(WalletService);
  toast = inject(ToastService);
  walletLoading = false;
  private walletBalance: number | null = null;

  isAuthPage(): boolean {
    const url = this.router.url;
    return url.startsWith('/login') || url.startsWith('/registro');
  }

  getUsername(): string | null {
    return localStorage.getItem('username');
  }

  isCliente(): boolean {
    return localStorage.getItem('role') === 'CLIENTE';
  }

  goToInvest() {
    this.router.navigate(['/cliente/opcoes']);
  }

  logout() {
    this.auth.logout();
  }

  ngOnInit() {
    this.tryLoadWallet();
    // Atualiza saldo ao navegar para outras rotas (ex.: após registro/login)
    this.router.events.pipe(filter(e => e instanceof NavigationEnd)).subscribe(() => {
      this.tryLoadWallet();
    });
    // Retry curto para evitar condição de corrida logo após o registro
    setTimeout(() => this.tryLoadWallet(), 1200);
  }

  private tryLoadWallet() {
    if (!this.isCliente()) return;
    const userId = this.auth.getUserId();
    if (!userId) return;
    this.walletLoading = true;
    this.wallet.getBalance(userId).subscribe({
      next: (res: WalletBalance) => {
        this.walletBalance = res.balance ?? 0;
        this.walletLoading = false;
      },
      error: () => {
        this.walletLoading = false;
      }
    });
  }

  walletDisplay(): string {
    if (this.walletLoading) return 'carregando...';
    const v = this.walletBalance ?? 0;
    return v.toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' });
  }
}
