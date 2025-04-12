# Changelog

## [1.1.0] - 2025-04-12
### Added
- Add proxy for actuator api

## [1.0.0] - 2025-04-11
### Added
- Reverse proxy configuration for all microservices:
  - `/api/1/auth/` → `auth-api`
  - `/api/1/operation/` → `operation-api`
  - `/api/1/history/` → `history-api`
- Rate limiting rules using `limit_req_zone` and `limit_req` for each endpoint.
  - Limit set to 3 requests per minute with burst control.
- Custom response for rate-limited requests (`429 Too Many Requests`) with JSON error payload.
- Mock endpoint for external percentage service:
  - `GET /api/1/external/percentage` returns static JSON `{ "percentage": 10.5 }`
  - Supports CORS headers.
- Exposes port 80 for local access via `http://localhost`.