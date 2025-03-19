package mate.academy.bookshop.service.category;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import mate.academy.bookshop.dto.category.CategoryRequestDto;
import mate.academy.bookshop.dto.category.CategoryResponseDto;
import mate.academy.bookshop.exceptions.EntityNotFoundException;
import mate.academy.bookshop.mapper.impl.CategoryMapperImpl;
import mate.academy.bookshop.model.Category;
import mate.academy.bookshop.repository.CategoryRepository;
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

@ExtendWith(MockitoExtension.class)
class CategoryServiceImplTest {
    @InjectMocks
    private CategoryServiceImpl categoryService;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CategoryMapperImpl categoryMapper;

    private Category category;

    private Category oldCategory;

    private CategoryRequestDto categoryRequestDto;

    private CategoryResponseDto categoryResponseDto;

    @BeforeEach
    void setUp() {
        category = new Category();
        category.setId(1L);
        category.setName("Comedy");
        category.setDescription("A little funny book");

        oldCategory = new Category();
        oldCategory.setId(1L);
        oldCategory.setName("Comedy old version");
        oldCategory.setDescription("A old funny book");

        categoryRequestDto = new CategoryRequestDto()
                .setName(category.getName())
                .setDescription(category.getDescription());

        categoryResponseDto = new CategoryResponseDto()
                .setId(category.getId())
                .setName(category.getName())
                .setDescription(category.getDescription());
    }

    @Test
    @DisplayName("Save category with valid request DTO returns response DTO")
    public void save_WithValidRequestDto_ReturnResponseDto() {
        when(categoryMapper.toEntity(categoryRequestDto)).thenReturn(category);
        when(categoryRepository.save(category)).thenReturn(category);
        when(categoryMapper.toDto(category)).thenReturn(categoryResponseDto);

        CategoryResponseDto expectedDto = categoryResponseDto;
        CategoryResponseDto actualDto = categoryService.save(categoryRequestDto);

        assertNotNull(actualDto);
        assertThat(actualDto).isEqualTo(expectedDto);

        verify(categoryMapper).toEntity(categoryRequestDto);
        verify(categoryRepository).save(category);
        verify(categoryMapper).toDto(category);
    }

    @Test
    @DisplayName("Find all categories with valid pageable returns all categories")
    public void findAll_ValidPageable_ReturnAllCategories() {
        Pageable pageable = PageRequest.of(0, 10);
        PageImpl<Category> categoryPage = new PageImpl<>(List.of(category), pageable, 1);

        when(categoryRepository.findAll(pageable)).thenReturn(categoryPage);
        when(categoryMapper.toDto(category)).thenReturn(categoryResponseDto);

        CategoryResponseDto expectedDto = categoryResponseDto;
        Page<CategoryResponseDto> actualPageCategory = categoryService.findAll(pageable);

        assertNotNull(actualPageCategory);
        assertThat(actualPageCategory.getContent()).hasSize(1).contains(expectedDto);

        verify(categoryRepository).findAll(pageable);
        verify(categoryMapper).toDto(category);
    }

    @Test
    @DisplayName("Get category by valid ID returns category response DTO")
    public void getById_ValidCategoryId_ReturnCategoryResponseDto() {
        Long validId = 1L;

        when(categoryRepository.findById(validId)).thenReturn(Optional.of(category));
        when(categoryMapper.toDto(category)).thenReturn(categoryResponseDto);

        CategoryResponseDto expectedDto = categoryResponseDto;
        CategoryResponseDto actualCategoryById = categoryService.getById(validId);

        assertThat(expectedDto).isEqualTo(actualCategoryById);

        verify(categoryRepository).findById(validId);
        verify(categoryMapper).toDto(category);
    }

    @Test
    @DisplayName("Get category by invalid ID throws EntityNotFoundException")
    public void getById_InvalidCategoryId_ThrowEntityNotFoundException() {
        Long invalidId = 2L;

        when(categoryRepository.findById(invalidId)).thenReturn(Optional.empty());

        EntityNotFoundException entityNotFoundException = assertThrows(
                EntityNotFoundException.class,
                () -> categoryService.getById(invalidId));

        String expectedMessage = "Can't find category by id: " + invalidId;
        String actualMessage = entityNotFoundException.getMessage();

        assertEquals(expectedMessage, actualMessage);

        verify(categoryRepository).findById(invalidId);
    }

    @Test
    @DisplayName("Update category with valid ID and "
            + "request DTO returns updated category response DTO")
    public void update_WithValidLongIdAndCategoryRequestDto_ReturnCategoryResponseDto() {
        Long validId = 1L;

        when(categoryRepository.findById(validId)).thenReturn(Optional.of(oldCategory));
        doNothing().when(categoryMapper)
                .updateModelFromDto(categoryRequestDto, oldCategory);
        when(categoryMapper.toDto(oldCategory)).thenReturn(categoryResponseDto);
        when(categoryRepository.save(oldCategory)).thenReturn(oldCategory);

        CategoryResponseDto expectedDto = categoryResponseDto;
        CategoryResponseDto actualUpdateCategoryResponseDto = categoryService
                .update(validId, categoryRequestDto);

        assertNotNull(actualUpdateCategoryResponseDto);
        assertEquals(expectedDto.getName(), actualUpdateCategoryResponseDto.getName());
        assertEquals(expectedDto.getDescription(),
                actualUpdateCategoryResponseDto.getDescription());

        verify(categoryRepository).findById(validId);
        verify(categoryMapper).updateModelFromDto(categoryRequestDto, oldCategory);
        verify(categoryMapper).toDto(oldCategory);
        verify(categoryRepository).save(oldCategory);
    }

    @Test
    @DisplayName("Update category with invalid ID throws EntityNotFoundException")
    public void update_WithInvalidLongId_ThrowEntityNotFoundException() {
        Long invalidId = 2L;

        when(categoryRepository.findById(invalidId)).thenReturn(Optional.empty());
        EntityNotFoundException entityNotFoundException = assertThrows(
                EntityNotFoundException.class,
                () -> categoryService.update(invalidId, categoryRequestDto));

        String expectedMessage = "Can't find category by id: " + invalidId;
        String actualMessage = entityNotFoundException.getMessage();

        assertEquals(expectedMessage, actualMessage);

        verify(categoryRepository).findById(invalidId);
    }

    @Test
    @DisplayName("Delete category by valid ID removes it from the database")
    public void deleteById_WithValidLongId_DeletedFromDb() {
        Long validId = 1L;

        categoryService.deleteById(validId);

        verify(categoryRepository).deleteById(validId);
    }
}
