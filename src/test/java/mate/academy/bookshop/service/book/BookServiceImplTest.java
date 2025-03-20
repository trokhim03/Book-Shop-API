package mate.academy.bookshop.service.book;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import mate.academy.bookshop.dto.book.BookDto;
import mate.academy.bookshop.dto.book.BookDtoWithoutCategoryIds;
import mate.academy.bookshop.dto.book.BookSearchParameters;
import mate.academy.bookshop.dto.book.CreateBookRequestDto;
import mate.academy.bookshop.exceptions.EntityNotFoundException;
import mate.academy.bookshop.mapper.impl.BookMapperImpl;
import mate.academy.bookshop.model.Book;
import mate.academy.bookshop.repository.BookRepository;
import mate.academy.bookshop.repository.specification.book.BookSpecificationBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

@ExtendWith(MockitoExtension.class)
class BookServiceImplTest {
    @InjectMocks
    private BookServiceImpl bookService;
    @Mock
    private BookRepository bookRepository;
    @Mock
    private BookMapperImpl bookMapper;
    @Mock
    private BookSpecificationBuilder bookSpecificationBuilder;

    private Book book;
    private Book oldBook;
    private BookDto bookDto;
    private CreateBookRequestDto createBookRequestDto;
    private BookDtoWithoutCategoryIds bookDtoWithoutCategoryIds;

    @BeforeEach
    void setUp() {
        book = new Book();
        book.setId(1L);
        book.setTitle("Title");
        book.setAuthor("Author");
        book.setIsbn("978-966-97824-4-1");
        book.setPrice(new BigDecimal(100));
        book.setDescription("A thrilling journey");
        book.setCoverImage("https://www.google.com/search");

        oldBook = new Book();
        oldBook.setId(1L);
        oldBook.setTitle("Old Title");
        oldBook.setAuthor("Old Author");
        oldBook.setIsbn("978-966-97824-4-1");
        oldBook.setPrice(new BigDecimal(200));
        oldBook.setDescription("A old thrilling journey");
        oldBook.setCoverImage("https://www.google.com/search");

        bookDto = new BookDto()
                .setId(book.getId())
                .setTitle(book.getTitle())
                .setAuthor(book.getAuthor())
                .setIsbn(book.getIsbn())
                .setPrice(book.getPrice())
                .setDescription(book.getDescription())
                .setCoverImage(book.getCoverImage());

        createBookRequestDto = new CreateBookRequestDto()
                .setTitle(book.getTitle())
                .setAuthor(book.getAuthor())
                .setIsbn(book.getIsbn())
                .setPrice(book.getPrice())
                .setDescription(book.getDescription())
                .setCoverImage(book.getCoverImage())
                .setCategories(List.of(1L, 2L));

        bookDtoWithoutCategoryIds = new BookDtoWithoutCategoryIds()
                .setId(book.getId())
                .setTitle(book.getTitle())
                .setAuthor(book.getAuthor())
                .setIsbn(book.getIsbn())
                .setPrice(book.getPrice())
                .setDescription(book.getDescription())
                .setCoverImage(book.getCoverImage());
    }

    @Test
    @DisplayName("Save book with valid request DTO returns book DTO")
    void save_WithValidRequestDto_ReturnBookDto() {
        when(bookMapper.toModel(createBookRequestDto)).thenReturn(book);
        when(bookRepository.save(book)).thenReturn(book);
        when(bookMapper.toDto(book)).thenReturn(bookDto);

        BookDto expectedDto = bookDto;
        BookDto actualDto = bookService.save(createBookRequestDto);

        assertThat(actualDto).isEqualTo(expectedDto);
        verify(bookMapper).toModel(createBookRequestDto);
        verify(bookRepository).save(book);
        verify(bookMapper).toDto(book);
    }

    @Test
    @DisplayName("Find all books with valid pageable returns all books")
    void findAll_ValidPageable_ReturnAllBooks() {
        Pageable pageable = PageRequest.of(0, 10);
        PageImpl<Book> bookPage = new PageImpl<>(List.of(book), pageable, 1);

        when(bookRepository.findAll(pageable)).thenReturn(bookPage);
        when(bookMapper.toDto(book)).thenReturn(bookDto);

        BookDto expectedDto = bookDto;
        Page<BookDto> actualBookDtoPage = bookService.findAll(pageable);

        assertThat(actualBookDtoPage.getContent()).hasSize(1).contains(expectedDto);
        verify(bookRepository).findAll(pageable);
        verify(bookMapper).toDto(book);
    }

    @Test
    @DisplayName("Find book by valid ID returns book DTO")
    void findBookById_ValidLongId_ReturnBookDto() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(bookMapper.toDto(book)).thenReturn(bookDto);

        BookDto expectedDto = bookDto;
        BookDto actualBookDtoById = bookService.findBookById(1L);

        assertThat(actualBookDtoById).isEqualTo(expectedDto);
        verify(bookRepository).findById(1L);
        verify(bookMapper).toDto(book);
    }

    @Test
    @DisplayName("Find book by invalid ID throws EntityNotFoundException")
    void findBookById_InvalidLongId_ThrowEntityNotFoundException() {
        Long invalidId = 2L;
        when(bookRepository.findById(invalidId)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> bookService.findBookById(invalidId));

        String expectedMessage = "Can't find book by id: " + invalidId;
        String actualMessage = exception.getMessage();

        assertThat(expectedMessage).isEqualTo(actualMessage);
        verify(bookRepository).findById(invalidId);
    }

    @Test
    @DisplayName("Delete book by valid ID removes it from the database")
    void deleteById_ValidLongId_DeletesBook() {
        Long validId = 3L;

        bookService.deleteById(validId);

        verify(bookRepository).deleteById(validId);
    }

    @Test
    @DisplayName("Update book with valid ID and request DTO returns updated book DTO")
    void updateById_ValidIdAndBookRequestDto_ReturnBookDto() {
        Long validId = 1L;
        when(bookRepository.findById(validId)).thenReturn(Optional.of(oldBook));
        when(bookMapper.toDto(oldBook)).thenReturn(bookDto);
        doNothing().when(bookMapper).updateModelFromDto(createBookRequestDto, oldBook);

        BookDto expectedDto = bookDto;
        BookDto actualBookDto = bookService.updateById(validId, createBookRequestDto);

        assertThat(actualBookDto).isEqualTo(expectedDto);
        verify(bookRepository).findById(validId);
        verify(bookMapper).updateModelFromDto(createBookRequestDto, oldBook);
        verify(bookRepository).save(oldBook);
        verify(bookMapper).toDto(oldBook);
    }

    @Test
    @DisplayName("Update book with invalid ID throws EntityNotFoundException")
    void updateById_InvalidId_ThrowEntityNotFoundException() {
        Long invalidId = 4L;
        when(bookRepository.findById(invalidId)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> bookService.updateById(invalidId, createBookRequestDto));

        String expectedMessage = "Can't find book by id: " + invalidId;
        String actualMessage = exception.getMessage();

        assertThat(expectedMessage).isEqualTo(actualMessage);
        verify(bookRepository).findById(invalidId);
    }

    @Test
    @DisplayName("Search books with valid parameters returns list of book DTOs")
    void search_WithValidParameters_ReturnListBookDto() {
        BookSearchParameters parameters = new BookSearchParameters(
                new String[]{"Title"},
                new String[]{"Author"}
        );
        Specification<Book> mockSpecification = mock(Specification.class);

        when(bookSpecificationBuilder.build(parameters)).thenReturn(mockSpecification);
        when(bookRepository.findAll(mockSpecification)).thenReturn(List.of(book));
        when(bookMapper.toDto(book)).thenReturn(bookDto);

        BookDto expectedDto = bookDto;
        List<BookDto> actualSearch = bookService.search(parameters);

        assertThat(actualSearch).hasSize(1).contains(expectedDto);
        verify(bookSpecificationBuilder).build(parameters);
        verify(bookRepository).findAll(mockSpecification);
        verify(bookMapper).toDto(book);
    }

    @Test
    @DisplayName("Get books by valid category ID returns list of book DTOs without category IDs")
    void getBooksByCategoryId_WithValidLongId_ReturnListBookDtoWithoutCategoryIds() {
        Long categoryId = 1L;
        when(bookRepository.findAllByCategoriesId(categoryId)).thenReturn(List.of(book));
        when(bookMapper.toDtoWithoutCategories(book)).thenReturn(bookDtoWithoutCategoryIds);

        BookDtoWithoutCategoryIds expectedDtoWithoutCategoryIds = bookDtoWithoutCategoryIds;
        List<BookDtoWithoutCategoryIds> actualBooksByCategoryId = bookService
                .getBooksByCategoryId(categoryId);

        assertThat(actualBooksByCategoryId).hasSize(1).contains(expectedDtoWithoutCategoryIds);
        verify(bookRepository).findAllByCategoriesId(categoryId);
        verify(bookMapper).toDtoWithoutCategories(book);
    }

    @Test
    @DisplayName("Get books by invalid category ID returns empty list")
    void getBooksByCategoryId_NoBooksFound_ReturnEmptyList() {
        Long invalidCategoryId = 3L;
        when(bookRepository.findAllByCategoriesId(invalidCategoryId)).thenReturn(List.of());

        List<BookDtoWithoutCategoryIds> actualBooksByCategoryId = bookService
                .getBooksByCategoryId(invalidCategoryId);

        assertThat(actualBooksByCategoryId).isEmpty();
        verify(bookRepository).findAllByCategoriesId(invalidCategoryId);
    }
}
