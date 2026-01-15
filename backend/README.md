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
