DELETE FROM books;
INSERT INTO books (id, title, author, isbn, price, description, cover_image, is_deleted)
VALUES
    (1, 'The Great Adventure', 'John Doe', '978-3-16-148410-0', 19.99, 'An amazing journey', 'https://example.com/image1.jpg', false),
    (2, 'Mystery of the Night', 'Jane Smith', '978-1-23-456789-7', 25.50, 'A thrilling mystery', 'https://example.com/image2.jpg', false),
    (3, 'Programming in Java', 'Alice Johnson', '978-0-12-345678-9', 35.00, 'Learn Java from scratch', 'https://example.com/image3.jpg', false);
