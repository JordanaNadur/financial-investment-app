import { Routes } from '@angular/router';
import { LoginComponent } from './login/login.component';
import { InvestmentsComponent } from './investments/investments.component';

export const routes: Routes = [
  { path: '', redirectTo: '/login', pathMatch: 'full' },
  { path: 'login', component: LoginComponent },
  { path: 'investments', component: InvestmentsComponent },
  { path: '**', redirectTo: '/login' }
];
