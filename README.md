
# TaskVerse

### Smart Team Collaboration & Task Management Platform

TaskVerse is a RESTful backend application built with Java and Spring Boot, designed to simplify team collaboration, task management, and workflow tracking through a centralized platform.

The project focuses on building a secure, maintainable, and scalable backend using modern Java and Spring technologies.

---

## Overview

TaskVerse aims to provide an organized environment where teams can collaborate, assign responsibilities, manage tasks, and track progress efficiently.

The platform is being developed incrementally through independent development sprints, with a focus on clean architecture, secure authentication, API design, and maintainable code.

## Implemented Features

### Sprint 0 – Project Setup & Foundation

- Spring Boot project initialization.
- Java 21 and Maven configuration.
- PostgreSQL database integration.
- Spring Data JPA and Hibernate setup.
- Swagger / OpenAPI integration.
- Initial project structure and configuration.

### Sprint 1 – User Management

- User entity and database design.
- User registration.
- Email uniqueness validation.
- Password encryption using BCrypt.
- Request validation.
- Standard API response structure.
- Global exception handling.
- Custom exceptions.

### Sprint 2 – Authentication & JWT

- User authentication using Spring Security.
- Login using email and password.
- JWT generation and validation.
- Stateless authentication.
- Custom JWT claims.
- Database-backed user validation during JWT verification.
- Token versioning for invalidating previously issued tokens.
- Custom authentication entry point.
- Standardized unauthorized responses.
- Current user profile retrieval.
- Current user profile updates.
- Password change with current password verification.
- Automatic token invalidation after password changes.
- Account deactivation (soft delete).
- Protected user endpoints.

## Technology Stack

| Technology | Purpose |
|---|---|
| Java 21 | Programming Language |
| Spring Boot 4 | Backend Framework |
| Spring Web MVC | REST API Development |
| Spring Data JPA | Data Access |
| Hibernate | ORM |
| Spring Security | Authentication & Security |
| Spring Security OAuth2 Resource Server | JWT Validation |
| Spring Security JOSE | JWT Encoding and Decoding |
| BCrypt | Password Encryption |
| PostgreSQL | Relational Database |
| Swagger / OpenAPI | API Documentation |
| Maven | Dependency Management |
| Lombok | Boilerplate Reduction |
| JUnit 5 | Unit and Integration Testing |
| Mockito | Mocking and Testing |

## Security

TaskVerse uses Spring Security to protect its REST APIs.

Authentication is based on JWT (JSON Web Tokens), with the following security mechanisms:

- Passwords are stored using BCrypt hashing.
- Users authenticate using their email and password.
- Authenticated requests use Bearer JWT tokens.
- JWT signatures and expiration are validated.
- User account status is checked during token validation.
- Token versioning allows previously issued tokens to be invalidated.
- Password changes invalidate existing tokens.
- Disabled accounts cannot use previously issued tokens.

## API Endpoints

### Authentication

| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/v1/auth/register` | Register a new user |
| POST | `/api/v1/auth/login` | Authenticate and receive a JWT |

### User Management

All user endpoints below require authentication.

| Method | Endpoint | Description |
|---|---|---|
| GET | `/api/v1/users/me` | Get current user profile |
| PUT | `/api/v1/users/me` | Update current user profile |
| PATCH | `/api/v1/users/me/password` | Change current user password |
| DELETE | `/api/v1/users/me` | Deactivate current user account |

## API Documentation

API documentation is provided through Swagger UI using OpenAPI.

After starting the application, access:

[Swagger UI](http://localhost:8080/swagger-ui/index.html)

Swagger UI supports exploring and testing the available API endpoints.

For protected endpoints, provide a valid Bearer token using the Authorize option.

## Database

TaskVerse uses PostgreSQL as its relational database.

The development database name is:

`taskverse_db`

Database connection settings are configured through environment variables.

## Getting Started

### Prerequisites

Make sure you have the following installed:

- JDK 21
- Maven
- PostgreSQL
- IntelliJ IDEA or another Java IDE

### Installation

**1. Clone the repository**

```bash
git clone https://github.com/Mahamedgalhoum/TaskVerse.git
```

**2. Navigate to the project directory**

```bash
cd TaskVerse
```

**3. Create the PostgreSQL database**

```sql
CREATE DATABASE taskverse_db;
```

**4. Configure database connection**

Set the following environment variables:

```properties
DB_USERNAME=postgres
DB_PASSWORD=YOUR_PASSWORD
```

Make sure your `application.properties` references these variables:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/taskverse_db
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}
```

Also configure the JWT properties required by the application:

- `jwt.secret`: A Base64-encoded secret key suitable for HS256.
- `jwt.expiration`: Token expiration duration in milliseconds.

Keep database credentials and JWT secrets outside source control.

**5. Run the application**

Using Maven:

```bash
mvn spring-boot:run
```

Or run `TaskverseApplication.java` from your IDE.

The application will be available at:

`http://localhost:8080`

## Testing

The project includes automated tests using JUnit 5, Mockito, and Spring Boot testing support.

Current test results:

- Total tests: 46
- Failures: 0
- Errors: 0
- Skipped: 0

Run the test suite using:

```bash
mvn test
```

## Project Roadmap

| Sprint | Module | Status |
|---|---|---|
| Sprint 0 | Project Setup & Foundation | Completed |
| Sprint 1 | User Management | Completed |
| Sprint 2 | Authentication & JWT | Completed |
| Sprint 3 | Roles & Authorization | Planned |
| Sprint 4 | Team Management | Planned |
| Sprint 5 | Task Management & Workflow | Planned |
| Sprint 6 | Notifications | Planned |
| Sprint 7 | Document Management | Planned |
| Sprint 8 | Activity Logs & AOP | Planned |
| Sprint 9 | Reminders & Scheduling | Planned |
| Sprint 10 | Admin APIs & Statistics | Planned |

## Author

**Mohamed Galhoum**

[GitHub](https://github.com/Mahamedgalhoum)

---

*TaskVerse is currently under active development.*
