# Tenpo Challenge – Microservices Backend

This project is a solution for the Tenpo technical challenge. 
It implements a microservices-based architecture using modern technologies such as Spring Boot, Kafka, Redis, PostgreSQL, and NGINX. 
The solution fulfills the requirements for authentication, arithmetic operations, call history, and rate limiting.

## 📦 Microservices

- **auth-api**: Handles user authentication and JWT token generation.
- **operation-api**: Performs arithmetic operations and percentage application.
- **history-api**: Logs and retrieves API call history.
- **nginx**: Acts as a reverse proxy and applies rate limiting.

## 🧪 Features

- JWT Authentication
- Kafka Event Logging
- Async History Logging
- Rate Limiting (NGINX-based)
- Error Handling
- Swagger API Docs
- Docker Compose for local orchestration

## 🚀 Getting Started

### Prerequisites

- Docker
- Docker Compose

### Running the project

```bash
docker compose up --build
```

Services will be available at:

- API Gateway: `http://localhost`
