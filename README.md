
# TaskVerse

### Smart Team Collaboration & Task Management Platform

TaskVerse is a RESTful API built with Java and Spring Boot, designed to simplify team collaboration, task management, and workflow tracking through a centralized platform.

## Overview

TaskVerse aims to provide an organized environment where teams can collaborate, assign responsibilities, manage tasks, and track progress efficiently.

The project focuses on building a secure, maintainable, and well-structured backend application using modern Java and Spring technologies.

## Features

- User registration and profile management.
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
- Spring Security & JWT
- Spring Data JPA & Hibernate
- PostgreSQL
- Swagger / OpenAPI
- Maven
- Lombok

## Database

TaskVerse uses PostgreSQL as its relational database.

The development database name is:

`taskverse_db`

## API Documentation

API documentation and testing are provided through Swagger UI.

Once the application is running, access:

[Swagger UI](http://localhost:8080/swagger-ui/index.html)

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

Update `src/main/resources/application.properties` with your local database credentials:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/taskverse_db
spring.datasource.username=postgres
spring.datasource.password=YOUR_PASSWORD
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

| Sprint | Module |
|---|---|
| Sprint 0 | Project Setup & Foundation |
| Sprint 1 | User Management |
| Sprint 2 | Authentication & JWT |
| Sprint 3 | Roles & Authorization |
| Sprint 4 | Team Management |
| Sprint 5 | Task Management & Workflow |
| Sprint 6 | Notifications |
| Sprint 7 | Document Management |
| Sprint 8 | Activity Logs & AOP |
| Sprint 9 | Reminders & Scheduling |
| Sprint 10 | Admin APIs & Statistics |
| Sprint 11 | Testing & Final Delivery |

## Author

**Mohamed Galhoum**

[GitHub](https://github.com/Mahamedgalhoum)

---

*TaskVerse is currently under development.*
