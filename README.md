# Foreign Exchange Service

A Spring Boot 3 (Java 17) application that provides normalized FX rates APIs by integrating with the [OANDA public exchange rates API](https://fxds-public-exchange-rates-api.oanda.com).

Features include:
- Currency master data management (CRUD via REST)
- FX rates retrieval and normalization from OANDA
- Scheduled synchronization of exchange rates
- OpenAPI/Swagger documentation
- Dockerized for easy deployment

## Tech Stack

- **Java 17**, **Spring Boot 3**
- **Maven** (multi-module: `api-spec`, `impl`)
- **Spring Data JPA** + **H2 Database**
- **Springdoc OpenAPI** for API docs
- **MapStruct** for mapping
- **JUnit 5 + Mockito** for testing
- **Docker** for containerization

## Project Structure

```
foreign-exchange-service/
├── api-spec        # OpenAPI spec, generated DTOs/interfaces
├── impl            # Main service implementation
│   ├── client      # Web client for external services
│   ├── config      # Application config
│   ├── controller  # REST controllers
│   ├── service     # Business logic
│   ├── repository  # Spring Data JPA repositories
│   ├── entity      # JPA entities
│   ├── mapper      # MapStruct mappers
│   ├── scheduler   # Cron job for FX sync
│   └── resources   # application.properties, schema.sql, data.sql
└── Dockerfile
```
## Running Locally

### Prerequisites
- JDK 17
- Maven 3.9+

### Build & Run
```bash
./mvnw clean package
cd impl
mvn spring-boot:run
```

App will be available at:

```
http://localhost:8080/api
```

## Running with Docker

### Build the image
```bash
docker build -t fx-service .
```

### Run the container
```bash
docker run --rm -p 8080:8080 fx-service
```

## Configuration

Default `application.properties`:

```properties
server.servlet.context-path=/api

spring.datasource.url=jdbc:h2:mem:fxdb
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=password
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect

spring.jpa.hibernate.ddl-auto=none
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console
spring.h2.console.settings.web-allow-others=true

oanda.base-url=https://fxds-public-exchange-rates-api.oanda.com

fxsync.cron=*/30 * * * * *
fxsync.zone=UTC
fxsync.lookback-days=3
fxsync.quote=USD
fxsync.exclude-bases=USD

springdoc.api-docs.path=/api-docs
```

## API Documentation

- Swagger UI → [http://localhost:8080/api/swagger-ui.html](http://localhost:8080/api/swagger-ui.html)
- OpenAPI JSON → [http://localhost:8080/api/api-docs](http://localhost:8080/api/api-docs)

## Scheduler

The scheduled job `FxSyncJob`:
- Runs on cron (`fxsync.cron`)
- Fetches rates for all currencies in DB (excluding `fxsync.exclude-bases`)
- Calls OANDA API
- Saves daily rates into `fx_daily_rates` table
- Skips duplicates by `(base, quote, rate_date)`

## Database

- Default: **H2 in-memory** (`jdbc:h2:mem:fxdb`)
- Console: [http://localhost:8080/api/h2-console](http://localhost:8080/api/h2-console)

## Tests

Run unit tests:
```bash
./mvnw test
```

Covers:
- Controllers
- Services
- Scheduler
- Exception handling

## Notes

- Not production-ready.
- H2 console & `web-allow-others` are enabled for Docker testing only.