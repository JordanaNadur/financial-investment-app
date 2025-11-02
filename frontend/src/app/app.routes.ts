import { Routes } from '@angular/router';
import { LoginComponent } from './login/login.component';
import { InvestmentsComponent } from './investments/investments.component';
import { InvestmentOptionsComponent } from './investment-options/investment-options.component';
import { authGuard } from './guards/auth.guard';
import { RegisterComponent } from './register/register.component';
import { roleGuard } from './guards/role.guard';
import { AdminDashboardComponent } from './admin/admin-dashboard.component';
import { AdminOptionsComponent } from './admin/admin-options.component';
import { ClientDashboardComponent } from './client/client-dashboard.component';
import { ClientPortfolioComponent } from './client/client-portfolio.component';

export const routes: Routes = [
  { path: '', redirectTo: '/login', pathMatch: 'full' },

  // Login/Cadastro separados por perfil
  { path: 'login', component: LoginComponent },
  { path: 'login/cliente', component: LoginComponent, data: { role: 'CLIENTE' } },
  { path: 'login/admin', component: LoginComponent, data: { role: 'ADMIN' } },

  { path: 'registro', component: RegisterComponent },
  { path: 'registro/cliente', component: RegisterComponent, data: { role: 'CLIENTE' } },
  { path: 'registro/admin', component: RegisterComponent, data: { role: 'ADMIN' } },

  // Área do cliente
  { path: 'cliente/dashboard', component: ClientDashboardComponent, canActivate: [authGuard, roleGuard], data: { role: 'CLIENTE' } },
  { path: 'cliente/portfolio', component: ClientPortfolioComponent, canActivate: [authGuard, roleGuard], data: { role: 'CLIENTE' } },
  { path: 'cliente/investimentos', component: InvestmentsComponent, canActivate: [authGuard, roleGuard], data: { role: 'CLIENTE' } },
  { path: 'cliente/opcoes', component: InvestmentOptionsComponent, canActivate: [authGuard, roleGuard], data: { role: 'CLIENTE' } },

  // Área do admin (reuse de InvestmentOptions para gestão de catálogo, futuramente criar componente dedicado)
  { path: 'admin/dashboard', component: AdminDashboardComponent, canActivate: [authGuard, roleGuard], data: { role: 'ADMIN' } },
  { path: 'admin/opcoes', component: AdminOptionsComponent, canActivate: [authGuard, roleGuard], data: { role: 'ADMIN' } },

  // Rotas antigas mantidas para compatibilidade
  // Rotas antigas descontinuadas
  // { path: 'investments', component: InvestmentsComponent, canActivate: [authGuard] },
  // { path: 'investment-options', component: InvestmentOptionsComponent, canActivate: [authGuard] },

  { path: '**', redirectTo: '/login' }
];
