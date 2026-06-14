/* Initial data seeding */

INSERT INTO books (id, title, author, genre, available) VALUES
    (1, 'The Master and Margarita', 'Mikhail Bulgakov', 'FANTASY', TRUE),
    (2, 'War and Peace', 'Leo Tolstoy', 'NOVEL', FALSE),
    (3, 'Crime and Punishment', 'Fyodor Dostoevsky', 'CRIME', TRUE),
    (4, 'Anna Karenina', 'Leo Tolstoy', 'NOVEL', TRUE)
ON CONFLICT (id) DO NOTHING;

INSERT INTO clients (id, name, email, created_at) VALUES
    (1, 'Иван Петров', 'ivan.petrov@example.com', NOW() - INTERVAL '3 days'),
    (2, 'Мария Сидорова', 'maria.sidorova@example.com', NOW() - INTERVAL '7 days'),
    (3, 'Дженнифер Лопес', 'jennifer.lopez@example.com', NOW() - INTERVAL '1 day')
ON CONFLICT (id) DO NOTHING;

INSERT INTO operations (id, book_id, client_id, type, performed_at) VALUES
    (1, 2, 1, 'WITHDRAW', NOW() - INTERVAL '3 days'),
    (2, 3, 2, 'WITHDRAW', NOW() - INTERVAL '7 days'),
    (3, 3, 2, 'RETURN',   NOW() - INTERVAL '1 day')
ON CONFLICT (id) DO NOTHING;

SELECT setval('books_id_seq', (SELECT COALESCE(MAX(id), 0) FROM books));
SELECT setval('clients_id_seq', (SELECT COALESCE(MAX(id), 0) FROM clients));
SELECT setval('operations_id_seq', (SELECT COALESCE(MAX(id), 0) FROM operations));
