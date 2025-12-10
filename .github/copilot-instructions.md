<!-- Copilot / AI agent guidance for the Infrafix backend project -->
# Infrafix — AI Coding Agent Instructions

Quick context
- Language & framework: Java 21 + Spring Boot (entry: `src/main/java/com/infrafix/citizen_reporting/Infrafix.java`).
- Build system: Maven (`pom.xml`) — uses Spring Boot starter parent and `spring-boot-maven-plugin`.
- DB driver: Microsoft SQL Server at runtime (`mssql-jdbc` in `pom.xml`).

What to prioritize
- Preserve existing REST behavior: controllers in `controller/` rely on services in `service/` and repositories in `repo/`.
- Follow existing DTO pattern: request/response/validation split under `dto/` (examples: `dto/request/*`, `dto/response/*`, `dto/validation/*`).
- Error handling and responses are centralized: see `handler/GlobalExceptionHandler.java` and `handler/ResponseHandler.java`. Use them instead of ad-hoc ResponseEntity shapes.

Architecture & important files (big picture)
- Entry point: `Infrafix.java` — app uses `@EnableAsync` and `@EnableScheduling`.
- Config: `config/JwtConfig.java`, `otherconfig.properties`, `jwt.properties`, and `application.properties` contain runtime settings.
- Security: `security/JwtFilter.java`, `JwtUtility.java`, `SecurityConfig.java`, `BcryptCustom.java` — authentication is JWT-based; when changing auth behavior, update these files.
- Controllers: `controller/*` define endpoints and map closely to service interfaces in `core/` (e.g., `IReportService`, `IUserService`).
- Services: `service/*` implement business logic. Keep controllers thin; prefer adding behavior in service layer.
- Repositories: `repo/*` are Spring Data JPA interfaces (e.g., `UserRepository` uses `findByEmail`, `findByNameContainsIgnoreCase(Pageable, String)`). Mimic these naming conventions when adding queries.
- DTOs & Validation: request DTOs validated via `javax`/Jakarta validation; `GlobalExceptionHandler` converts validation errors into standardized responses.
- Utilities: `util/GlobalResponse.java`, `GlobalFunction.java`, `LoggingFile.java` implement common behaviour — reuse them where applicable.

Conventions & patterns to follow (concrete)
- Response shape: Use `ResponseHandler` to build API responses — controllers and exception handler expect that format.
- Pagination/search: Repositories expose `findBy<Field>ContainsIgnoreCase(Pageable page, String value)` patterns. Keep `Pageable` first argument.
- Service interfaces: There are `I*Service` interfaces in `core/`. New services should expose interfaces and be implemented under `service/`.
- DTO layering: Always separate `request` vs `response` DTOs. Put validation-specific DTOs under `dto/validation/`.
- Exception handling: Throw domain exceptions (or `ConstraintViolationException`) and let `GlobalExceptionHandler` map responses. Log exceptions with `LoggingFile.logException()` and include `RequestCapture.allRequest(request)` when context is needed.

Build, run, and test (commands)
- Run tests: `mvn test` or `mvn -DskipTests=false test`.
- Build artifact: `mvn clean package` (creates jar under `target/`).
- Run app locally (development):
  - `mvn spring-boot:run` (reads `src/main/resources/*.properties`).
  - Or after build: `java -jar target/citizen-reporting-0.0.1-SNAPSHOT.jar`.
- Docker: `docker build -t infrafix-be .` then `docker run -p 8080:8080 infrafix-be` (ensure built jar is included by the `Dockerfile`).

Environment notes
- Java 21 is declared in `pom.xml` — CI/agents must use Java 21 runtime.
- Default DB config is in `src/main/resources/application.properties`; tests may rely on an embedded or test DB — inspect `test/` before changing runtime DB settings.

Integration points & external dependencies
- Mail: `spring-boot-starter-mail` is used for email flows (see `templates/email_verification.html`).
- PDF export: `com.itextpdf:html2pdf` for report downloads (`GET /report/download/{reportId}`).
- JWT: `io.jsonwebtoken` library used across `security/*` files.

Guidance for code changes
- When adding endpoints, add request/response DTOs and validations first, then service interface, then service impl, then controller.
- Use repository method naming patterns (Spring Data) rather than custom JPQL where possible for consistency.
- Maintain centralized response format by leveraging `ResponseHandler` and the same `code` patterns used in `GlobalExceptionHandler` (e.g., "X04000").
- Keep config values in `*.properties` files and avoid hard-coding credentials or URLs.

Examples (copyable references)
- Standard repository search: `Page<User> findByNameContainsIgnoreCase(Pageable page, String value)` — follow for other entities.
- Validation handling: see `GlobalExceptionHandler.handleValidationErrors(...)` — validation errors are converted to a map of field->message and returned with `ResponseHandler`.
- App entry: `SpringApplication.run(Infrafix.class, args)` in `Infrafix.java`.

If you need more
- If anything here is incomplete or you want examples for a specific change (new controller, new service, or running in CI), tell me which area and I'll expand with concrete code snippets or tests.

---
Last updated: generated by AI agent based on repository files. Please review and request edits for any missing project-specific policies.
