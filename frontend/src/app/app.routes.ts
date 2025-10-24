import { Routes } from '@angular/router';
import { LoginComponent } from './login/login.component';
import { InvestmentsComponent } from './investments/investments.component';
import { InvestmentOptionsComponent } from './investment-options/investment-options.component';

export const routes: Routes = [
  { path: '', redirectTo: '/login', pathMatch: 'full' },
  { path: 'login', component: LoginComponent },
  { path: 'investments', component: InvestmentsComponent },
  { path: 'investment-options', component: InvestmentOptionsComponent },
  { path: '**', redirectTo: '/login' }
];
