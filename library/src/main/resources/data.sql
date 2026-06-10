/* Initial data seeding */

INSERT INTO books (id, title, author, genre, available) VALUES
    (1, 'The Master and Margarita', 'Mikhail Bulgakov', 'FANTASY', TRUE),
    (2, 'War and Peace', 'Leo Tolstoy', 'NOVEL', FALSE),
    (3, 'Crime and Punishment', 'Fyodor Dostoevsky', 'CRIME', TRUE),
    (4, 'Anna Karenina', 'Leo Tolstoy', 'NOVEL', TRUE)
ON CONFLICT (id) DO NOTHING;

INSERT INTO operations (id, book_id, type, performed_at, borrower_name) VALUES
    (1, 2, 'WITHDRAW', NOW() - INTERVAL '3 days', 'Ivan Petrov'),
    (2, 3, 'WITHDRAW', NOW() - INTERVAL '7 days', 'Maria Sidorova'),
    (3, 3, 'RETURN',   NOW() - INTERVAL '1 day', 'Maria Sidorova')
ON CONFLICT (id) DO NOTHING;

SELECT setval('books_id_seq', (SELECT COALESCE(MAX(id), 0) FROM books));
SELECT setval('operations_id_seq', (SELECT COALESCE(MAX(id), 0) FROM operations));
