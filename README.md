# Tenpo Challenge – Microservices Backend

This project is a solution for the Tenpo technical challenge.  
It implements a microservices-based architecture using modern technologies such as **Spring Boot**, **Kafka**, **Redis**, **PostgreSQL**, and **NGINX**.  
The solution fulfills the requirements for **authentication**, **arithmetic operations**, **call history**, and **rate limiting**.

---

## 📦 Microservices

- **auth-api**: Handles user authentication and JWT token generation.
- **operation-api**: Performs arithmetic operations and percentage application.
- **history-api**: Logs and retrieves API call history.
- **nginx**: Acts as a reverse proxy and applies rate limiting.

---

## 🧪 Features

- ✅ JWT Authentication  
- ✅ Kafka Event Logging  
- ✅ Async History Logging  
- ✅ Rate Limiting (NGINX-based)  
- ✅ Centralized Error Handling  
- ✅ Swagger API Documentation  
- ✅ Docker Compose for local orchestration  

---

## 🗺️ High-Level Architecture

Below is a visual representation of the architecture and the interaction between components:

![Architecture Diagram](docs/calculate-operation-process.png)

### 🔁 Flow Summary

1. The **client** initiates **token generation** using previously registered **username** and **password**.
2.  
   a. The provided **credentials** are validated against the **user database**.  
   b. A **Kafka event** is generated with the **request/response** data to be logged **asynchronously**.
3. The **client** uses the **generated token** to send a body with the **numbers to be summed**.
4.  
   a. To perform the **calculation**, the service first checks if the **percentage value** is **cached in Redis** (cached value expires after **30 minutes**).  
   b. If not found in Redis, the **external percentage service** is called, and the value is **stored in Redis**.  
   c. A **Kafka event** is generated with the **request/response** data to be logged.
5.  
   a. The **client** queries the **history** of requests and responses from **`auth-api`** and **`operation-api`**.  
   b. The **events** sent to **Kafka** are consumed by the **`history-api`**.
6. The **consumed events** are **persisted into the database**.



---

## 🚀 Getting Started

### Prerequisites

- Docker  
- Docker Compose

### Running the project

```bash
docker compose up --build


<COMPLETAR>