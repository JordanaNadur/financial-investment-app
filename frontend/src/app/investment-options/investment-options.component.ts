import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';

@Component({
  selector: 'app-investment-options',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule
  ],
  template: `
    <div class="header">
      <h1>Opções de Investimento</h1>
      <button class="back-btn" (click)="goBack()">
        ← Voltar ao Portfólio
      </button>
    </div>

    <div class="investment-options-container">
      <div class="options-grid">
        <div class="option-card" (click)="selectInvestment('CDB')">
          <div class="card-icon">🏦</div>
          <h3>CDB</h3>
          <p>Certificado de Depósito Bancário</p>
          <span class="return-rate">110% - 130% CDI</span>
          <div class="risk-level low">Risco Baixo</div>
        </div>

        <div class="option-card" (click)="selectInvestment('Tesouro Direto')">
          <div class="card-icon">🏛️</div>
          <h3>Tesouro Direto</h3>
          <p>Títulos públicos do governo</p>
          <span class="return-rate">SELIC + 0,5%</span>
          <div class="risk-level low">Risco Baixo</div>
        </div>

        <div class="option-card" (click)="selectInvestment('Ações')">
          <div class="card-icon">📈</div>
          <h3>Ações</h3>
          <p>Renda variável - Bolsa de valores</p>
          <span class="return-rate">Variável</span>
          <div class="risk-level high">Risco Alto</div>
        </div>

        <div class="option-card" (click)="selectInvestment('LCI/LCA')">
          <div class="card-icon">🏠</div>
          <h3>LCI/LCA</h3>
          <p>Letras de Crédito Imobiliário/Agronegócio</p>
          <span class="return-rate">90% - 105% CDI</span>
          <div class="risk-level low">Risco Baixo</div>
        </div>
      </div>

      <!-- Detalhes dos produtos -->
      <div class="product-details" *ngIf="selectedInvestment && !showInvestmentForm">
        <h3>{{ getInvestmentTitle(selectedInvestment) }}</h3>
        
        <!-- CDB Details -->
        <div *ngIf="selectedInvestment === 'CDB'" class="products-list">
          <div class="product-item" *ngFor="let product of cdbProducts" (click)="selectProduct(product)">
            <div class="product-main-info">
              <div class="rate-info">
                <span class="cdi-rate">CDI: {{ currentCDI }}% a.a.</span>
                <span class="percentage">{{ product.percentage }}% do CDI</span>
                <span class="final-rate">≈ {{ (currentCDI * product.percentage / 100).toFixed(2) }}% a.a.</span>
              </div>
              <div class="maturity">Vencimento: {{ product.maturity }}</div>
            </div>
            <div class="product-secondary-info">
              <div class="risk-protection">
                <span class="risk">Risco: {{ product.risk }}</span>
                <span class="fgc">Proteção FGC: {{ product.fgc }}</span>
                <span class="tax">IR: {{ product.taxRate }}</span>
              </div>
              <div class="issuer-min">
                <span class="issuer">Emissor: {{ product.issuer }}</span>
                <span class="min-value">Valor mínimo: {{ product.minValue }}</span>
              </div>
            </div>
          </div>
        </div>

        <!-- Tesouro Direto Details -->
        <div *ngIf="selectedInvestment === 'Tesouro Direto'" class="products-list">
          <div class="product-item" *ngFor="let product of tesouroProducts" (click)="selectProduct(product)">
            <div class="product-main-info">
              <div class="rate-info">
                <span class="bond-name">{{ product.name }}</span>
                <span class="final-rate">{{ product.rate }}% a.a.</span>
              </div>
              <div class="maturity">Vencimento: {{ product.maturity }}</div>
            </div>
            <div class="product-secondary-info">
              <div class="risk-protection">
                <span class="risk">Risco: {{ product.risk }}</span>
                <span class="government">Garantia: Tesouro Nacional</span>
                <span class="tax">IR: {{ product.taxRate }}</span>
              </div>
              <div class="issuer-min">
                <span class="issuer">Emissor: {{ product.issuer }}</span>
                <span class="min-value">Valor mínimo: {{ product.minValue }}</span>
              </div>
            </div>
          </div>
        </div>

        <!-- Ações Details -->
        <div *ngIf="selectedInvestment === 'Ações'" class="products-list">
          <div class="product-item" *ngFor="let product of stockProducts" (click)="selectProduct(product)">
            <div class="product-main-info">
              <div class="rate-info">
                <span class="stock-code">{{ product.code }}</span>
                <span class="company-name">{{ product.company }}</span>
                <span class="price">R$ {{ product.price }}</span>
              </div>
              <div class="dividend">Dividend Yield: {{ product.dividendYield }}%</div>
            </div>
            <div class="product-secondary-info">
              <div class="risk-protection">
                <span class="risk">Risco: {{ product.risk }}</span>
                <span class="sector">Setor: {{ product.sector }}</span>
                <span class="tax">IR: {{ product.taxRate }}</span>
              </div>
              <div class="issuer-min">
                <span class="market">Mercado: {{ product.market }}</span>
                <span class="min-value">Valor mínimo: {{ product.minValue }}</span>
              </div>
            </div>
          </div>
        </div>

        <!-- LCI/LCA Details -->
        <div *ngIf="selectedInvestment === 'LCI/LCA'" class="products-list">
          <div class="product-item" *ngFor="let product of lciLcaProducts" (click)="selectProduct(product)">
            <div class="product-main-info">
              <div class="rate-info">
                <span class="cdi-rate">CDI: {{ currentCDI }}% a.a.</span>
                <span class="percentage">{{ product.percentage }}% do CDI</span>
                <span class="final-rate">≈ {{ (currentCDI * product.percentage / 100).toFixed(2) }}% a.a.</span>
              </div>
              <div class="maturity">Vencimento: {{ product.maturity }}</div>
            </div>
            <div class="product-secondary-info">
              <div class="risk-protection">
                <span class="risk">Risco: {{ product.risk }}</span>
                <span class="fgc">Proteção FGC: {{ product.fgc }}</span>
                <span class="tax">IR: Isento</span>
              </div>
              <div class="issuer-min">
                <span class="issuer">Emissor: {{ product.issuer }}</span>
                <span class="min-value">Valor mínimo: {{ product.minValue }}</span>
              </div>
            </div>
          </div>
        </div>

        <button class="back-to-categories-btn" (click)="backToCategories()">
          ← Voltar às categorias
        </button>
      </div>

      <!-- Formulário de investimento -->
      <div class="investment-form" *ngIf="showInvestmentForm && selectedProduct">
        <h3>Investir em {{ selectedProduct.name || selectedProduct.code || selectedProduct.issuer }}</h3>
        
        <div class="product-summary">
          <div class="summary-item">
            <span class="label">Rentabilidade:</span>
            <span class="value">{{ getProductReturnDisplay() }}</span>
          </div>
          <div class="summary-item">
            <span class="label">Vencimento:</span>
            <span class="value">{{ selectedProduct.maturity }}</span>
          </div>
          <div class="summary-item">
            <span class="label">Valor mínimo:</span>
            <span class="value">{{ selectedProduct.minValue }}</span>
          </div>
        </div>

        <form [formGroup]="investmentForm" (ngSubmit)="confirmInvestment()">
          <div class="form-group">
            <label for="amount">Valor do investimento:</label>
            <input 
              id="amount" 
              type="number" 
              formControlName="amount" 
              [placeholder]="'Valor mínimo: ' + selectedProduct.minValue"
              [min]="getMinInvestmentValue()"
            >
            <div class="error" *ngIf="investmentForm.get('amount')?.invalid && investmentForm.get('amount')?.touched">
              {{ getAmountErrorMessage() }}
            </div>
          </div>

          <div class="form-buttons">
            <button type="button" class="cancel-btn" (click)="cancelInvestment()">
              Cancelar
            </button>
            <button type="submit" class="confirm-btn" [disabled]="investmentForm.invalid">
              Confirmar Investimento
            </button>
          </div>
        </form>
      </div>
    </div>
  `,
  styleUrls: ['./investment-options.component.scss']
})
export class InvestmentOptionsComponent {
  selectedInvestment: string | null = null;
  selectedProduct: any = null;
  showInvestmentForm = false;
  investmentForm: FormGroup;
  currentCDI = 13.75; // CDI atual (exemplo realista)

  cdbProducts = [
    {
      issuer: 'Banco Inter',
      percentage: 125,
      maturity: '24 meses',
      risk: 'Baixo',
      fgc: 'Até R$ 250.000',
      taxRate: 'Tabela regressiva IR',
      minValue: 'R$ 100,00'
    },
    {
      issuer: 'BTG Pactual',
      percentage: 130,
      maturity: '36 meses',
      risk: 'Baixo',
      fgc: 'Até R$ 250.000',
      taxRate: 'Tabela regressiva IR',
      minValue: 'R$ 1.000,00'
    },
    {
      issuer: 'Banco Safra',
      percentage: 128,
      maturity: '18 meses',
      risk: 'Baixo',
      fgc: 'Até R$ 250.000',
      taxRate: 'Tabela regressiva IR',
      minValue: 'R$ 500,00'
    }
  ];

  tesouroProducts = [
    {
      name: 'Tesouro SELIC 2029',
      rate: 13.85,
      maturity: '01/03/2029',
      risk: 'Muito Baixo',
      taxRate: 'Tabela regressiva',
      issuer: 'Tesouro Nacional',
      minValue: 'R$ 30,00'
    },
    {
      name: 'Tesouro Prefixado 2027',
      rate: 12.50,
      maturity: '01/01/2027',
      risk: 'Baixo',
      taxRate: 'Tabela regressiva',
      issuer: 'Tesouro Nacional',
      minValue: 'R$ 30,00'
    },
    {
      name: 'Tesouro IPCA+ 2035',
      rate: 6.25,
      maturity: '15/05/2035',
      risk: 'Baixo',
      taxRate: 'Tabela regressiva',
      issuer: 'Tesouro Nacional',
      minValue: 'R$ 30,00'
    }
  ];

  stockProducts = [
    {
      code: 'PETR4',
      company: 'Petrobras',
      price: '38.24',
      dividendYield: '12.5',
      risk: 'Alto',
      sector: 'Petróleo e Gás',
      taxRate: '15% sobre ganho capital',
      market: 'B3 - Bovespa',
      minValue: '1 ação (R$ 38,24)'
    },
    {
      code: 'VALE3',
      company: 'Vale S.A.',
      price: '65.12',
      dividendYield: '8.7',
      risk: 'Alto',
      sector: 'Mineração',
      taxRate: '15% sobre ganho capital',
      market: 'B3 - Bovespa',
      minValue: '1 ação (R$ 65,12)'
    },
    {
      code: 'ITUB4',
      company: 'Itaú Unibanco',
      price: '32.85',
      dividendYield: '9.2',
      risk: 'Médio-Alto',
      sector: 'Financeiro',
      taxRate: '15% sobre ganho capital',
      market: 'B3 - Bovespa',
      minValue: '1 ação (R$ 32,85)'
    }
  ];

  lciLcaProducts = [
    {
      issuer: 'Banco ABC Brasil',
      percentage: 95,
      maturity: '24 meses',
      risk: 'Baixo',
      fgc: 'Até R$ 250.000',
      minValue: 'R$ 1.000,00',
      type: 'LCI'
    },
    {
      issuer: 'Banco Daycoval',
      percentage: 98,
      maturity: '36 meses',
      risk: 'Baixo',
      fgc: 'Até R$ 250.000',
      minValue: 'R$ 5.000,00',
      type: 'LCA'
    },
    {
      issuer: 'Banco Pine',
      percentage: 102,
      maturity: '30 meses',
      risk: 'Médio-Baixo',
      fgc: 'Até R$ 250.000',
      minValue: 'R$ 2.500,00',
      type: 'LCI'
    }
  ];

  constructor(private router: Router, private fb: FormBuilder) {
    this.investmentForm = this.fb.group({
      amount: ['', [Validators.required, Validators.min(100)]]
    });
  }

  goBack() {
    this.router.navigate(['/investments']);
  }

  selectInvestment(type: string) {
    this.selectedInvestment = type;
    this.selectedProduct = null;
    this.showInvestmentForm = false;
  }

  selectProduct(product: any) {
    this.selectedProduct = product;
    this.showInvestmentForm = true;
    
    // Define valor mínimo baseado no produto
    const minValue = this.getMinInvestmentValue();
    this.investmentForm.get('amount')?.setValidators([Validators.required, Validators.min(minValue)]);
    this.investmentForm.get('amount')?.updateValueAndValidity();
  }

  backToCategories() {
    this.selectedInvestment = null;
    this.selectedProduct = null;
    this.showInvestmentForm = false;
  }

  cancelInvestment() {
    this.showInvestmentForm = false;
    this.selectedProduct = null;
    this.investmentForm.reset();
  }

  getInvestmentTitle(type: string): string {
    switch (type) {
      case 'CDB': return 'Certificados de Depósito Bancário';
      case 'Tesouro Direto': return 'Títulos do Tesouro Nacional';
      case 'Ações': return 'Ações na Bolsa de Valores';
      case 'LCI/LCA': return 'Letras de Crédito Imobiliário e Agronegócio';
      default: return type;
    }
  }

  getMinInvestmentValue(): number {
    if (!this.selectedProduct) return 100;
    
    if (this.selectedInvestment === 'Ações') {
      return parseFloat(this.selectedProduct.price);
    } else {
      return parseFloat(this.selectedProduct.minValue.replace(/[^\d,]/g, '').replace(',', '.')) || 100;
    }
  }

  getAmountErrorMessage(): string {
    const minValue = this.getMinInvestmentValue();
    return `O valor mínimo é R$ ${minValue.toFixed(2).replace('.', ',')}`;
  }

  getProductReturnDisplay(): string {
    if (!this.selectedProduct) return '';
    
    if (this.selectedInvestment === 'CDB' || this.selectedInvestment === 'LCI/LCA') {
      const finalRate = (this.currentCDI * this.selectedProduct.percentage / 100).toFixed(2);
      return `${this.selectedProduct.percentage}% do CDI (≈ ${finalRate}% a.a.)`;
    } else if (this.selectedInvestment === 'Tesouro Direto') {
      return `${this.selectedProduct.rate}% a.a.`;
    } else if (this.selectedInvestment === 'Ações') {
      return `Variável (DY: ${this.selectedProduct.dividendYield}%)`;
    }
    
    return '';
  }

  confirmInvestment() {
    if (this.investmentForm.valid && this.selectedProduct) {
      const formData = this.investmentForm.value;
      let investmentName = '';
      
      // Personaliza o nome baseado no produto selecionado
      if (this.selectedInvestment === 'CDB') {
        investmentName = `CDB ${this.selectedProduct.issuer} - ${this.selectedProduct.percentage}% CDI`;
      } else if (this.selectedInvestment === 'Tesouro Direto') {
        investmentName = this.selectedProduct.name;
      } else if (this.selectedInvestment === 'Ações') {
        investmentName = `${this.selectedProduct.code} - ${this.selectedProduct.company}`;
      } else if (this.selectedInvestment === 'LCI/LCA') {
        investmentName = `${this.selectedProduct.type} ${this.selectedProduct.issuer} - ${this.selectedProduct.percentage}% CDI`;
      }

      // Simula o processamento do investimento
      const newInvestment = {
        name: investmentName,
        type: this.getInvestmentType(this.selectedInvestment!),
        value: formData.amount,
        return: this.getEstimatedReturn(this.selectedInvestment!),
        date: new Date()
      };

      // Salva no localStorage para usar na página de investimentos
      const existingInvestments = JSON.parse(localStorage.getItem('investments') || '[]');
      existingInvestments.push(newInvestment);
      localStorage.setItem('investments', JSON.stringify(existingInvestments));

      alert(`Investimento realizado com sucesso! 🎉\n\nProduto: ${investmentName}\nValor: ${formData.amount.toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' })}\nVencimento: ${this.selectedProduct.maturity}\n\nRetornando ao seu portfólio...`);
      
      this.router.navigate(['/investments']);
    }
  }

  private getInvestmentType(investment: string): string {
    if (investment === 'Ações') return 'Renda Variável';
    return 'Renda Fixa';
  }

  private getEstimatedReturn(investment: string): number {
    switch (investment) {
      case 'CDB': return Math.random() * 5 + 10; // 10-15%
      case 'Tesouro Direto': return Math.random() * 3 + 11; // 11-14%
      case 'Ações': return Math.random() * 20 + 5; // 5-25%
      case 'LCI/LCA': return Math.random() * 4 + 8; // 8-12%
      default: return 10;
    }
  }
}
