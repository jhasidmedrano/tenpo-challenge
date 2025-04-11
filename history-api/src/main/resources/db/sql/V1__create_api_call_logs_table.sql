CREATE TABLE IF NOT EXISTS api_call_logs (
    id BIGSERIAL PRIMARY KEY,
    service VARCHAR(255) NOT NULL,
    method VARCHAR(20) NOT NULL,
    endpoint TEXT NOT NULL,
    request TEXT,
    response TEXT,
    status INTEGER NOT NULL,
    timestamp TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
    );
