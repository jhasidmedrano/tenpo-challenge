CREATE TABLE users (
                       id SERIAL PRIMARY KEY,
                       username VARCHAR(100) NOT NULL UNIQUE,
                       password VARCHAR(100) NOT NULL,
                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                       updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

INSERT INTO users (username, password)
VALUES
    ('admin', '$2a$10$QfRvJwXujT8KqXhUpDRNwuYOwIVhMvlPzXq9Jrr37o0v89mZzL5Ja'),
    ('tenpo', '$2a$10$lK3RyCzA0N9OEtUoLUvDUez1ZzITty7NYBbZ6uQZmJOVev2FOOEWu'),
    ('tenpologin', '$2a$10$2p5KMo4eG3Teqp30YdZDNuahtsL89ZsUZa0MIiRK1hRIKOjZAGwOS');
