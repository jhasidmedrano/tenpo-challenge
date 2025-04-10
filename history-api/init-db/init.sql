CREATE TABLE IF NOT EXISTS api_call_logs (
    id BIGSERIAL PRIMARY KEY,
    service VARCHAR(100) NOT NULL,
    method VARCHAR(10) NOT NULL,
    endpoint VARCHAR(255) NOT NULL,
    request TEXT,
    response TEXT,
    status INTEGER,
    timestamp TIMESTAMP NOT NULL
);
