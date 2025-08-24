CREATE TABLE currencies
(
    code      VARCHAR(3) PRIMARY KEY,
    name      VARCHAR(255) NOT NULL,
    created_at TIMESTAMP    NOT NULL,
    updated_at TIMESTAMP    NOT NULL
);
