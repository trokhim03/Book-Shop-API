DELETE FROM books_categories WHERE book_id = (SELECT id FROM books WHERE title = 'Abetka');
DELETE FROM books WHERE title = 'Abetka';