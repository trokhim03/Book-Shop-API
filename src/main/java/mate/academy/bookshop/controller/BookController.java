package mate.academy.bookshop.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import mate.academy.bookshop.dto.BookDto;
import mate.academy.bookshop.dto.BookSearchParameters;
import mate.academy.bookshop.dto.CreateBookRequestDto;
import mate.academy.bookshop.service.BookService;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/books")
@Tag(name = "Book Manager",
        description = "Operations for working with books")
public class BookController {
    private final BookService bookService;

    @GetMapping
    @Operation(summary = "Get All Books",
            description = "Get a list of all available books")
    public List<BookDto> findAll(Pageable pageable) {
        return bookService.findAll(pageable);
    }

    @PostMapping
    @Operation(summary = "Create a New Book",
            description = "Save a new book to the system")
    public BookDto save(@RequestBody @Valid CreateBookRequestDto createBookRequestDto) {
        return bookService.save(createBookRequestDto);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get Book by ID",
            description = "Find a book by its unique identifier")
    public BookDto findById(@PathVariable Long id) {
        return bookService.findBookById(id);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete Book by ID",
            description = "Delete a book from the system by its ID")
    public void delete(@PathVariable Long id) {
        bookService.deleteById(id);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update Book by ID",
            description = "Update an existing book's details by ID")
    public BookDto update(@PathVariable Long id,
                          @RequestBody CreateBookRequestDto createBookRequestDto) {
        return bookService.updateById(id, createBookRequestDto);
    }

    @GetMapping("/search")
    @Operation(summary = "Search Books by Parameters",
            description = "Search for books using specific parameters")
    public List<BookDto> search(BookSearchParameters searchParameters) {
        return bookService.search(searchParameters);
    }
}
