import { Component, inject } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { AuthService } from '../services/auth.service';
import { ToastService } from '../shared/toast.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule
  ],
  template: `
    <div class="login-container">
      <h1>Financial Investment App</h1>
      <form [formGroup]="loginForm" (ngSubmit)="onSubmit()">
        <div>
          <label for="username">Username:</label>
          <input id="username" type="text" formControlName="username" placeholder="Digite seu usuário">
        </div>
        <div>
          <label for="password">Password:</label>
          <input id="password" type="password" formControlName="password" placeholder="Digite sua senha">
        </div>
        <div class="buttons">
          <button type="submit" [disabled]="loginForm.invalid || loading">Entrar</button>
          <button type="button" (click)="goToRegister()">Cadastro</button>
        </div>
      </form>
    </div>
  `,
  styleUrls: ['./login.component.scss']
})
export class LoginComponent {
  loginForm: FormGroup;
  loading = false;
  private toast = inject(ToastService);

  constructor(private fb: FormBuilder, private auth: AuthService, private router: Router, private route: ActivatedRoute) {
    this.loginForm = this.fb.group({
      username: ['', Validators.required],
      password: ['', Validators.required]
    });
  }

  onSubmit() {
    if (this.loginForm.valid) {
      this.loading = true;
      const { username, password } = this.loginForm.value;
      this.auth.login({ username, password }).subscribe({
        next: () => {
          this.loading = false;
          const roleHint = (this.route.snapshot.data?.['role'] as string | undefined)?.toUpperCase();
          const role = (localStorage.getItem('role') || '').toUpperCase();
          const finalRole = roleHint || role;
          if (finalRole === 'ADMIN') this.router.navigate(['/admin/dashboard']);
          else this.router.navigate(['/cliente/dashboard']);
        },
        error: (err) => {
          this.loading = false;
          const msg = err?.error?.message || err?.statusText || 'Erro desconhecido';
          this.toast.error('Falha no login: ' + msg);
        }
      });
    }
  }

  goToRegister() {
    this.router.navigate(['/registro']);
  }
}
