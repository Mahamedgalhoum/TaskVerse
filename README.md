
# TaskVerse

### Smart Team Collaboration & Task Management Platform

TaskVerse is a RESTful backend application built with Java and Spring Boot, designed to simplify team collaboration, task management, and workflow tracking through a centralized platform.

The project focuses on building a secure, maintainable, and scalable backend using modern Java and Spring technologies.

---

## Overview

TaskVerse aims to provide an organized environment where teams can collaborate, assign responsibilities, manage tasks, and track progress efficiently.

The platform is being developed incrementally through independent development sprints, with a focus on clean architecture, secure authentication, role-based authorization, API design, and maintainable code.

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

### Sprint 3 – Roles & Authorization

- Role-based access control.
- User roles: ADMIN, TEAM_LEADER, and USER.
- Default USER role assignment during registration.
- Spring Security authorities integration.
- JWT role claim integration.
- Method-level security using `@PreAuthorize`.
- Admin-only user management endpoints.
- Retrieve all users with pagination.
- Retrieve user details by ID.
- Change user roles.
- Enable and disable user accounts.
- Token invalidation after role and account status changes.
- Protection against disabling or demoting the last active administrator.
- Account reactivation during the configured grace period.
- Configurable account deletion grace period.
- Admin account deletion settings endpoints.

**Note:** Resource-level authorization for teams and tasks is deferred until the Team Management and Task Management modules are implemented.

## Technology Stack

| Technology | Purpose |
|---|---|
| Java 21 | Programming Language |
| Spring Boot 4.1.2 | Backend Framework |
| Spring Web MVC | REST API Development |
| Spring Data JPA | Data Access |
| Hibernate | ORM |
| Spring Security | Authentication & Authorization |
| Spring Security OAuth2 Resource Server | JWT Validation |
| Spring Security JOSE | JWT Encoding and Decoding |
| BCrypt | Password Hashing |
| PostgreSQL | Relational Database |
| Swagger / OpenAPI | API Documentation |
| Maven | Dependency Management |
| Lombok | Boilerplate Reduction |
| JUnit 5 | Testing |
| Mockito | Mocking and Testing |

## Security

TaskVerse uses Spring Security to protect its REST APIs.

Authentication and authorization are based on JWT and role-based access control.

Security mechanisms include:

- Passwords are stored using BCrypt hashing.
- Users authenticate using their email and password.
- Authenticated requests use Bearer JWT tokens.
- JWT signatures and expiration are validated.
- User account status is checked during token validation.
- Token versioning allows previously issued tokens to be invalidated.
- Password changes invalidate existing tokens.
- Disabled accounts cannot use previously issued tokens.
- Role-based authorities control access to protected endpoints.
- Administrative operations are restricted to users with the ADMIN role.
- Role and account status changes invalidate existing tokens.

### User Roles

| Role | Description |
|---|---|
| ADMIN | Manages system-wide user administration and settings. |
| TEAM_LEADER | Intended to manage team-related operations. |
| USER | Standard registered user. |

New registrations are assigned the USER role by default. Users cannot choose their own role during registration.

## API Endpoints

### Authentication

| Method | Endpoint | Description | Access |
|---|---|---|---|
| POST | `/api/v1/auth/register` | Register a new user | Public |
| POST | `/api/v1/auth/login` | Authenticate and receive a JWT | Public |
| POST | `/api/v1/auth/reactivate` | Reactivate an eligible account | Public |

### User Management

| Method | Endpoint | Description | Access |
|---|---|---|---|
| GET | `/api/v1/users/me` | Get current user profile | Authenticated |
| PUT | `/api/v1/users/me` | Update current user profile | Authenticated |
| PATCH | `/api/v1/users/me/password` | Change current user password | Authenticated |
| DELETE | `/api/v1/users/me` | Deactivate current user account | Authenticated |
| GET | `/api/v1/users` | Retrieve all users with pagination | ADMIN |
| GET | `/api/v1/users/{id}` | Retrieve user details | ADMIN |
| PATCH | `/api/v1/users/{id}/role` | Change a user's role | ADMIN |
| PATCH | `/api/v1/users/{id}/status` | Enable or disable a user account | ADMIN |

### Admin Settings

| Method | Endpoint | Description | Access |
|---|---|---|---|
| GET | `/api/v1/admin/settings/account-deletion` | Retrieve account deletion settings | ADMIN |
| PATCH | `/api/v1/admin/settings/account-deletion` | Update account deletion grace period | ADMIN |

All protected endpoints require a valid Bearer JWT token and the appropriate authorization.

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

Database connection settings are configured through environment variables where applicable.

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

**4. Configure environment variables**

Set the required environment variables:

```properties
DB_PASSWORD=YOUR_PASSWORD
JWT_SECRET=YOUR_BASE64_ENCODED_SECRET
```

Configure the application properties:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/taskverse_db
spring.datasource.username=postgres
spring.datasource.password=${DB_PASSWORD}

jwt.secret=${JWT_SECRET}
jwt.expiration=600000
```

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

Automated testing is part of the project development process.

Testing for Sprint 2 (Authentication & JWT) and Sprint 3 (Roles & Authorization) is pending.

The planned testing scope includes:

- Unit testing using JUnit 5 and Mockito.
- Repository testing.
- Controller testing.
- Authentication and JWT validation testing.
- Role-based authorization testing.
- Account management testing.
- Integration testing.

Run the test suite using:

```bash
mvn test
```

## Project Roadmap

| Sprint | Module | Status |
|---|---|---|
| Sprint 0 | Project Setup & Foundation | Completed |
| Sprint 1 | User Management | Completed |
| Sprint 2 | Authentication & JWT | Implementation Completed |
| Sprint 3 | Roles & Authorization | Implementation In Progress |
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
