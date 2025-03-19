DELETE
FROM books
WHERE title = 'Abetka';
INSERT INTO books (id, title, author, isbn, price, description, cover_image, is_deleted)
VALUES (4, 'Abetka', 'Hryhoriy Falkovych', '978-617-585-236-3', 120.00,
        'A classic Ukrainian alphabet book in verse with bright illustrations for children.',
        'https://example.com/abetka-cover.jpg', false);
