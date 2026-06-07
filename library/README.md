# Library management application

A Spring Boot application for library management using Spring Data JDBC and PostgreSQL.

## Tech Stack

| Component       | Technology                 |
| --------------- | -------------------------- |
| Framework       | Spring Boot 4.0.6          |
| Data access     | Spring Data JDBC           |
| Database        | PostgreSQL 18              |
| Connection pool | HikariCP (auto-configured) |
| Code generation | Lombok 1.18.42             |
| Testing         | JUnit 5, Testcontainers    |
| Build           | Maven                      |

## Prerequisites

- Java 21+
- Maven 3.9+
- Docker (for PostgreSQL and tests)

## Setup

### 1. Start PostgreSQL

From the `library/` directory:

```bash
docker compose up -d
```

This starts a PostgreSQL 18 container on `localhost:5432` with database `library`, user `library`, password `library`.

### 2. Build

From the project root:

```bash
mvn compile -pl library
```

### 3. Run

```bash
mvn spring-boot:run -pl library
```

On first startup, Spring Boot automatically:
- Creates tables via `schema.sql`
- Populates seed data via `data.sql`

### 4. Build and run the executable jar

Package the application into a self-contained executable jar. The `spring-boot-maven-plugin`'s `repackage` goal builds a proper Spring Boot fat jar (nested dependencies, merged `spring.factories`, auto-detected main class):

```bash
mvn clean package -pl library
```

The executable archive is produced at `library/target/library-1.0-SNAPSHOT.jar`.
To run it use the following command:

```bash
java -jar library/target/library-*.jar
```

Need logs from the jar occasionally? Just opt in per-run: 

```bash
java -jar library-*.jar --spring.profiles.active=dev # (or --logging.level.root=INFO).
```

## Tests

Tests use [Testcontainers](https://java.testcontainers.org/) to spin up a disposable PostgreSQL container automatically – no manual database setup needed, just Docker running.

```bash
mvn test -pl library
```

## Stopping the Database

```bash
docker compose down # or also add -v flag to delete saved data
```
