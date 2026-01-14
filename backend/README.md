# Old Mutual Code Challenge - Backend

This is the backend service for the Old Mutual Code Challenge. It is a Spring Boot application that provides an API to retrieve country information by integrating with the REST Countries API.

## Tools and Technologies

- **Java 17**: The primary programming language.
- **Spring Boot 3.4.1**: Framework for building the RESTful API.
- **Gradle**: Build automation and dependency management.
- **Lombok**: To reduce boilerplate code (getters, setters, constructors, etc.).
- **MapStruct**: For efficient mapping between Models and DTOs.
- **SpringDoc OpenAPI (Swagger UI)**: For API documentation and testing.
- **JUnit 5 & Spring Boot Test**: For unit and integration testing.

## Project Structure

The project follows a standard Spring Boot layered architecture:

- `controllers`: REST endpoints for handling client requests.
- `services`: Business logic and orchestration between controllers and data sources/external APIs.
- `models`: Core domain entities and external API client interfaces.
- `dtos`: Data Transfer Objects for API requests and responses.
- `config`: Configuration classes (e.g., CORS).

### Key Components

- `OldMutualChallengeApplication`: The main entry point and configuration for the `ICountryApiClient`.
- `ICountryApiClient`: A declarative HTTP client (using Spring's `HttpServiceProxyFactory`) to interact with `https://restcountries.com/v3.1`.
- `CountryController`: Exposes endpoints under `/countries`.

## Architecture Layout

1. **Client Request**: The client hits an endpoint in `CountryController`.
2. **Service Layer**: `CountryController` calls `CountryService`.
3. **External API**: `CountryService` uses `ICountryApiClient` to fetch data from the REST Countries API.
4. **Mapping**: Data is mapped between internal models and DTOs as needed using `CountryMapper` (MapStruct).
5. **Response**: The processed data is returned to the client as JSON.

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

The server will start at `http://localhost:8080`.

## API Documentation

Once the application is running, you can access the Swagger UI documentation at:

[http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

The OpenAPI JSON definition is available at:

[http://localhost:8080/api-docs](http://localhost:8080/api-docs)

## Testing

To run the tests:

```bash
./gradlew test
```
