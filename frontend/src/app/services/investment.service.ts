import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable, map } from 'rxjs';
import { environment } from '../../environments/environment';

export interface InvestmentRequestDto {
  valor: number;              // valor do investimento
  prazoMeses: number;         // prazo em meses
  rentabilidadeMensal: number;// % mensal
  modalidade: string;         // ex: "MENSAL"
  optionId: number;           // id do produto selecionado
}

export interface InvestmentResponseDto {
  id: number;
  valor: number;
  retornoSimulado: number;
  modalidade: string;
  productName?: string;
  productType?: string;
  dataCriacao?: string;
  dataResgate?: string | null;
  ativo?: boolean;
}

@Injectable({ providedIn: 'root' })
export class InvestmentService {
  private http = inject(HttpClient);
  private baseUrl = `${environment.apiBaseUrl}/investments`;

  create(payload: InvestmentRequestDto): Observable<InvestmentResponseDto> {
    return this.http.post<InvestmentResponseDto>(this.baseUrl, payload);
  }

  list(page = 0, size = 10): Observable<InvestmentResponseDto[]> {
    const params = new HttpParams().set('page', page).set('size', size);
    return this.http.get<any>(this.baseUrl, { params }).pipe(
      map((pageResp) => pageResp?.content ?? [])
    );
  }

  get(id: number): Observable<InvestmentResponseDto> {
    return this.http.get<InvestmentResponseDto>(`${this.baseUrl}/${id}`);
  }

  withdraw(id: number): Observable<number> {
    return this.http.post<number>(`${this.baseUrl}/${id}/withdraw`, {});
  }

  reportSummary(): Observable<Record<string, number>> {
    return this.http.get<Record<string, number>>(`${this.baseUrl}/report/summary`);
  }
}
