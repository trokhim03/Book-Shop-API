package mate.academy.bookshop.repository;

import mate.academy.bookshop.model.Book;
import java.util.List;

public interface BookRepository {
    Book save(Book book);
    List findAll();
}
