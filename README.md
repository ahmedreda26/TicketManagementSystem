# 🎟️ Ticket Service Management System

[![Java](https://img.shields.io/badge/Java-17+-brightgreen)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.1+-blue)](https://spring.io/projects/spring-boot)
[![Database](https://img.shields.io/badge/Database-Oracle%20XE-orange)](https://hub.docker.com/r/gvenzl/oracle-xe)
[![Docker](https://img.shields.io/badge/Docker-Supported-blue)](https://www.docker.com/)
[![License](https://img.shields.io/github/license/your-org/your-repo)](LICENSE)

A full-featured microservices-based Ticket Management System with **JWT authentication**, **CQRS pattern**, and **Hexagonal Architecture**, designed for extensibility and maintainability.

---

## 📦 Tech Stack

- **Backend**: Java 17, Spring Boot 3+, Spring Security, Spring Data JPA
- **Architecture**: Microservices, CQRS, Hexagonal Architecture
- **Database**: Oracle XE (via Docker)
- **Security**: JWT Authentication & Role-based Authorization
- **DevOps**: Docker, Docker Compose

---

## 🚀 Getting Started

### 1. Clone the Repository

```bash
git clone https://github.com/your-username/ticket-service-management.git
cd ticket-service-management
```

### 2. Build All Services

```bash
mvn clean install
```

---

## 🛠️ Configuration

### Environment Variables (can be set in `.env`)

| Key | Description |
|-----|-------------|
| `JWT_SECRET_BASE64` | Base64-encoded JWT secret key (at least 32 bytes) |
| `SPRING_DATASOURCE_URL` | Oracle JDBC URL |
| `SPRING_DATASOURCE_USERNAME` | Oracle DB username |
| `SPRING_DATASOURCE_PASSWORD` | Oracle DB password |

Or use `application.yml`:

```yaml
spring:
  datasource:
    url: jdbc:oracle:thin:@oracle-db:1521/XEPDB1
    username: TicketManagmentSystem
    password: 1234
  jpa:
    show-sql: true
    hibernate:
      ddl-auto: update
```

---

## 🗃️ Database Setup (Oracle XE)

Run Oracle XE in Docker:

```bash
docker run -d   --name oracle-db   -p 1521:1521   -e ORACLE_PASSWORD=1234   gvenzl/oracle-xe
```

Then create the schema:

```sql
CREATE USER TicketManagmentSystem IDENTIFIED BY 1234;
GRANT CONNECT, RESOURCE TO TicketManagmentSystem;
```

---

## ▶️ Running Services with Docker Compose

Make sure `docker-compose.yml` is set up with:

- API Gateway → `8080`
- User Service → `8081`
- Ticket Service → `8082`

Then run:

```bash
docker-compose up --build
```

---

## 🔐 Authentication Flow

### 🧾 Login Request

```http
POST /api/auth/login
Content-Type: application/json

{
  "username": "admin",
  "password": "admin123"
}
```

**Response:**
```json
{
  "token": "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

Use the token in subsequent requests:

```http
Authorization: Bearer <JWT_TOKEN>
```

---

## 📡 Example APIs

### ✅ Get Ticket by ID

```http
GET /api/tickets/1
Authorization: Bearer <JWT_TOKEN>
```

### 🆕 Create a Ticket

```http
POST /api/tickets
Authorization: Bearer <JWT_TOKEN>
Content-Type: application/json

{
  "title": "Login issue",
  "description": "Cannot access dashboard",
  "status": "OPEN"
}
```

---

## 🔐 Roles & Permissions

| Role      | Permissions                             |
|-----------|-----------------------------------------|
| ADMIN     | Full access to all APIs                 |
| SUPPORT   | View & manage tickets only              |
| CUSTOMER  | Create/view own tickets                 |

---

## 🧱 Project Structure

```bash
ticket-service/
├── controller/         # REST Controllers
├── domain/             # Domain Models
├── application/        # Use cases, DTOs, Mappers
│   ├── service/
│   └── mapper/
├── infrastructure/     # Configs & External dependencies
├── repository/         # Spring Data JPA Repos
```
