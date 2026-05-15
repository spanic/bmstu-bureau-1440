/* Initial data seeding */

INSERT INTO books (id, title, author, isbn, available) VALUES
    (1, 'The Master and Margarita', 'Mikhail Bulgakov', '978-0-14-118014-1', TRUE),
    (2, 'War and Peace', 'Leo Tolstoy', '978-0-19-923276-5', FALSE),
    (3, 'Crime and Punishment', 'Fyodor Dostoevsky', '978-0-14-044913-6', TRUE),
    (4, 'Anna Karenina', 'Leo Tolstoy', '978-0-14-303500-8', TRUE)
ON CONFLICT (id) DO NOTHING;

INSERT INTO operations (id, book_id, type, performed_at, borrower_name) VALUES
    (1, 2, 'WITHDRAW', NOW() - INTERVAL '3 days', 'Ivan Petrov'),
    (2, 3, 'WITHDRAW', NOW() - INTERVAL '7 days', 'Maria Sidorova'),
    (3, 3, 'RETURN',   NOW() - INTERVAL '1 day', 'Maria Sidorova')
ON CONFLICT (id) DO NOTHING;

SELECT setval('books_id_seq', (SELECT COALESCE(MAX(id), 0) FROM books));
SELECT setval('operations_id_seq', (SELECT COALESCE(MAX(id), 0) FROM operations));
