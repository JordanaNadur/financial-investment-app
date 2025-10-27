import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { AuthService } from '../services/auth.service';

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

  constructor(private fb: FormBuilder, private auth: AuthService, private router: Router) {
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
          this.router.navigate(['/investments']);
        },
        error: (err) => {
          this.loading = false;
          alert('Falha no login: ' + (err?.error?.message || err.statusText || 'Erro desconhecido'));
        }
      });
    }
  }

  goToRegister() {
    this.router.navigate(['/registro']);
  }
}
