DELETE FROM categories;
INSERT INTO categories (id, name, description, is_deleted) VALUES
                                                               (1, 'Non-Fiction', 'Books based on facts and real events', false),
                                                               (2, 'Science Fiction', 'Books with futuristic and scientific themes', false),
                                                               (3, 'Mystery', 'Books involving suspense, crime, or detective stories', false);
