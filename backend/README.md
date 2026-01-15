# Old Mutual Code Challenge - Backend

This is the backend service for the Old Mutual Code Challenge. It is a Spring Boot application that provides an API to retrieve country information by integrating with the REST Countries API.

## Tools and Technologies

- **Java 17**: The primary programming language.
- **Spring Boot 3.4.1**: Framework for building the RESTful API.
- **Gradle**: Build automation and dependency management.
- **Resilience4j**: For implementing the Circuit Breaker pattern to handle external API failures gracefully.
- **Spring Retry**: To automatically retry failed external API calls.
- **Spring Boot Actuator**: For monitoring and managing the application (health, metrics, etc.).
- **Lombok**: To reduce boilerplate code (getters, setters, constructors, etc.).
- **MapStruct**: For efficient mapping between Models and DTOs.
- **SpringDoc OpenAPI (Swagger UI)**: For API documentation and testing.
- **Docker Compose**: For containerized development and deployment.
- **JUnit 5 & Mockito**: For unit and integration testing.

## Project Structure

The project follows a standard Spring Boot layered architecture:

- `controllers`: REST endpoints for handling client requests.
- `services`: Business logic and orchestration between controllers and data sources/external APIs.
- `models`: Core domain entities and external API client interfaces.
- `dtos`: Data Transfer Objects for API requests and responses.
- `config`: Configuration classes (e.g., CORS, Resilience, API Client).
- `exceptions`: Global exception handling and custom exception classes.

### Key Features and Components

- **Resilience**: Implements `Circuit Breaker` and `Retry` patterns in `CountryService` to ensure high availability when communicating with the REST Countries API.
- **Pagination and Filtering**: The `/countries` endpoint supports Spring Data pagination and filtering by region.
- **Observability**:
  - **Logging**: A custom `LoggingFilter` adds a `correlationId` to every request for better traceability.
  - **Actuator**: Exposes health and metrics endpoints at `/actuator`.
- **Declarative HTTP Client**: Uses `ICountryApiClient` (Spring's `HttpServiceProxyFactory`) to interact with `https://restcountries.com/v3.1`.
- **API Documentation**: Automated documentation via Swagger UI.

## API Endpoints

- `GET /countries`: Retrieves a paginated list of countries. Supports query parameters `page`, `size`, `sort`, and `region`.
- `GET /countries/all`: Retrieves all countries in a single list (no pagination).
- `GET /countries/{name}`: Retrieves details for a specific country by name.

## Architecture Layout

1. **Client Request**: The client hits an endpoint in `CountryController`. A `LoggingFilter` assigns a unique `correlationId` to the request.
2. **Service Layer**: `CountryController` calls `CountryService`.
3. **Resilience**: `CountryService` attempts to fetch data. If the external API is down or slow, `Spring Retry` will attempt retries, and the `Circuit Breaker` will trip if failures persist.
4. **External API**: `CountryService` uses `ICountryApiClient` to fetch data from the REST Countries API.
5. **Mapping**: Data is mapped between internal models and DTOs as needed using `CountryMapper` (MapStruct).
6. **Response**: The processed data is returned to the client as JSON.

## Architecture and Systems Thinking

In a production-ready ecosystem, this service would not operate in isolation. Below is an annotation of how it integrates into a broader enterprise architecture:

### 1. Authentication & Authorization (Auth)
- **Current State**: The service is currently open.
- **Ecosystem Integration**: 
  - **Identity Provider (IdP)**: Integration with an IdP like Keycloak or Okta using OIDC/OAuth2.
  - **JWT Validation**: The service would act as a Resource Server, validating JWTs issued by the IdP.
  - **RBAC**: Implementing Role-Based Access Control to restrict access to certain endpoints (e.g., only `ADMIN` could refresh caches or access detailed actuator metrics).

### 2. API Gateway
- **Current State**: Direct access to the service.
- **Ecosystem Integration**:
  - **Entry Point**: All client requests (Frontend) would pass through a Gateway (e.g., Spring Cloud Gateway, Kong, or NGINX).
  - **Cross-Cutting Concerns**: The Gateway would handle SSL termination, rate limiting, and request routing.
  - **Security**: The Gateway would perform initial token validation before forwarding requests to this microservice.

### 3. Observability Stack
- **Current State**: Basic logging with `correlationId` and Spring Boot Actuator.
- **Ecosystem Integration**:
  - **Metrics**: Actuator metrics (Prometheus format) would be scraped by **Prometheus** and visualized in **Grafana** dashboards.
  - **Distributed Tracing**: Integration with **Spring Cloud Sleuth/Micrometer Tracing** and **Zipkin/Jaeger**. The `correlationId` would be part of a larger `traceId` to track requests across multiple services (Gateway -> Country Service -> External API).
  - **Log Aggregation**: Logs (in JSON format) would be shipped to an **ELK Stack** (Elasticsearch, Logstash, Kibana) or **Grafana Loki** for centralized searching and alerting.

### 4. Resilience & External API Integration
- **Current State**: Local Resilience4j and Spring Retry.
- **Ecosystem Integration**:
  - **Global Circuit Breakers**: Monitoring circuit breaker states via a dashboard (e.g., Resilience4j Dashboard or Grafana).
  - **Caching**: Implementing a distributed cache (e.g., **Redis**) to store external API responses, reducing latency and external dependency pressure.

## Getting Started

### Prerequisites

- JDK 17 or higher
- Gradle (optional, wrapper is provided)

### Build and Run

To build the project:

```bash
./gradlew build
```

To run the application:

```bash
./gradlew bootRun
```

Or using Docker Compose:

```bash
docker compose up
```

The server will start at `http://localhost:8080`.

## API Documentation

Once the application is running, you can access the Swagger UI documentation at:

[http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

The OpenAPI JSON definition is available at:

[http://localhost:8080/api-docs](http://localhost:8080/api-docs)

Health and metrics can be monitored via Actuator:

[http://localhost:8080/actuator/health](http://localhost:8080/actuator/health)

## Testing

To run the tests:

```bash
./gradlew test
```
