# Changelog

Changelog for get events api

## [1.0.0] - 2025-04-11
### Added
- Kafka consumer to receive and process API log events from other services.
- Persistence of log events in PostgreSQL using JPA.
- Paginated retrieval of logs via `/api/1/history/logs`.
- Health check endpoint (`/api/1/history/health`) for readiness.
- Centralized error handling with `@RestControllerAdvice`.
- JWT token validation using custom filter and `JwtUtil`.
- Custom authentication entry point for unauthorized access handling.
- Actuator endpoints enabled and exposed for monitoring.