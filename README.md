# 📝 Blog Platform Application

This project guides you through building a modern, full-featured blog platform using **Spring Boot** for the backend and leveraging key technologies like **PostgreSQL**, **Docker**, and **JWT-based Security**.

The backend provides all essential features for content management, including user authentication, post creation with drafts, and flexible content organization using categories and tags. A pre-built **React frontend** is provided to interact with the API endpoints.

---

## 🚀 Getting Started

To follow along and run the application, you'll need a foundational knowledge of Java, Maven, Docker, and PostgreSQL.

### Prerequisites

Ensure you have the following technologies installed:

| Technology | Minimum Version | Purpose | Check Command |
| :--- | :--- | :--- | :--- |
| **Java Development Kit (JDK)** | 21+ | Primary development language. | `java -version` |
| **Node.js** | 20+ | Required to run the accompanying React frontend. | `node -version` |
| **Docker** | Latest | Used to run the PostgreSQL database in a container. | `docker version` |
| **IDE** | N/A | An Integrated Development Environment (IntelliJ IDEA Community is recommended). | N/A |

### Project Setup

1.  **Generate the Spring Boot Project**
    The project was initialized using the [Spring Initializr](https://start.spring.io) with the following settings:
    * **Project**: Maven
    * **Language**: Java
    * **Spring Boot**: 3.4.0
    * **Java**: 21
    * **Dependencies**: Spring Web, Spring Data JPA, PostgreSQL Driver, Spring Security, Lombok, Validation, and H2.

2.  **Database Setup with Docker Compose**
    The PostgreSQL database is run via Docker Compose, which simplifies setup and ensures a consistent environment.

    Create a `docker-compose.yml` file in the project root:

    ```yaml
    services:
      # Our PostgreSQL database
      db:
        image: postgres:latest
        ports:
          - "5432:5432"
        restart: always
        environment:
          POSTGRES_PASSWORD: changemeinprod!
      # Database management interface (Adminer is optional)
      adminer:
        image: adminer:latest
        restart: always
        ports:
          - "8888:8080"
    ```

    Start the database and Adminer interface:
    ```bash
    docker-compose up
    ```
    The **Adminer** interface is available at `http://localhost:8888`.

3.  **Application Configuration**
    Configure the Spring Boot application to connect to the PostgreSQL database in `src/main/resources/application.properties`:

    ```properties
    # Database Connection
    spring.datasource.url=jdbc:postgresql://localhost:5432/postgres
    spring.datasource.username=postgres
    spring.datasource.password=changemeinprod!

    # JPA Configuration
    spring.jpa.hibernate.ddl-auto=update
    spring.jpa.show-sql=true
    spring.jpa.properties.hibernate.format_sql=true
    spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
    ```

4.  **Run the Backend**
    Run the `BlogApplication.java` entry point to start the Spring Boot application.

5.  **Run the Frontend**
    Navigate to the directory containing the provided React frontend code and run:

    ```bash
    # Install dependencies
    npm install

    # Run the application
    npm run dev
    ```
    The frontend will be accessible at `http://localhost:5173/`.

---

## 🔑 Authentication

The application uses **JWT (JSON Web Tokens)** for stateless authentication. A default user is automatically created on startup for easy testing.

| Detail | Value |
| :--- | :--- |
| **Login Endpoint** | `POST /api/v1/auth/login` |
| **Default Email** | `user@test.com` |
| **Default Password** | `password` |

After a successful login, the API returns a JWT token. This token must be included in the `Authorization` header of all protected requests in the format `Bearer <token>`.

---

## 💻 API Endpoints

### Categories

| HTTP Method | Path | Description | Access |
| :--- | :--- | :--- | :--- |
| `GET` | `/api/v1/categories` | List all categories with their published post counts. | Public |
| `POST` | `/api/v1/categories` | Create a new category, preventing duplicates by name. | Protected |
| `DELETE` | `/api/v1/categories/{id}` | Delete a category (only if it has no associated posts). | Protected |

### Tags

| HTTP Method | Path | Description | Access |
| :--- | :--- | :--- | :--- |
| `GET` | `/api/v1/tags` | List all tags with their published post counts. | Public |
| `POST` | `/api/v1/tags` | Create one or more new tags, safely handling existing tag names. | Protected |
| `DELETE` | `/api/v1/tags/{id}` | Delete a tag (only if it has no associated posts). | Protected |

### Posts

Posts support **DRAFT** and **PUBLISHED** statuses.

| HTTP Method | Path | Description | Access |
| :--- | :--- | :--- | :--- |
| `GET` | `/api/v1/posts` | List all **PUBLISHED** posts with optional filtering by `categoryId` and `tagId`. | Public |
| `GET` | `/api/v1/posts/drafts` | List **DRAFT** posts belonging to the authenticated user. | Protected |
| `GET` | `/api/v1/posts/{id}` | Retrieve a single post by ID. | Public |
| `POST` | `/api/v1/posts` | Create a new post (draft or published). | Protected |
| `PUT` | `/api/v1/posts/{id}` | Update an existing post. | Protected |
| `DELETE` | `/api/v1/posts/{id}` | Delete a post. | Protected |

---

## 🛠 Project Structure Highlights

The project follows a standard Spring Boot architecture using several key components:

* **Domain Entities:** JPA `@Entity` classes (`User`, `Post`, `Category`, `Tag`) model the database schema.
* **Repositories:** Spring Data JPA `JpaRepository` interfaces abstract data access for simplified CRUD operations.
* **Services:** The layer for all business logic and transaction management, e.g., preventing category deletion if posts are associated.
* **Controllers:** REST API endpoints using `@RestController`.
* **DTOs (Data Transfer Objects):** Used to define the exact format for API requests (`CreateCategoryRequest`, `LoginRequest`) and responses (`CategoryDto`, `AuthResponse`), separating the API contract from the internal domain model.
* **MapStruct:** A code generator to efficiently convert between domain entities and DTOs at compile time, eliminating reflection overhead.
* **Error Handling:** A centralized `@ControllerAdvice` handles exceptions (`IllegalArgumentException`, `IllegalStateException`, `EntityNotFoundException`) to provide consistent API error responses.

---

## 📈 Next Steps for Improvement

While our platform has the essential features of a blog, here are key steps to move it toward production readiness:

### Security Enhancements

* **Implement Refresh Tokens:** Use refresh tokens to improve user session handling, allowing the primary **Access Token** to be short-lived (a security best practice) while maintaining a seamless user experience (UX).
* **Add CSRF Protection:** Although using a Bearer token in the `Authorization` header offers a basic defense, adding explicit **CSRF protection** (like the Synchronizer Token Pattern) is a best practice, especially if the tokens were to be stored in cookies, which are automatically sent by the browser on cross-site requests.

### Performance & Stability

* **Optimize JPA Queries:** Focus on optimizing database queries to resolve potential **N+1 query issues** that arise when accessing lazy-loaded collections (like posts on a category or tags on a post). This can be achieved using `JOIN FETCH` clauses or `@BatchSize` annotations.
* **Refine Reading Time Calculation:** The current implementation relies on simple content length. A more accurate calculation should consider the average adult reading speed (**~200-275 words per minute**) and potentially add extra time to account for images or other media.
* **Comprehensive Testing:** Prioritize adding a robust suite of **unit and integration tests** to ensure stability and prevent regressions as new features are added.

### User Experience

* **Enhanced Error Messages:** Refine the current error messages to be more specific and user-friendly, guiding the user on how to correct the issue rather than just reporting the error.

---
