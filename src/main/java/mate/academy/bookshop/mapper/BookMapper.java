package mate.academy.bookshop.mapper;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import mate.academy.bookshop.config.MapperConfig;
import mate.academy.bookshop.dto.book.BookDto;
import mate.academy.bookshop.dto.book.BookDtoWithoutCategoryIds;
import mate.academy.bookshop.dto.book.CreateBookRequestDto;
import mate.academy.bookshop.model.Book;
import mate.academy.bookshop.model.Category;
import org.mapstruct.AfterMapping;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(config = MapperConfig.class)
public interface BookMapper {
    BookDto toDto(Book book);

    Book toModel(CreateBookRequestDto createBookRequestDto);

    BookDtoWithoutCategoryIds toDtoWithoutCategories(Book book);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateModelFromDto(CreateBookRequestDto requestDto, @MappingTarget Book book);

    @AfterMapping
    default void setCategoryIds(@MappingTarget BookDto bookDto, Book book) {
        bookDto.setCategoryIds(book.getCategories()
                .stream()
                .map(Category::getId)
                .collect(Collectors.toSet()));

    }

    @AfterMapping
    default void setCategoryIds(@MappingTarget CreateBookRequestDto bookRequestDto, Book book) {
        book.setCategories(
                mapCategoryIdsToCategories(bookRequestDto.getCategories())
        );
    }

    default Set<Category> mapCategoryIdsToCategories(List<Long> categoriesIds) {
        return categoriesIds.stream()
                .map(Category::new)
                .collect(Collectors.toSet());
    }
}
