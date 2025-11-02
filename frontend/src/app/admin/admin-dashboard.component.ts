import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-admin-dashboard',
  standalone: true,
  imports: [RouterLink],
  template: `
    <section class="admin-dashboard">
      <h1>Dashboard do Administrador</h1>
      <div class="cards">
        <a routerLink="/admin/opcoes" class="admin-card" title="Gerenciar Produtos">
          <div class="icon">🧩</div>
          <div class="content">
            <h3>Catálogo de Produtos</h3>
            <p>Crie, edite e gerencie os produtos de investimento disponíveis para os clientes.</p>
          </div>
          <div class="chevron">→</div>
        </a>
      </div>
    </section>
  `,
  styleUrls: ['./admin-dashboard.component.scss']
})
export class AdminDashboardComponent {}
