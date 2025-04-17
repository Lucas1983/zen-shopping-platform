# 🛒 Shopping Platform

#### A shopping service that manages products and calculates prices based on configurable discount policies.

---

## 🚀 How to Build, Run, and Test

### Requirements

- Java 21
- Docker
- Gradle (wrapper included in the project: `./gradlew`)

---

### Build the Application

```bash
./gradlew clean build
```

This command:

- Compiles the source code,
- Generates MapStruct mappers,
- Runs all unit and integration tests (including Testcontainers-based tests).

---

### Run the Application

```bash
./gradlew bootRun
```

This command starts the application on port 8080.

### Run the Application with Docker (Buildpacks)

To create a production-ready Docker image and run it:

```bash
./gradlew bootBuildImage
```

This command builds the application and creates a Docker image named `shopping-platform:0.0.1-SNAPSHOT`
The image is built using Spring Boot Buildpacks (no custom Dockerfile needed).

To run the Docker image:

```bash
docker run -p 8080:8080 ala-shopping-platform:0.0.1-SNAPSHOT
```

This command runs the application in a Docker container, exposing it on port 8080.

---

### Run Tests

```bash
./gradlew test
```

Tests include:
• Unit tests
• Integration tests using Testcontainers with a PostgreSQL database.

Docker must be running for integration tests to work properly.

## Testing with Testcontainers

The integration tests use Testcontainers to spin up a PostgreSQL database dynamically.
No manual setup is required — Docker must be installed and running.

---

## 🛠 Technologies Used

- Java 21
- Spring Boot 3.4.4
- MapStruct — DTO mapping
- Testcontainers — PostgreSQL integration tests
- OpenAPI (Swagger UI)
- Gradle — build tool
- Docker Buildpacks — for containerization without Dockerfile
- Lombok — for reducing boilerplate code
- JUnit 5 — for unit and integration testing
- Mockito — for mocking dependencies in tests
- PostgreSQL — for the database

---

### Project Structure

Application structure follows the principles of Clean ( Hexagonal ) Architecture , separating concerns into different
layers:

```plaintext
ala-shopping-platform/
├── src/main/java
│   └── com/zen/ala
│       ├── application/         # Application services
│       ├── domain/              # Business domain model
│       ├── infrastructure/      # Persistence, web API, configuration
├── src/test/java
│   └── com/zen/ala              # Unit and integration tests
├── build.gradle                 # Gradle build file
├── README.md                    # Project documentation
└── Dockerfile (generated via Buildpacks)
```

---

## 📚 API Documentation

The API documentation is available at `/swagger-ui/index.html` after starting the application.

### API Endpoints

- **POST /api/v1/products**: Create a new product.
- **GET /api/v1/products**: Retrieve all products.
- **GET /api/v1/products/{id}**: Retrieve a product by ID.
- **PUT /api/v1/products/{id}**: Update a product by ID.
- **DELETE /api/v1/products/{id}**: Delete a product by ID.
- **GET /api/v1/products/{id}/calculate-price**: Calculate the final price of a product after applying discounts.

