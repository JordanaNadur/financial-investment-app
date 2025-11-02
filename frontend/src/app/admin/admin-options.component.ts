import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ReactiveFormsModule, FormBuilder, Validators } from '@angular/forms';
import { Product, ProductService } from '../services/product.service';
import { ToastService } from '../shared/toast.service';

@Component({
  selector: 'app-admin-options',
  standalone: true,
  imports: [CommonModule, FormsModule, ReactiveFormsModule],
  styleUrls: ['./admin-options.component.scss'],
  template: `
    <nav class="subheader">
      <button class="btn btn-link" (click)="goBack()">← Voltar</button>
      <span class="subtitle">Admin • Produtos Financeiros</span>
    </nav>

    <section class="admin-container">
      <div class="cards-grid">
        <div class="card">
          <h3 class="card-title">{{ editing ? 'Editar Produto' : 'Novo Produto' }}</h3>
          <form [formGroup]="form" (ngSubmit)="onSubmit()" class="form">
            <div class="form-group">
              <label>Nome</label>
              <input formControlName="name" placeholder="Nome do produto" />
              <div class="error" *ngIf="form.get('name')?.touched && form.get('name')?.invalid">
                Informe ao menos 2 caracteres.
              </div>
            </div>

            <div class="form-row">
              <div class="form-group">
                <label>Tipo</label>
                <select formControlName="type">
                  <option value="ACAO">Ação</option>
                  <option value="FUNDO">Fundo de Investimento</option>
                  <option value="TESOURO">Tesouro Direto</option>
                  <option value="CRIPTO">Criptomoeda</option>
                  <option value="RENDA_FIXA">Renda Fixa</option>
                  <option value="CDB">Certificado de Depósito Bancário</option>
                  <option value="LCI">Letra de Crédito Imobiliário</option>
                  <option value="LCA">Letra de Crédito do Agronegócio</option>
                  <option value="DEBENTURE">Debênture</option>
                  <option value="FII">Fundo Imobiliário</option>
                  <option value="ETF">Exchange Traded Fund</option>
                  <option value="OURO">Ouro</option>
                  <option value="CAMBIO">Câmbio</option>
                </select>
              </div>
              <div class="form-group">
                <label>Nível de Risco</label>
                <select formControlName="riskLevel">
                  <option value="BAIXO">Baixo</option>
                  <option value="MEDIO">Médio</option>
                  <option value="ALTO">Alto</option>
                </select>
              </div>
            </div>

            <div class="form-group">
              <label>Descrição</label>
              <input formControlName="description" placeholder="Descrição" />
            </div>

            <div class="form-row">
              <div class="form-group">
                <label>Investimento Mínimo</label>
                <input type="number" step="0.01" formControlName="minimumInvestment" placeholder="0.00" />
              </div>
              <div class="form-group">
                <label>Retorno Mensal (%)</label>
                <input type="number" step="0.01" formControlName="monthlyReturn" placeholder="Ex.: 1.25" />
              </div>
            </div>

            <div class="form-row">
              <div class="form-group">
                <label>Prazo Mínimo (meses)</label>
                <input type="number" step="1" formControlName="minimumTermMonths" placeholder="0" />
              </div>
              <div class="form-group checkbox">
                <label><input type="checkbox" formControlName="active" /> Ativo</label>
              </div>
            </div>

            <div class="form-actions">
              <button type="submit" class="btn btn-primary" [disabled]="form.invalid || saving">
                <span *ngIf="saving" class="spinner"></span>
                {{ editing ? 'Atualizar' : 'Criar' }}
              </button>
              <button type="button" class="btn btn-secondary" (click)="reset()" *ngIf="editing && !saving">Cancelar</button>
            </div>
          </form>
        </div>

        <div class="card">
          <div class="card-header">
            <h3 class="card-title">Produtos</h3>
            <div class="toolbar">
              <input class="search" placeholder="Buscar por nome ou descrição" [(ngModel)]="query" (ngModelChange)="applyFilter()" />
              <div class="pager">
                <label>
                  Página
                  <input type="number" min="1" [(ngModel)]="page" (change)="applyFilter()" />
                </label>
                <label>
                  Itens
                  <input type="number" min="1" [(ngModel)]="pageSize" (change)="applyFilter()" />
                </label>
              </div>
            </div>
          </div>

          <div class="loading" *ngIf="loading">Carregando...</div>

          <table class="table" *ngIf="pageItems?.length; else empty">
            <thead>
              <tr>
                <th>ID</th>
                <th>Nome</th>
                <th>Tipo</th>
                <th>Risco</th>
                <th>Retorno (%)</th>
                <th>Ativo</th>
                <th style="width: 160px;">Ações</th>
              </tr>
            </thead>
            <tbody>
              <tr *ngFor="let p of pageItems">
                <td>{{ p.id }}</td>
                <td class="text-ellipsis" title="{{ p.name }}">{{ p.name }}</td>
                <td>{{ p.type }}</td>
                <td>{{ p.riskLevel }}</td>
                <td>{{ p.monthlyReturn ?? '-' }}</td>
                <td>
                  <span class="badge" [class.badge-success]="p.active" [class.badge-muted]="!p.active">{{ p.active ? 'Ativo' : 'Inativo' }}</span>
                </td>
                <td>
                  <div class="row-actions">
                    <button class="btn btn-light" (click)="edit(p)" [disabled]="saving">Editar</button>
                    <button class="btn btn-danger" (click)="askDelete(p)" [disabled]="saving">Excluir</button>
                  </div>
                </td>
              </tr>
            </tbody>
          </table>
          <ng-template #empty>
            <div class="empty">Nenhum produto cadastrado.</div>
          </ng-template>
        </div>
      </div>
    </section>

    <!-- Modal de confirmação -->
    <div class="overlay" *ngIf="confirmDelete">
      <div class="modal">
        <h3>Confirmar exclusão</h3>
        <p>Deseja excluir o produto "{{ confirmDelete?.name }}"?</p>
        <div class="modal-actions">
          <button class="btn btn-light" (click)="confirmDelete=null" [disabled]="saving">Cancelar</button>
          <button class="btn btn-danger" (click)="del(confirmDelete)" [disabled]="saving">
            <span *ngIf="saving" class="spinner"></span>
            Excluir
          </button>
        </div>
      </div>
    </div>
  `,
})
export class AdminOptionsComponent {
  private fb = inject(FormBuilder);
  private service = inject(ProductService);
  private toast = inject(ToastService);

  products: Product[] = [];
  view: Product[] = [];
  pageItems: Product[] = [];
  query = '';
  page = 1;
  pageSize = 10;
  editing: Product | null = null;
  error = '';
  loading = false;
  saving = false;
  confirmDelete: Product | null = null;

  form = this.fb.group({
    name: this.fb.control<string>('', { validators: [Validators.required, Validators.minLength(2)] }),
    type: this.fb.control<Product['type']>('RENDA_FIXA', { validators: [Validators.required] }),
    description: this.fb.control<string>(''),
    riskLevel: this.fb.control<Product['riskLevel']>('BAIXO', { validators: [Validators.required] }),
    minimumInvestment: this.fb.control<number>(0, { validators: [Validators.min(0)] }),
    monthlyReturn: this.fb.control<number>(0, { validators: [Validators.min(0)] }),
    minimumTermMonths: this.fb.control<number>(0, { validators: [Validators.min(0)] }),
    active: this.fb.control<boolean>(true),
  });

  constructor() {
    this.load();
  }

  goBack() {
    window.history.back();
  }

  load() {
    this.error = '';
    this.loading = true;
    this.service.list().subscribe({
      next: (res) => {
        this.products = res || [];
        this.applyFilter();
        this.loading = false;
      },
      error: (err) => { this.error = this.extractErr(err); this.loading = false; this.toast.error(this.error); },
    });
  }

  onSubmit() {
    if (this.form.invalid) return;
    this.error = '';
    this.saving = true;
    const raw = this.form.getRawValue();
    const payload: Product = {
      name: raw.name!,
      type: raw.type!,
      description: raw.description || undefined,
      riskLevel: raw.riskLevel!,
      minimumInvestment: raw.minimumInvestment ?? 0,
      monthlyReturn: raw.monthlyReturn ?? 0,
      minimumTermMonths: raw.minimumTermMonths ?? 0,
      active: raw.active ?? true,
    };
    if (this.editing?.id) {
      this.service.update(this.editing.id, payload).subscribe({
        next: (p) => {
          this.toast.success('Produto atualizado com sucesso');
          const idx = this.products.findIndex((x) => x.id === p.id);
          if (idx >= 0) this.products[idx] = p;
          this.applyFilter();
          this.reset();
          this.saving = false;
        },
        error: (err) => { this.error = this.extractErr(err); this.toast.error(this.error); this.saving = false; },
      });
    } else {
      this.service.create(payload).subscribe({
        next: (p) => {
          this.toast.success('Produto criado com sucesso');
          this.products = [p, ...this.products];
          this.applyFilter();
          this.reset();
          this.saving = false;
        },
        error: (err) => { this.error = this.extractErr(err); this.toast.error(this.error); this.saving = false; },
      });
    }
  }

  edit(p: Product) {
    this.editing = p;
    this.form.patchValue({
      name: p.name,
      type: p.type,
      description: p.description || '',
      riskLevel: p.riskLevel,
      minimumInvestment: p.minimumInvestment,
      monthlyReturn: p.monthlyReturn,
      minimumTermMonths: p.minimumTermMonths,
      active: !!p.active,
    });
  }

  askDelete(p: Product) {
    this.confirmDelete = p;
  }

  del(p: Product) {
    if (!p.id) return;
    this.saving = true;
    this.service.remove(p.id).subscribe({
      next: () => {
        this.toast.info('Produto excluído');
        this.products = this.products.filter((x) => x.id !== p.id);
  this.applyFilter();
  if (this.editing?.id === p.id) this.reset();
        this.confirmDelete = null;
        this.saving = false;
      },
      error: (err) => { this.error = this.extractErr(err); this.toast.error(this.error); this.saving = false; },
    });
  }

  reset() {
    this.editing = null;
    this.form.reset({
      name: '',
      type: 'RENDA_FIXA',
      description: '',
      riskLevel: 'BAIXO',
      minimumInvestment: 0,
      monthlyReturn: 0,
      minimumTermMonths: 0,
      active: true,
    });
  }

  private extractErr(err: any): string {
    if (!err) return 'Erro desconhecido';
    if (typeof err === 'string') return err;
    if (err.error?.message) return err.error.message;
    if (err.status) return `Erro ${err.status}`;
    return 'Falha na operação';
  }

  applyFilter() {
    const q = this.query.trim().toLowerCase();
    this.view = !q
      ? [...this.products]
      : this.products.filter(
          (p) =>
            p.name?.toLowerCase().includes(q) ||
            (p.description || '').toLowerCase().includes(q)
        );
    this.applyPage();
  }

  applyPage() {
    const start = (this.page - 1) * this.pageSize;
    this.pageItems = this.view.slice(start, start + this.pageSize);
  }
}
