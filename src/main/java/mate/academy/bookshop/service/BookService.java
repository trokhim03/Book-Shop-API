package mate.academy.bookshop.service;

import java.util.List;
import mate.academy.bookshop.dto.BookDto;
import mate.academy.bookshop.dto.BookSearchParameters;
import mate.academy.bookshop.dto.CreateBookRequestDto;

public interface BookService {
    BookDto save(CreateBookRequestDto createBookRequestDto);

    List<BookDto> findAll();

    BookDto findBookById(Long id);

    void deleteById(Long id);

    BookDto updateById(Long id, CreateBookRequestDto requestDto);

    List<BookDto> search(BookSearchParameters parameters);
}
