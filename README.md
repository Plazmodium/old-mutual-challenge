# Old Mutual Code Challenge Monorepo

This repository contains both the backend and frontend for the Old Mutual Code Challenge.

## Architecture Overview

```mermaid
graph TD
    subgraph Client_Layer [Client Layer]
        A[Browser]
    end

    subgraph Frontend_App [Frontend: Angular]
        B[Components]
        C[Services]
        D[Models/DTOs]
        B --> C
        C --> D
    end

    subgraph Backend_App [Backend: Spring Boot]
        E[Controllers]
        F[Services]
        G[Clients - RestClient/HTTP Interface]
        H[Resilience4j - Circuit Breaker/Retry]
        E --> F
        F --> G
        G -.-> H
    end

    subgraph External_API [External Data Source]
        I[Rest Countries API]
    end

    A <-->|HTTP/REST| E
    A <-->|Serves UI| Frontend_App
    G <-->|HTTP/REST| I

    style Frontend_App fill:#f9f,stroke:#333,stroke-width:2px
    style Backend_App fill:#bbf,stroke:#333,stroke-width:2px
    style External_API fill:#dfd,stroke:#333,stroke-width:2px
```

## Project Structure
- `backend/`: Spring Boot application (Java 17, Gradle)
- `frontend/`: Angular application (Node.js 20, Angular 21)

## Prerequisites
- **Java 17** or higher
- **Node.js 20** or higher
- **NPM** (usually comes with Node.js)

## Getting Started

### Backend
To run the backend application:
```bash
cd backend
./gradlew bootRun
```
The backend will be available at `http://localhost:8080`.

### Frontend
To run the frontend application:
```bash
cd frontend
npm install
npm start
```
The frontend will be available at `http://localhost:4200`.

## Running Tests

### Backend Tests
```bash
cd backend
./gradlew test
```

### Frontend Tests
```bash
cd frontend
npm test
```

## CI/CD
The project uses GitHub Actions for continuous integration. The configuration can be found in `.github/workflows/ci-cd.yml`.
