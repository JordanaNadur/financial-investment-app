import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { Observable, tap } from 'rxjs';
import { environment } from '../../environments/environment';

export interface AuthResponse {
  token: string;
  refreshToken?: string;
  username?: string;
  role?: string;
  userId?: number;
}

export interface LoginRequest {
  username: string;
  password: string;
}

export interface RegisterRequest {
  username: string;
  email: string;
  password: string;
  role?: string; // backend exige role, default CLIENTE
}

@Injectable({ providedIn: 'root' })
export class AuthService {
  private http = inject(HttpClient);
  private router = inject(Router);
  private baseUrl = `${environment.apiBaseUrl}/auth`;
  private tokenKey = 'token';
  private userIdKey = 'userId';

  private decodeJwtPayload(token: string): any | null {
    try {
      const parts = token.split('.');
      if (parts.length < 2) return null;
      const payload = parts[1]
        .replace(/-/g, '+')
        .replace(/_/g, '/');
      const decoded = atob(payload.padEnd(payload.length + (4 - (payload.length % 4)) % 4, '='));
      return JSON.parse(decoded);
    } catch {
      return null;
    }
  }

  private persistAuth(res: AuthResponse) {
    if (!res?.token) return;
    localStorage.setItem(this.tokenKey, res.token);

    let username = res.username || '';
    let role = res.role || '';
    let userId = res.userId ?? null;

    const payload = this.decodeJwtPayload(res.token);
    if (payload) {
      username = payload.sub || username;
      role = payload.role || role;
      userId = (payload.userId ?? userId) as number | null;
    }

    if (username) localStorage.setItem('username', username);
    if (role) localStorage.setItem('role', role);
    if (userId != null) localStorage.setItem(this.userIdKey, String(userId));
  }

  login(payload: LoginRequest): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.baseUrl}/login`, payload).pipe(
      tap(res => this.persistAuth(res))
    );
  }

  register(payload: RegisterRequest): Observable<AuthResponse> {
    const withRole = { ...payload, role: payload.role || 'CLIENTE' };
    return this.http.post<AuthResponse>(`${this.baseUrl}/register`, withRole).pipe(
      tap(res => this.persistAuth(res))
    );
  }

  logout(): void {
    localStorage.removeItem(this.tokenKey);
    localStorage.removeItem('username');
    localStorage.removeItem('role');
    localStorage.removeItem(this.userIdKey);
    this.router.navigate(['/login']);
  }

  getToken(): string | null {
    return localStorage.getItem(this.tokenKey);
  }

  isAuthenticated(): boolean {
    return !!this.getToken();
  }

  getUserId(): number | null {
    const v = localStorage.getItem(this.userIdKey);
    return v ? Number(v) : null;
  }
}
