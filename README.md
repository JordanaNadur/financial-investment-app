# Financial Investment App

A microservices-based financial investment application built with Spring Boot for backend services and Angular for the frontend. The application allows users to manage investments, authenticate, and receive notifications via RabbitMQ messaging.

## Architecture

The application consists of the following components:

- **Auth Service**: Handles user authentication and registration using JWT tokens.
- **Investment Service**: Manages investment creation, calculation of returns, and withdrawals.
- **Transaction Service**: Handles investment transactions, integrating with catalog, wallet, and portfolio services.
- **Welcome Service**: Provides a simple welcome endpoint for the application.
- **Notification Service**: Processes notifications asynchronously via RabbitMQ when investments are created.
- **Frontend**: Angular application with PrimeNG and Angular Material for user interface.
- **RabbitMQ**: Message broker for inter-service communication.

Services communicate via REST APIs and RabbitMQ for event-driven notifications.

## Technologies Used

- **Backend**: Spring Boot, Java, Spring Security, JWT, RabbitMQ, H2 Database (in-memory for demo)
- **Frontend**: Angular 18, PrimeNG, Angular Material
- **Containerization**: Docker, Docker Compose
- **Build Tools**: Maven (for services), Angular CLI (for frontend)

## Prerequisites

- Java 21
- Node.js 18 or higher
- Docker and Docker Compose
- Angular CLI (install globally: `npm install -g @angular/cli`)

## Installation and Setup

1. **Clone the repository**:
   ```
   git clone <repository-url>
   cd financial-investment-app
   ```

2. **Backend Services**:
   - Each service is a Maven project. No additional setup is required as dependencies are managed via `pom.xml`.
   - Services use H2 in-memory database for simplicity.

3. **Frontend**:
   - Navigate to the frontend directory:
     ```
     cd frontend
     ```
   - Install dependencies:
     ```
     npm install
     ```

4. **Docker Setup**:
   - Ensure Docker Desktop is running.
   - The `docker/docker-compose.yml` file defines the services.

## Running the Application

### Option 1: Using Docker Compose (Recommended)

1. From the root directory:
   ```
   docker-compose -f docker/docker-compose.yml up -d
   ```
   This will start all services: auth-service (port 8081), investment-service (port 8082), transaction-service (port 8083), welcome-service (port 8084), notification-service (port 8085), and RabbitMQ (ports 5672, 15672).

2. Start the frontend separately:
   ```
   cd frontend
   ng serve --host 0.0.0.0 --port 4200
   ```
   Access the frontend at `http://localhost:4200`.

### Option 2: Running Locally

1. **Start RabbitMQ**:
   - Use Docker: `docker run -d --name rabbitmq -p 5672:5672 -p 15672:15672 rabbitmq:3-management`
   - Or install RabbitMQ locally.

2. **Start Backend Services**:
   - Auth Service: `cd services/auth-service && mvn spring-boot:run`
   - Investment Service: `cd services/investment-service && mvn spring-boot:run`
   - Transaction Service: `cd services/transaction-service && mvn spring-boot:run`
   - Notification Service: `cd services/notification-service && mvn spring-boot:run`

3. **Start Frontend**:
   ```
   cd frontend
   ng serve
   ```
   Access at `http://localhost:4200`.

## API Endpoints

### Auth Service (Port 8081)
- `POST /api/auth/register`: Register a new user
- `POST /api/auth/login`: Authenticate user and return JWT

### Investment Service (Port 8082)
- `POST /api/investments`: Create a new investment (requires JWT)
- `GET /api/investments/{id}`: Get investment details
- `PUT /api/investments/{id}/withdraw`: Withdraw from investment
- `GET /api/investments/{id}/returns`: Calculate returns

### Transaction Service (Port 8083)
- `POST /api/transactions/invest`: Process an investment transaction (requires JWT)

### Welcome Service (Port 8084)
- `GET /api/welcome`: Get a welcome message

## Usage

1. Register a user via the frontend or API.
2. Login to obtain a JWT token.
3. Create investments, view returns, and manage withdrawals.
4. Notifications are sent asynchronously when investments are created.

## Configuration

- Application properties are in `src/main/resources/application.properties` for each service.
- For Docker, profiles are set to `docker` to use appropriate configurations.

## Development

- **Frontend**: Use `ng serve` for development server with hot reload.
- **Backend**: Use `mvn spring-boot:run` for each service.
- **Testing**: Run `ng test` for frontend, `mvn test` for backend services.

## Contributing

1. Fork the repository.
2. Create a feature branch.
3. Commit changes.
4. Push to the branch.
5. Create a Pull Request.

## License

This project is licensed under the MIT License.
