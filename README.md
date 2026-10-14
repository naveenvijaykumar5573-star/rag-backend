# Multi-Tenant AI RAG Platform — Spring Boot Backend

A backend service for a multi-tenant Retrieval-Augmented Generation (RAG) platform built with Spring Boot and integrated with a Python FastAPI AI service. The platform supports secure authentication, tenant-specific document management, and AI-powered question answering over PDF documents.

## &#x20;Features

- **JWT Authentication:** Secure user authentication using JSON Web Tokens.
- **Multi-Tenant Architecture:** Organizes users and documents by tenant.
- **Document Management:** Upload, retrieve, list, and delete PDF documents.
- **AI Integration:** Communicates with a Python FastAPI service for RAG processing.
- **REST APIs:** Exposes endpoints for authentication and document-based question answering.
- **Error Handling:** Consistent HTTP error responses for common failures.
- **PostgreSQL Integration:** Stores application data in a relational database.

## Tech Stack

- Java
- Spring Boot
- Spring Security
- JWT
- Spring Data JPA / Hibernate
- PostgreSQL
- Maven
- Python FastAPI (AI service integration)
- pgvector and sentence-transformers (Python RAG service)

## Architecture

```text
Client
  |
  v
Spring Boot Backend
  |
  |-- Authentication and JWT validation
  |-- Tenant-aware API access
  |-- Document API orchestration
  |
  v
Python FastAPI RAG Service
  |
  |-- PDF processing and chunking
  |-- Embedding generation
  |-- Semantic retrieval
  |-- LLM-based answer generation
  |
  v
PostgreSQL + pgvector
```

## Prerequisites

- Java (compatible with the version configured in `pom.xml`)
- Maven, or use the included Maven Wrapper
- PostgreSQL
- Python RAG service running locally

## Configuration

Configure the following environment variables before starting the backend:

| Variable      | Purpose                                           |
| ------------- | ------------------------------------------------- |
| `DB_USER`     | PostgreSQL username                               |
| `DB_PASSWORD` | PostgreSQL password                               |
| `JWT_SECRET`  | Shared secret used for JWT signing and validation |

The default local configuration expects PostgreSQL at `localhost:5432` and the Python RAG service at `http://127.0.0.1:8000`.

**Security:** Never commit real passwords, JWT secrets, API keys, or production credentials to source control.

## Run the Application

1. Create the PostgreSQL database configured in `application.properties`.
2. Start the Python FastAPI RAG service.
3. Set the required environment variables.
4. From the project root, run:

**Windows:**

```cmd
mvnw.cmd spring-boot:run
```

The backend starts on port `8081` with the current configuration.

## API Endpoints

Base URL: `http://localhost:8081`

| Method   | Endpoint                          | Purpose                        |
| -------- | --------------------------------- | ------------------------------ |
| `POST`   | `/api/auth/register`              | Register a user                |
| `POST`   | `/api/auth/login`                 | Authenticate a user            |
| `POST`   | `/api/rag/ask`                    | Ask a question about documents |
| `GET`    | `/api/rag/documents`              | List accessible documents      |
| `GET`    | `/api/rag/documents/{documentId}` | Retrieve document details      |
| `DELETE` | `/api/rag/documents/{documentId}` | Delete a document              |
| `POST`   | `/api/rag/documents/upload`       | Upload a PDF document          |

Authenticated endpoints require a valid bearer token in the `Authorization` header:

```http
Authorization: Bearer <your_jwt_token>
```

## Security and Tenant Isolation

The backend uses JWT authentication and tenant-associated user records. Authenticated requests are forwarded to the Python RAG service with the authorization header.

Tenant isolation must be enforced and verified across every document operation to prevent users from accessing another tenant's data.

## Future Improvements

- Automated integration and security tests
- API documentation with Swagger/OpenAPI
- Docker-based deployment
- Centralized configuration for development and production
- Logging, monitoring, and performance optimization

## Author

Naveen V

[GitHub Profile](https://github.com/naveenvijaykumar5573-star)
