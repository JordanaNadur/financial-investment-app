import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';

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
        <button type="submit" [disabled]="loginForm.invalid">Entrar</button>
      </form>
    </div>
  `,
  styleUrls: ['./login.component.scss']
})
export class LoginComponent {
  loginForm: FormGroup;

  constructor(private fb: FormBuilder, private http: HttpClient, private router: Router) {
    this.loginForm = this.fb.group({
      username: ['', Validators.required],
      password: ['', Validators.required]
    });
  }

  onSubmit() {
    if (this.loginForm.valid) {
      console.log('Form submitted:', this.loginForm.value);
      // Temporarily navigate without API call
      this.router.navigate(['/investments']);
    }
  }
}
