CREATE TABLE IF NOT EXISTS users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);


INSERT INTO users (username, password)
VALUES
    ('admin', '$2a$10$QfRvJwXujT8KqXhUpDRNwuYOwIVhMvlPzXq9Jrr37o0v89mZzL5Ja'),
    ('tenpo', '$2a$10$lK3RyCzA0N9OEtUoLUvDUez1ZzITty7NYBbZ6uQZmJOVev2FOOEWu'),
    ('tenpologin', '$2a$10$L9TUtfW2hTV1txkfxeJQuePdrss6TiQKtIV.adypgKD/SRBokTRzC')
    ON CONFLICT (username) DO NOTHING;

