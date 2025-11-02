import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';

export interface WalletBalance { balance: number }

@Injectable({ providedIn: 'root' })
export class WalletService {
  private http = inject(HttpClient);
  private baseUrl = `${environment.apiBaseUrl}/wallet`;

  getBalance(clientId: number): Observable<WalletBalance> {
    return this.http.get<WalletBalance>(`${this.baseUrl}/${clientId}`);
  }

  credit(clientId: number, amount: number): Observable<void> {
    return this.http.post<void>(`${this.baseUrl}/${clientId}/credit?amount=${amount}`, {});
  }

  debit(clientId: number, amount: number): Observable<{ success: boolean; message?: string }> {
    return this.http.post<{ success: boolean; message?: string }>(`${this.baseUrl}/${clientId}/debit?amount=${amount}`, {});
  }
}
