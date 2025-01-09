package mate.academy.bookshop.service;

import java.util.List;
import mate.academy.bookshop.dto.BookDto;
import mate.academy.bookshop.dto.CreateBookRequestDto;

public interface BookService {
    BookDto save(CreateBookRequestDto createBookRequestDto);

    List<BookDto> findAll();

    BookDto findBookById(Long id);

    void deleteById(Long id);
}
