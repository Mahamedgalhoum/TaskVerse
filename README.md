
# TaskVerse

### Smart Team Collaboration & Task Management Platform

TaskVerse is a RESTful API built with Java and Spring Boot, designed to simplify team collaboration, task management, and workflow tracking through a centralized platform.

## Overview

TaskVerse aims to provide an organized environment where teams can collaborate, assign responsibilities, manage tasks, and track progress efficiently.

The project focuses on building a secure, maintainable, and well-structured backend application using modern Java and Spring technologies.

## Implemented Features

### Sprint 0 – Project Setup & Foundation
- Spring Boot project initialization.
- PostgreSQL database configuration.
- Maven project setup.
- Swagger / OpenAPI integration.
- Basic health check endpoint.

### Sprint 1 – User Management
- User entity and database design.
- User registration.
- Email uniqueness validation.
- Password encryption using BCrypt.
- Request validation.
- Standard API response structure.
- Global exception handling.
- Custom exceptions.

## Planned Features

- User profile management.
- Authentication and authorization using JWT.
- Role-based access control.
- Team creation and member management.
- Task creation, assignment, and tracking.
- Task status workflow and review process.
- Search, filtering, sorting, and pagination.
- In-app notifications and scheduled reminders.
- Document upload and management.
- Activity logging and monitoring.
- Administrative APIs and statistics.

## Technology Stack

- Java 21
- Spring Boot
- Spring Web MVC
- Spring Data JPA & Hibernate
- Spring Security Crypto (BCrypt)
- PostgreSQL
- Swagger / OpenAPI
- Maven
- Lombok

**Planned Technologies:**
- Spring Security
- JWT Authentication
- Spring AOP

## Database

TaskVerse uses PostgreSQL as its relational database.

The development database name is:

`taskverse_db`

## API Documentation

API documentation and testing are provided through Swagger UI.

Once the application is running, access:

[Swagger UI](http://localhost:8080/swagger-ui/index.html)

## Available Endpoints

| Method | Endpoint | Description | Status |
|---|---|---|---|
| GET | `/api/v1/health` | Check application health | Implemented |
| POST | `/api/v1/auth/register` | Register a new user | Implemented |

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

Set the following environment variables on your machine:

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

**5. Run the application**

Using Maven:

```bash
mvn spring-boot:run
```

Or run `TaskverseApplication.java` from your IDE.

The application will be available at:

`http://localhost:8080`

## Project Roadmap

| Sprint | Module | Status |
|---|---|---|
| Sprint 0 | Project Setup & Foundation | Completed |
| Sprint 1 | User Management | Registration Completed |
| Sprint 2 | Authentication & JWT | Planned |
| Sprint 3 | Roles & Authorization | Planned |
| Sprint 4 | Team Management | Planned |
| Sprint 5 | Task Management & Workflow | Planned |
| Sprint 6 | Notifications | Planned |
| Sprint 7 | Document Management | Planned |
| Sprint 8 | Activity Logs & AOP | Planned |
| Sprint 9 | Reminders & Scheduling | Planned |
| Sprint 10 | Admin APIs & Statistics | Planned |
| Sprint 11 | Testing & Final Delivery | Planned |

## Author

**Mohamed Galhoum**

[GitHub](https://github.com/Mahamedgalhoum)

---

*TaskVerse is currently under development.*
