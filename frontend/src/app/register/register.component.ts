import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../services/auth.service';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  template: `
    <div class="register-container">
      <h1>Criar conta</h1>
      <form [formGroup]="form" (ngSubmit)="onSubmit()">
        <div>
          <label for="username">Username:</label>
          <input id="username" type="text" formControlName="username" placeholder="Seu usuário" autocomplete="username" required>
          <small class="error" *ngIf="form.get('username')?.touched && form.get('username')?.invalid">Informe um usuário.</small>
        </div>
        <div>
          <label for="email">Email:</label>
          <input id="email" type="email" formControlName="email" placeholder="seu@email.com" autocomplete="email" required>
          <small class="error" *ngIf="form.get('email')?.touched && form.get('email')?.invalid">Informe um e-mail válido.</small>
        </div>
        <div>
          <label for="password">Senha:</label>
          <input id="password" type="password" formControlName="password" placeholder="Crie uma senha" autocomplete="new-password" required>
          <small class="error" *ngIf="form.get('password')?.touched && form.get('password')?.invalid">Senha mínima de 6 caracteres.</small>
        </div>
        <div class="buttons">
          <button type="submit" [disabled]="form.invalid || loading">{{ loading ? 'Registrando...' : 'Registrar' }}</button>
          <button type="button" (click)="goLogin()">Voltar ao Login</button>
        </div>
      </form>
    </div>
  `,
  styleUrls: ['./register.component.scss']
})
export class RegisterComponent {
  form: FormGroup;
  loading = false;

  constructor(private fb: FormBuilder, private auth: AuthService, private router: Router) {
    this.form = this.fb.group({
      username: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(6)]]
    });
  }

  onSubmit() {
    if (this.loading) return;
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      console.warn('Registro inválido', this.form.value);
      return;
    }
    this.loading = true;
    const username = String(this.form.value.username || '').trim();
    const email = String(this.form.value.email || '').trim();
    const password = this.form.value.password;
    console.log('Chamando register', { username, email });
    this.auth.register({ username, email, password, role: 'CLIENTE' }).subscribe({
      next: (res) => {
        this.loading = false;
        this.router.navigate(['/investments']);
      },
      error: (err) => {
        console.error('Erro no registro', err);
        this.loading = false;
        const msg = err?.error?.message || err?.message || err?.statusText || 'Erro desconhecido';
        alert('Falha no cadastro: ' + msg);
      }
    });
  }

  goLogin() {
    this.router.navigate(['/login']);
  }
}
