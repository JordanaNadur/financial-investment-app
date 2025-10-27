import { Routes } from '@angular/router';
import { LoginComponent } from './login/login.component';
import { InvestmentsComponent } from './investments/investments.component';
import { InvestmentOptionsComponent } from './investment-options/investment-options.component';
import { authGuard } from './guards/auth.guard';
import { RegisterComponent } from './register/register.component';

export const routes: Routes = [
  { path: '', redirectTo: '/login', pathMatch: 'full' },
  { path: 'login', component: LoginComponent },
  { path: 'registro', component: RegisterComponent },
  { path: 'investments', component: InvestmentsComponent, canActivate: [authGuard] },
  { path: 'investment-options', component: InvestmentOptionsComponent, canActivate: [authGuard] },
  { path: '**', redirectTo: '/login' }
];
