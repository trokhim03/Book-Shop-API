package mate.academy.bookshop.service;

import mate.academy.bookshop.model.Book;

import java.util.List;

public interface BookService {
    Book save(Book book);
    List findAll();
}
