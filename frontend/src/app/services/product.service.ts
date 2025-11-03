import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, map } from 'rxjs';
import { environment } from '../../environments/environment';

// Enums alinhados ao backend (valores em PT-BR)
export type ProductType =
  | 'ACAO'
  | 'FUNDO'
  | 'TESOURO'
  | 'CRIPTO'
  | 'RENDA_FIXA'
  | 'CDB'
  | 'LCI'
  | 'LCA'
  | 'DEBENTURE'
  | 'FII'
  | 'ETF'
  | 'OURO'
  | 'CAMBIO';
export type RiskLevel = 'BAIXO' | 'MEDIO' | 'ALTO';

// DTOs Backend
export interface ProductResponseDto {
  id: number;
  name: string;
  type: ProductType;
  description: string;
  riskLevel: RiskLevel;
  minimumInvestment: number;
  monthlyReturn: number;
  minimumTermMonths: number;
  active: boolean;
}

export interface ProductRequestDto {
  name: string;
  type: ProductType;
  description: string;
  riskLevel: RiskLevel;
  minimumInvestment: number;
  monthlyReturn: number;
  minimumTermMonths: number;
  active: boolean;
}

// Modelo usado pela UI (alinhado ao backend)
export interface Product {
  id?: number;
  name: string;
  type: ProductType;
  description?: string;
  riskLevel: RiskLevel;
  minimumInvestment: number;
  monthlyReturn: number; // antes "rate"; manteremos "monthlyReturn" para clareza
  minimumTermMonths: number;
  active?: boolean;
}

@Injectable({ providedIn: 'root' })
export class ProductService {
  private http = inject(HttpClient);
  private baseUrl = `${environment.apiBaseUrl}/products`;

  private toUi(dto: ProductResponseDto): Product {
    return {
      id: dto.id,
      name: dto.name,
      type: dto.type,
      description: dto.description,
      riskLevel: dto.riskLevel,
      minimumInvestment: dto.minimumInvestment,
      monthlyReturn: dto.monthlyReturn,
      minimumTermMonths: dto.minimumTermMonths,
      active: dto.active,
    };
  }

  private toRequest(p: Product): ProductRequestDto {
    return {
      name: p.name,
      type: p.type,
      description: p.description || '',
      riskLevel: p.riskLevel,
      minimumInvestment: p.minimumInvestment,
      monthlyReturn: p.monthlyReturn,
      minimumTermMonths: p.minimumTermMonths,
      active: p.active ?? true,
    };
  }

  list(): Observable<Product[]> {
    return this.http.get<ProductResponseDto[]>(this.baseUrl).pipe(map(arr => (arr || []).map(this.toUi)));
  }

  get(id: number): Observable<Product> {
    return this.http.get<ProductResponseDto>(`${this.baseUrl}/${id}`).pipe(map(this.toUi));
  }

  getByName(name: string): Observable<Product> {
    return this.http.get<ProductResponseDto>(`${this.baseUrl}/name/${encodeURIComponent(name)}`).pipe(map(this.toUi));
  }

  create(payload: Product): Observable<Product> {
    const body = this.toRequest(payload);
    return this.http.post<ProductResponseDto>(this.baseUrl, body).pipe(map(this.toUi));
  }

  // Observação: o backend atual não expõe PUT/UPDATE. Mantemos método para futura compatibilidade;
  // agora o backend expõe PUT /products/{id}.
  update(id: number, payload: Product): Observable<Product> {
    const body = this.toRequest(payload);
    return this.http.put<ProductResponseDto>(`${this.baseUrl}/${id}`, body).pipe(map(this.toUi));
  }

  remove(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`);
  }
}
