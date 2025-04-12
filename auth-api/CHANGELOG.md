# Changelog

Changelog for the Authentication microservice.

## [1.0.0] - 2025-04-11
### Added
- JWT authentication with `/login` and `/validate` endpoints.
- Kafka-based event logging for all requests and responses.
- Request/response body sanitization (e.g., masking passwords and tokens).
- Global error handling via `@RestControllerAdvice`.
- Health check endpoint (`/api/1/auth/health`) for readiness.