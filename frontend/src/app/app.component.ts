import { Component, inject } from '@angular/core';
import { Router, RouterOutlet } from '@angular/router';
import { CommonModule } from '@angular/common';
import { AuthService } from './services/auth.service';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, CommonModule],
  template: `
    <div *ngIf="!isAuthPage()" class="global-header">
      <div class="brand">Financial Investment App</div>
      <div class="actions">
        <span class="user">👤 {{ getUsername() }}</span>
        <button (click)="logout()">Sair</button>
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

  isAuthPage(): boolean {
    const url = this.router.url;
    return url.startsWith('/login') || url.startsWith('/registro');
  }

  getUsername(): string | null {
    return localStorage.getItem('username');
  }

  logout() {
    this.auth.logout();
  }
}
