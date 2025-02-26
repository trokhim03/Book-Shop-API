package mate.academy.bookshop.mapper;

import mate.academy.bookshop.config.MapperConfig;
import mate.academy.bookshop.dto.category.CategoryRequestDto;
import mate.academy.bookshop.dto.category.CategoryResponseDto;
import mate.academy.bookshop.model.Category;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class)
public interface CategoryMapper {
    CategoryResponseDto toDto(Category category);

    Category toEntity(CategoryRequestDto categoryResponseDto);

    void updateModelFromDto(CategoryRequestDto requestDto, @MappingTarget Category category);
}
