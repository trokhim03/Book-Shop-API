package mate.academy.bookshop.controller;

import static org.apache.commons.lang3.builder.EqualsBuilder.reflectionEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import javax.sql.DataSource;
import lombok.SneakyThrows;
import mate.academy.bookshop.dto.book.BookDto;
import mate.academy.bookshop.dto.book.CreateBookRequestDto;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BookControllerTest {

    protected static MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    static void beforeAll(
            @Autowired DataSource dataSource,
            @Autowired WebApplicationContext webApplicationContext
    ) throws SQLException {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .apply(springSecurity())
                .build();
        teardown(dataSource);
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource("database/books/add-three-default-books.sql")
            );
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource("database/category/add-category.sql")
            );
        }
    }

    @AfterAll
    static void afterAll(@Autowired DataSource dataSource) {
        teardown(dataSource);
    }

    @SneakyThrows
    static void teardown(DataSource dataSource) {
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource("database/books/remove-all-books.sql")
            );
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource("database/category/remove-all-categories.sql")
            );
        }
    }

    @WithMockUser(username = "user", roles = {"USER"})
    @Test
    @DisplayName("Find all books: returns all books successfully")
    public void findAll_GivenBooks_SuccessAndReturnAllBooks() throws Exception {
        // Arrange
        List<BookDto> expectedBookDto = new ArrayList<>();

        expectedBookDto.add(new BookDto()
                .setId(1L)
                .setTitle("The Great Adventure")
                .setAuthor("John Doe")
                .setIsbn("978-3-16-148410-0")
                .setPrice(new BigDecimal("19.99"))
                .setDescription("An amazing journey")
                .setCoverImage("https://example.com/image1.jpg")
                .setCategoryIds(Collections.emptySet())
        );

        expectedBookDto.add(new BookDto()
                .setId(2L)
                .setTitle("Mystery of the Night")
                .setAuthor("Jane Smith")
                .setIsbn("978-1-23-456789-7")
                .setPrice(new BigDecimal("25.50"))
                .setDescription("A thrilling mystery")
                .setCoverImage("https://example.com/image2.jpg")
                .setCategoryIds(Collections.emptySet())
        );

        expectedBookDto.add(new BookDto()
                .setId(3L)
                .setTitle("Programming in Java")
                .setAuthor("Alice Johnson")
                .setIsbn("978-0-12-345678-9")
                .setPrice(new BigDecimal("35.00"))
                .setDescription("Learn Java from scratch")
                .setCoverImage("https://example.com/image3.jpg")
                .setCategoryIds(Collections.emptySet())
        );

        // Act
        MvcResult result = mockMvc.perform(get("/books")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        // Assert
        String jsonResponse = result.getResponse().getContentAsString();
        JsonNode rootNode = objectMapper.readTree(jsonResponse);
        JsonNode contentNode = rootNode.path("content");

        BookDto[] actual = objectMapper.treeToValue(contentNode, BookDto[].class);

        assertEquals(3, actual.length);
        for (int i = 0; i < expectedBookDto.size(); i++) {
            BookDto expected = expectedBookDto.get(i);
            BookDto actualDto = actual[i];

            assertEquals(expected.getId(), actualDto.getId());
            assertEquals(expected.getTitle(), actualDto.getTitle());
            assertEquals(expected.getAuthor(), actualDto.getAuthor());
            assertEquals(expected.getIsbn(), actualDto.getIsbn());

            BigDecimal expectedPrice = expected.getPrice().stripTrailingZeros();
            BigDecimal actualPrice = actualDto.getPrice().stripTrailingZeros();
            assertEquals(expectedPrice, actualPrice);

            assertEquals(expected.getDescription(), actualDto.getDescription());
            assertEquals(expected.getCoverImage(), actualDto.getCoverImage());
            assertEquals(expected.getCategoryIds(), actualDto.getCategoryIds());
        }
    }

    @WithMockUser(username = "user", roles = {"USER"})
    @Test
    @DisplayName("Find book by ID: returns book successfully for valid ID")
    public void findById_ValidBookId_SuccessAndReturnBookDto() throws Exception {
        // Arrange
        Long validBookId = 1L;

        BookDto expected = new BookDto()
                .setId(1L)
                .setTitle("The Great Adventure")
                .setAuthor("John Doe")
                .setIsbn("978-3-16-148410-0")
                .setPrice(new BigDecimal("19.99").setScale(2, RoundingMode.HALF_UP))
                .setDescription("An amazing journey")
                .setCoverImage("https://example.com/image1.jpg")
                .setCategoryIds(Collections.emptySet());

        // Act
        MvcResult result = mockMvc.perform(get("/books/{id}", validBookId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        // Assert
        BookDto actualDto = objectMapper
                .readValue(result.getResponse().getContentAsString(), BookDto.class);

        assertEquals(expected, actualDto);
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Sql(scripts = "classpath:database/books/remove-abetka-and-relationship.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Test
    @DisplayName("Create book: creates and returns book successfully for valid request")
    public void create_ValidRequestDto_SuccessAndReturnBookDto() throws Exception {
        // Arrange
        CreateBookRequestDto requestDto = new CreateBookRequestDto()
                .setTitle("Abetka")
                .setAuthor("Hryhoriy Falkovych")
                .setIsbn("978-3-16-148410-0")
                .setPrice(new BigDecimal("50.00"))
                .setDescription("A classic Ukrainian alphabet book in"
                        + " verse with bright illustrations for children.")
                .setCoverImage("https://example.com/image1.jpg")
                .setCategories(List.of(1L));

        BookDto expectedDto = new BookDto()
                .setId(4L)
                .setTitle("Abetka")
                .setAuthor("Hryhoriy Falkovych")
                .setIsbn("978-3-16-148410-0")
                .setPrice(new BigDecimal("50.00"))
                .setDescription("A classic Ukrainian alphabet book in verse "
                        + "with bright illustrations for children.")
                .setCoverImage("https://example.com/image1.jpg")
                .setCategoryIds(Set.of(1L));

        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        // Act
        MvcResult mvcResult = mockMvc.perform(post("/books")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        // Assert
        BookDto actualDto = objectMapper
                .readValue(mvcResult.getResponse().getContentAsString(), BookDto.class);

        reflectionEquals(expectedDto, actualDto, "id");
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Sql(scripts = "classpath:database/books/add-abetka-book.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/books/remove-abetka-and-relationship.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Test
    @DisplayName("Delete book: deletes book successfully for valid ID")
    public void delete_ValidBookId_Success() throws Exception {
        // Arrange
        Long validBookId = 4L;
        int expectedStatusCode = 204;

        // Act
        MvcResult mvcResult = mockMvc.perform(delete("/books/{id}", validBookId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andReturn();

        // Assert
        int actualStatusCode = mvcResult.getResponse().getStatus();
        assertEquals(expectedStatusCode, actualStatusCode);
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @Sql(scripts = "classpath:database/books/add-abetka-book.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/books/remove-abetka-and-relationship.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @DisplayName("Update book: updates and returns book successfully for valid ID and request")
    public void update_ValidBookIdAndBookRequestDto_SuccessAndReturnBookDto() throws Exception {
        // Arrange
        MvcResult getResult = mockMvc.perform(get("/books")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = getResult.getResponse().getContentAsString();
        JsonNode rootNode = objectMapper.readTree(jsonResponse);
        JsonNode contentNode = rootNode.path("content");

        BookDto[] actual = objectMapper.treeToValue(contentNode, BookDto[].class);
        Long validId = Arrays.stream(actual).toList().get(actual.length - 1).getId();

        CreateBookRequestDto requestDto = new CreateBookRequestDto()
                .setTitle("Updated Title")
                .setAuthor("Updated Author")
                .setPrice(new BigDecimal("50.00"))
                .setDescription("A classic Ukrainian alphabet book in verse "
                        + "with bright illustrations for children.")
                .setCoverImage("https://example.com/image1.jpg")
                .setCategories(List.of(1L));

        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        // Act
        MvcResult mvcResult = mockMvc.perform(put("/books/{id}", validId)
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        // Assert
        BookDto actualDto = objectMapper
                .readValue(mvcResult.getResponse().getContentAsString(), BookDto.class);

        assertEquals(requestDto.getTitle(), actualDto.getTitle());
        assertEquals(requestDto.getAuthor(), actualDto.getAuthor());
        assertEquals(requestDto.getPrice(), actualDto.getPrice());
        assertEquals(requestDto.getDescription(), actualDto.getDescription());
        assertEquals(requestDto.getCoverImage(), actualDto.getCoverImage());
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @DisplayName("Update book: throws not found exception for invalid ID")
    public void update_InvalidBookId_ThrowNotFound() throws Exception {
        // Arrange
        Long invalidId = 999L;
        int expected = 404;

        CreateBookRequestDto requestDto = new CreateBookRequestDto()
                .setTitle("Non-existent Book")
                .setAuthor("Unknown Author")
                .setPrice(new BigDecimal("100.00"))
                .setDescription("This book does not exist.")
                .setCoverImage("https://example.com/non-existent.jpg")
                .setCategories(List.of(1L));

        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        // Act
        MvcResult mvcResult = mockMvc.perform(put("/books/{id}", invalidId)
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn();

        // Assert
        int actual = mvcResult.getResponse().getStatus();
        assertEquals(expected, actual);
    }

    @WithMockUser(username = "user", roles = {"USER"})
    @Test
    @DisplayName("Find book by ID: throws not found exception for invalid ID")
    public void findById_InvalidBookId_ThrowNotFound() throws Exception {
        // Arrange
        Long invalidId = 999L;
        int expected = 404;

        // Act
        MvcResult mvcResult = mockMvc.perform(get("/books/{id}", invalidId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn();

        // Assert
        int actual = mvcResult.getResponse().getStatus();
        assertEquals(expected, actual);
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @DisplayName("Create book: throws bad request exception for invalid request")
    public void create_InvalidRequestDto_ThrowException() throws Exception {
        // Arrange
        int expected = 400;

        CreateBookRequestDto createBookRequestDto = new CreateBookRequestDto()
                .setTitle("Non-existent Book")
                .setAuthor("Unknown Author")
                .setPrice(new BigDecimal("100.00"))
                .setDescription("This book does not exist.")
                .setCoverImage("https://example.com/non-existent.jpg");

        String jsonRequest = objectMapper.writeValueAsString(createBookRequestDto);

        // Act
        MvcResult mvcResult = mockMvc.perform(post("/books")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();

        // Assert
        int actual = mvcResult.getResponse().getStatus();
        assertEquals(expected, actual);
    }
}
