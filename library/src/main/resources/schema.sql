CREATE TYPE book_genre AS ENUM (
    'ADVENTURE', 'CRIME', 'HORROR', 'MYSTERY', 'FANTASY',
    'SCIENCE_FICTION', 'NOVEL', 'POETRY', 'BIOGRAPHY', 'HISTORY',
    'SCIENCE', 'TECHNOLOGY', 'ART', 'OTHER'
);

CREATE TABLE IF NOT EXISTS books (
    id        BIGSERIAL     PRIMARY KEY,
    title     VARCHAR(255)  NOT NULL,
    author    VARCHAR(255)  NOT NULL,
    genre     book_genre    NOT NULL,
    available BOOLEAN       NOT NULL DEFAULT TRUE
);

CREATE TABLE IF NOT EXISTS operations (
    id            BIGSERIAL    PRIMARY KEY,
    book_id       BIGINT       NOT NULL REFERENCES books(id),
    type          VARCHAR(20)  NOT NULL,
    performed_at  TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    borrower_name VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS clients (
    id          BIGSERIAL    PRIMARY KEY,
    name        VARCHAR(255) NOT NULL,
    email       VARCHAR(255),
    created_at  TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP
);