package mate.academy.bookshop.service;

import java.util.List;
import mate.academy.bookshop.model.Book;

public interface BookService {
    Book save(Book book);

    List<Book> findAll();
}
