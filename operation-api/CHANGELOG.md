# Changelog

Changelog for get operation results

## [1.1.0] - 2025-04-12
### Added
- Circuit Breaker integration using Resilience4j to protect the external service.
- Improvements for Retry mechanism with exponential backoff for external API calls.
- Actuator endpoints exposed for monitoring circuit breaker and retry metrics.

## [1.0.0] - 2025-04-11
### Added
- `POST /api/1/operation/sum`: Sum two numbers and apply a dynamic percentage.
- External percentage service integration using `WebClient`.
- Caching of percentage value using Redis with configurable TTL.
- Retry mechanism with exponential backoff for external API calls.
- Event logging via Kafka producer for both request and response payloads.
- Custom filter to log every API interaction asynchronously.
- Global exception handling for validation, runtime, and service-specific errors.
- Health check endpoint (`/api/1/operation/health`).
- JWT authentication with custom filter and entry point.
- Actuator endpoints exposed for monitoring circuit breaker and retry metrics.