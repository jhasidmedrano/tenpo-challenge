
## 🗃️ Database Access & Tables

### 🔌 How to connect

You can connect to the databases using a tool like **DBeaver**, **TablePlus**, or the **psql** CLI.

#### `auth-api` Database (`core-db`)
- **Host**: `localhost`
- **Port**: `5434`
- **Database**: `core`
- **Username**: `tenpo`
- **Password**: `tenpo`

#### `history-api` Database (`history-db`)
- **Host**: `localhost`
- **Port**: `5433`
- **Database**: `history`
- **Username**: `postgres`
- **Password**: `postgres`

---

### 📄 Table Structures

#### `core-db` (Authentication DB)

**Table: users**
| Column   | Type    | Description          |
|----------|---------|----------------------|
| id       | BIGINT  | Primary key          |
| username | VARCHAR | Unique username      |
| password | VARCHAR | Hashed password      |

#### `history-db` (API Logs DB)

**Table: api_call_logs**
| Column    | Type      | Description                        |
|-----------|-----------|------------------------------------|
| id        | BIGSERIAL | Primary key                        |
| service   | VARCHAR   | Service name (e.g., auth, operation) |
| method    | VARCHAR   | HTTP method (GET, POST...)         |
| endpoint  | TEXT      | Endpoint URL                       |
| request   | TEXT      | Sanitized request payload          |
| response  | TEXT      | Sanitized response payload         |
| status    | INTEGER   | HTTP response status               |
| timestamp | TIMESTAMP | Log timestamp                      |

---
