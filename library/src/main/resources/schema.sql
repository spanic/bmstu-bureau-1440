CREATE TYPE book_genre AS ENUM (
    'ADVENTURE', 'CRIME', 'HORROR', 'MYSTERY', 'FANTASY',
    'SCIENCE_FICTION', 'NOVEL', 'POETRY', 'BIOGRAPHY', 'HISTORY',
    'SCIENCE', 'TECHNOLOGY', 'ART', 'OTHER'
);

CREATE TYPE operation_type AS ENUM (
    'WITHDRAW', 'RETURN'
);

CREATE TABLE IF NOT EXISTS books (
    id        BIGSERIAL     PRIMARY KEY,
    title     VARCHAR(255)  NOT NULL,
    author    VARCHAR(255)  NOT NULL,
    genre     book_genre    NOT NULL,
    available BOOLEAN       NOT NULL DEFAULT TRUE
);

CREATE TABLE IF NOT EXISTS clients (
    id          BIGSERIAL    PRIMARY KEY,
    name        VARCHAR(255) NOT NULL,
    email       VARCHAR(255),
    created_at  TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS operations (
    id            BIGSERIAL       PRIMARY KEY,
    book_id       BIGINT          NOT NULL REFERENCES books(id),
    client_id     BIGINT          NOT NULL REFERENCES clients(id),
    type          operation_type  NOT NULL,
    performed_at  TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP
);


CREATE INDEX idx_operations_book_id ON operations (book_id);
CREATE INDEX idx_operations_client_book ON operations (client_id, book_id);


CREATE INDEX idx_operations_withdraw_book_id
    ON operations (book_id)
    WHERE type = 'WITHDRAW';


CREATE INDEX idx_books_available ON books (id) WHERE available = TRUE;