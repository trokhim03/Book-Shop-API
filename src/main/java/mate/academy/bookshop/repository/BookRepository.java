package mate.academy.bookshop.repository;

import java.util.List;
import mate.academy.bookshop.model.Book;

public interface BookRepository {
    Book save(Book book);

    List<Book> findAll();
}
