<div align="center">

# Financial Investment App

Aplicação de investimentos financeiros baseada em microsserviços (Spring Boot) e frontend em Angular, com comunicação assíncrona via RabbitMQ e orquestração por um API Gateway.

</div>

## Visão geral da arquitetura

- Frontend Angular 18 servido por Nginx (porta 4200 ↦ 80 no container)
- API Gateway (Spring Cloud Gateway) centraliza o roteamento e CORS (porta 8080)
- Microsserviços de domínio:
   - Auth Service (JWT, porta 8081)
   - Investment Service (PostgreSQL + eventos RabbitMQ, porta 8082)
   - Transaction Service (RabbitMQ + integrações, porta 8083)
   - Notification Service (consumidor de eventos RabbitMQ, porta 8083 no compose)
   - Catalog Service (produtos de investimento, porta 8084)
   - Portfolio/Wallet Service (gestão de portfólio e carteira, porta 8085)
   - Welcome Service (rota de boas‑vindas exposta via gateway)
- Infra: RabbitMQ (5672/AMQP, 15672/Console) e PostgreSQL 15 (5432)

Comunicação síncrona: REST via API Gateway. Comunicação assíncrona: RabbitMQ (exchange tópico: `investment.exchange`). Persistência: PostgreSQL para investimentos, H2 em memória em serviços auxiliares (dev/demo).

### Diagrama lógico (alto nível)

Frontend (Angular) → API Gateway → [Auth, Investment, Portfolio, Catalog, Transaction, Welcome]

Investment Service → publica eventos (RabbitMQ) → Notification Service consome e processa

## Componentes e responsabilidades

- API Gateway (`services/apigateway-service`)
   - Spring Cloud Gateway com CORS global e rotas para todos os serviços.
   - Segurança com Spring Security + jjwt para validação básica de JWT.
   - Principais rotas (prefixo externo /api):
      - /api/auth → auth-service:8081 (/auth/**)
      - /api/investments → investment-service:8082 (mantém /api)
      - /api/transactions → transaction-service:8083
      - /api/products → catalog-service:8084
      - /api/portfolio e /api/wallet → portfolio-service:8085
      - /api/welcome → welcome-service:8084 (reescrita para /auth no serviço)
   - Actuator exposto pelo gateway (health, info, metrics, gateway).

- Auth Service (`services/auth-service`)
   - Cadastro e autenticação de usuários com emissão de JWT (segredo configurado).
   - H2 em memória para persistência (dev) e Actuator básico.

- Investment Service (`services/investment-service`)
   - CRUD de investimentos, cálculo de retorno, resgates.
   - PostgreSQL como base (compose provê `postgres`).
   - Publica eventos de domínio no RabbitMQ:
      - Exchange: `investment.exchange`
      - Routing keys: `investment.created`, `investment.withdrawn`

- Notification Service (`services/notification-service`)
   - Consumidor de eventos via `@RabbitListener`.
   - Filas: `investment.created.queue` e `investment.withdrawn.queue` (recomendado DLQ em prod).
   - Processa e persiste notificações ao receber eventos.

- Transaction Service (`services/transaction-service`)
   - Orquestra transações de investimento (integra catálogos, carteira/portfólio); usa RabbitMQ.
   - Config dev com H2.

- Catalog Service (`services/catalog-service`)
   - Catálogo de produtos financeiros (ex.: CDB, Tesouro, Fundos, etc.).

- Portfolio/Wallet Service (`services/portfolio-service`)
   - Gestão de portfólio e endpoints de carteira (`/api/portfolio/**`, `/api/wallet/**`).

- Welcome Service (`services/welcome-service`)
   - Endpoint simplificado de welcome exposto pelo Gateway.

## Frontend (Angular 18)

- Stack: Angular 18, Angular Material, PrimeNG, Chart.js. Build com Node 20; servido por Nginx.
- Config: `src/environments/environment*.ts` define `apiBaseUrl` (default `http://localhost:8080/api`).
- Guards: `auth.guard.ts` e `role.guard.ts` (usa `localStorage` para token/role).
- Serviços HTTP principais:
   - `AuthService`: login/registro em `/auth` (persiste JWT e metadados no localStorage).
   - `InvestmentService`: `/investments` (criar, listar, obter, resgatar, relatórios).
   - `ProductService`: `/products` (CRUD de produtos de investimento).
   - `WalletService`: `/wallet` (saldo, crédito, débito).
- Rotas (exemplos):
   - Login/Cadastro: `/login`, `/registro`, `/registro/admin`
   - Cliente: `/cliente/dashboard`, `/cliente/portfolio`, `/cliente/investimentos`, `/cliente/opcoes`
   - Admin: `/admin/dashboard`, `/admin/opcoes`

## Mensageria (RabbitMQ)

- Exchange tópico: `investment.exchange`
- Routing keys: `investment.created` e `investment.withdrawn`
- Filas: `investment.created.queue`, `investment.withdrawn.queue`
- Produtor: `InvestmentEventProducer` (Investment Service) publica eventos após operações de negócio.
- Consumidor: `NotificationConsumer` (Notification Service) consome eventos e aciona `NotificationService`.
- Padrões recomendados (prod): DLQ, retries com backoff, idempotência no consumidor, versionamento de payloads.

Fluxo típico:
1) Usuário autentica (JWT) via Frontend → Gateway → Auth Service.
2) Usuário cria investimento → Gateway → Investment Service.
3) Investment Service persiste no PostgreSQL e publica `InvestmentCreatedEvent` no RabbitMQ.
4) Notification Service consome o evento e grava/entrega a notificação.
5) Frontend pode consultar notificações/estado via REST.

Campos de eventos (exemplos, a partir dos logs): `userId`, `investmentId`, e metadados do investimento.

## Orquestração com Docker Compose

Arquivo: `docker/docker-compose.yml`

Serviços provisionados:
- `postgres`: banco `investment_db` (user/pass: user/pass), volume persistente `postgres_data`.
- `rabbitmq`: imagem com console de administração (http://localhost:15672, guest/guest).
- `apigateway-service`: exposto em `localhost:8080`.
- `auth-service`, `investment-service`, `notification-service`, `catalog-service`, `portfolio-service`, `transaction-service`, `welcome-service` (expostos na rede interna e roteados via gateway).
- `frontend`: exposto em `http://localhost:4200` (servindo build estático Angular por Nginx).

Notas importantes:
- Perfis `SPRING_PROFILES_ACTIVE=docker` aplicados nos serviços no compose.
- Variáveis de conexão (RabbitMQ e PostgreSQL) injetadas via ambiente.
- Healthchecks garantem ordem de inicialização (RabbitMQ e Postgres).

## Como executar

Pré-requisitos:
- Docker Desktop
- Node 20+ e Angular CLI para executar o frontend em modo dev (opcional)
- Java 21 para builds locais dos serviços (Maven)

Executar com Docker Compose (recomendado):

```bash
docker compose -f docker/docker-compose.yml up -d --build
```

Acesse:
- Frontend: http://localhost:4200
- API Gateway: http://localhost:8080
- RabbitMQ Console: http://localhost:15672 (guest/guest)
- PostgreSQL: localhost:5432 (db: investment_db, user/pass)

Executar localmente (modo dev):
1) Suba RabbitMQ e Postgres (via Docker ou instalados).
2) Rode cada serviço com `mvn spring-boot:run` no respectivo diretório.
3) Frontend dev server: `cd frontend && npm install && npm start` (http://localhost:4200).

Se preferir evitar CORS no dev sem gateway, habilite o proxy no `frontend/nginx.conf` (se servir via Nginx) ou use o gateway como origem única de API.

## Endpoints principais (via Gateway)

- Auth: `POST /api/auth/login`, `POST /api/auth/register`
- Investments: `GET/POST /api/investments`, `GET /api/investments/{id}`, `POST /api/investments/{id}/withdraw`
- Products (Catálogo): `GET/POST/PUT/DELETE /api/products/**`
- Portfolio: `GET/POST/PUT /api/portfolio/**`
- Wallet: `GET/POST /api/wallet/**`
- Transactions: `POST /api/transactions/**`
- Welcome: `GET /api/welcome/**`

Observação: alguns serviços usam H2 em dev; comportamentos e schemas podem variar. Consulte os controllers nos respectivos módulos para detalhes precisos.

## Configuração e variáveis de ambiente

- Gateway (`services/apigateway-service/src/main/resources/application.yml`):
   - Rotas, CORS global, `jwt.secret` (não versionar segredos em produção).
- Auth Service: `jwt.secret`, `jwt.expiration`, H2 em dev.
- Investment Service: datasource PostgreSQL, RabbitMQ (host/port/credenciais), propriedades de exchange/queues/routing.
- Demais serviços: configurações de RabbitMQ e endpoints externos (`portfolio.service.base-url`, etc.).

## Boas práticas adotadas e recomendadas

- API Gateway: centraliza roteamento, CORS e, opcionalmente, autenticação e rate‑limiting.
- Mensageria: separar eventos por exchange e routing key; padronizar contratos e versionamento.
- Resiliência: retries com backoff, circuit breakers (Resilience4j presente no gateway), timeouts.
- Segurança:
   - JWT assinado; validação no gateway e propagação a serviços.
   - Segregação de papéis (guards no frontend e roles no token).
   - Armazenar segredos (JWT secret, senhas) em cofre (ex.: Vault, AWS Secrets, Azure Key Vault).
- Persistência:
   - Usar migrações (Flyway/Liquibase) em produção.
   - Evitar H2 em produção; padronizar PostgreSQL ou outro RDBMS.
- Observabilidade:
   - Spring Boot Actuator em todos os serviços; Prometheus + Grafana para métricas.
   - Logging estruturado (JSON) e correlação (traceId/spanId) com OpenTelemetry.
   - Tracing distribuído (Jaeger/Tempo/Zipkin).
- Containers:
   - Imagens multi-stage; reduzir superfície (alpine/slim) e fixar versões.
   - Healthchecks e readiness/liveness probes (Kubernetes).
- Qualidade:
   - Testes unitários e de contrato (ex.: Spring Cloud Contract) para eventos e REST.
   - CI/CD com build, testes, análise (Sonar), scans de segurança (SCA/SAST), e deploy automatizado.
- Consistência de runtime: padronizar Java 21 em todos os serviços (há Dockerfiles com Java 17 em `investment-service`).

## Possíveis evoluções

1) Integração com gateway de pagamentos
- Criar `payment-service` separado, responsável por iniciar cobranças e receber webhooks de provedores (Stripe, Mercado Pago, PagSeguro, etc.).
- Fluxo sugerido:
   1. Frontend solicita investimento pago → Gateway → Payment Service cria `PaymentIntent` no provedor.
   2. Frontend conclui o pagamento (SDK do provedor) e o provedor chama webhook → Payment Service valida assinatura e publica `payment.confirmed` em RabbitMQ.
   3. Transaction/Investment Service consomem `payment.confirmed`; idempotência por `paymentId`/`idempotencyKey`.
   4. Investment Service efetiva a criação/atualização; publica `investment.created`.
   5. Notification Service notifica o usuário.
- Considerações: idempotência, reconciliação, antifraude, PCI compliance (não trafegar dados sensíveis), webhooks assinados, retries e DLQ.

2) Observabilidade reforçada
- Padronizar Actuator + Micrometer; dashboards para latência, erros, throughput por rota no gateway, consumo de filas e tempos de processamento.

3) Hardening e segurança
- Rate limiting no gateway; WAF; validação de payloads; políticas CORS restritivas.
- OAuth2/OIDC (Keycloak/Cognito/Azure AD) em vez de JWT simples.

4) Escalabilidade e deploy
- Contêineres com HPA (Kubernetes) por fila/serviço; particionamento de filas por tipo.
- Cache distribuído (Redis) para catálogos; CQRS para leituras.

5) DX e testes
- Contratos OpenAPI publicados; coleção Postman/Insomnia.
- Testes de contrato para eventos; testes end‑to‑end com ambiente efêmero (Testcontainers).

## Troubleshooting

- Frontend sobe mas API 404/401: confira `environment.apiBaseUrl` (gateway em 8080), token salvo no localStorage e horário do sistema (expiração JWT).
- Serviços não sobem no compose: verifique health do RabbitMQ/Postgres; use `docker compose ps` e logs.
- Falha ao publicar/consumir eventos: valide exchange, routing key e nomes de filas; confira credenciais do RabbitMQ.
- Mismatch de versões Java: padronize Dockerfiles para Java 21.

## Licença

MIT License.
