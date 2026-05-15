CREATE TABLE IF NOT EXISTS books (
    id          BIGSERIAL    PRIMARY KEY,
    title       VARCHAR(255) NOT NULL,
    author      VARCHAR(255) NOT NULL,
    isbn        VARCHAR(20)  NOT NULL UNIQUE,
    available   BOOLEAN      NOT NULL DEFAULT TRUE
);

CREATE TABLE IF NOT EXISTS operations (
    id            BIGSERIAL    PRIMARY KEY,
    book_id       BIGINT       NOT NULL REFERENCES books(id),
    type          VARCHAR(20)  NOT NULL,
    performed_at  TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    borrower_name VARCHAR(255) NOT NULL
);
