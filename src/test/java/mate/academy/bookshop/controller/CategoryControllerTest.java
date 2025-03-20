package mate.academy.bookshop.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import javax.sql.DataSource;
import lombok.SneakyThrows;
import mate.academy.bookshop.dto.category.CategoryResponseDto;
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
class CategoryControllerTest {

    protected static MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    static void beforeAll(
            @Autowired WebApplicationContext webApplicationContext,
            @Autowired DataSource dataSource
    ) throws SQLException {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .apply(springSecurity())
                .build();
        teardown(dataSource);
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(connection,
                    new ClassPathResource("database/category/add-three-default-categories.sql"));
        }
    }

    @AfterAll
    static void afterAll(
            @Autowired DataSource dataSource
    ) {
        teardown(dataSource);
    }

    @SneakyThrows
    static void teardown(DataSource dataSource) {
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource("database/category/remove-all-categories.sql")
            );
        }
    }

    @WithMockUser(username = "user", roles = {"USER"})
    @Test
    @DisplayName("Get all categories: returns all categories successfully")
    public void getAll_GivenCategories_SuccessAndReturnAllCategories() throws Exception {
        // Arrange
        List<CategoryResponseDto> expectedCategories = List.of(
                new CategoryResponseDto()
                        .setId(1L)
                        .setName("Non-Fiction")
                        .setDescription("Books based on facts and real events"),
                new CategoryResponseDto()
                        .setId(2L)
                        .setName("Science Fiction")
                        .setDescription("Books with futuristic and scientific themes"),
                new CategoryResponseDto()
                        .setId(3L)
                        .setName("Mystery")
                        .setDescription("Books involving suspense, crime, or detective stories")
        );

        // Act
        MvcResult mvcResult = mockMvc.perform(get("/category")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        // Assert
        String jsonResponse = mvcResult.getResponse().getContentAsString();
        JsonNode rootNode = objectMapper.readTree(jsonResponse);
        JsonNode contentNode = rootNode.path("content");

        CategoryResponseDto[] actualCategories = objectMapper
                .treeToValue(contentNode, CategoryResponseDto[].class);

        assertEquals(3, actualCategories.length);
        assertEquals(expectedCategories, Arrays.stream(actualCategories).toList());
    }

    @WithMockUser(username = "user", roles = {"USER"})
    @Test
    @DisplayName("Get category by ID: returns category successfully for valid ID")
    public void getCategoryById_ValidCategoryId_SuccessAndReturnCategoryResponseDto()
            throws Exception {
        // Arrange
        Long validCategoryId = 2L;
        CategoryResponseDto expectedCategory = new CategoryResponseDto()
                .setId(2L)
                .setName("Science Fiction")
                .setDescription("Books with futuristic and scientific themes");

        // Act
        MvcResult mvcResult = mockMvc.perform(get("/category/{id}", validCategoryId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        // Assert
        CategoryResponseDto actualCategory = objectMapper
                .readValue(mvcResult.getResponse().getContentAsString(), CategoryResponseDto.class);

        assertEquals(expectedCategory, actualCategory);
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Sql(scripts = "classpath:database/category/remove-fantasy-category.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Test
    @DisplayName("Create category: creates and returns category successfully for valid request")
    public void createCategory_ValidCategoryRequestDto_SuccessAndReturnCategoryResponseDto()
            throws Exception {
        // Arrange
        Long fantasyCategoryId = 4L;
        CategoryResponseDto expectedCategory = new CategoryResponseDto()
                .setId(fantasyCategoryId)
                .setName("Fantasy")
                .setDescription("Book with features magic, mythical creatures, epic adventures");
        String jsonRequest = objectMapper.writeValueAsString(expectedCategory);

        // Act
        MvcResult mvcResult = mockMvc.perform(post("/category")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        // Assert
        CategoryResponseDto actualCategory = objectMapper
                .readValue(mvcResult.getResponse().getContentAsString(), CategoryResponseDto.class);

        assertEquals(expectedCategory.getName(), actualCategory.getName());
        assertEquals(expectedCategory.getDescription(), actualCategory.getDescription());
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Sql(scripts = "classpath:database/category/add-fantasy-category.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/category/remove-update-fantasy-category.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Test
    @DisplayName("Update category: updates and returns category"
            + " successfully for valid ID and request")
    public void updateCategory_ValidCategoryIdAndRequestDto_SuccessAndReturnResponseDto()
            throws Exception {
        // Arrange
        Long fantasyCategoryId = 4L;
        CategoryResponseDto expectedCategory = new CategoryResponseDto()
                .setId(fantasyCategoryId)
                .setName("Update Fantasy")
                .setDescription("Update Book with features magic,"
                        + " mythical creatures, epic adventures");
        String jsonRequest = objectMapper.writeValueAsString(expectedCategory);

        // Act
        MvcResult mvcResult = mockMvc.perform(put("/category/{id}", fantasyCategoryId)
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        // Assert
        CategoryResponseDto actualCategory = objectMapper
                .readValue(mvcResult.getResponse().getContentAsString(), CategoryResponseDto.class);

        assertEquals(expectedCategory.getName(), actualCategory.getName());
        assertEquals(expectedCategory.getDescription(), actualCategory.getDescription());
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Sql(scripts = "classpath:database/category/add-fantasy-category.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/category/remove-fantasy-category.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Test
    @DisplayName("Delete category: deletes category successfully for valid ID")
    public void deleteCategory_ValidCategoryId_Success() throws Exception {
        // Arrange
        Long fantasyCategoryId = 4L;
        int expectedStatusCode = 204;

        // Act
        MvcResult mvcResult = mockMvc.perform(delete("/category/{id}", fantasyCategoryId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andReturn();

        // Assert
        int actualStatusCode = mvcResult.getResponse().getStatus();
        assertEquals(expectedStatusCode, actualStatusCode);
    }

    @WithMockUser(username = "user", roles = {"USER"})
    @Test
    @DisplayName("Get category by ID: returns 404 for invalid category ID")
    public void getCategoryById_InvalidCategoryId_ThrowNotFound() throws Exception {
        // Arrange
        Long invalidId = 999L;
        int expectedStatusCode = 404;

        // Act
        MvcResult mvcResult = mockMvc.perform(get("/category/{id}", invalidId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn();
        int actualStatusCode = mvcResult.getResponse().getStatus();

        // Assert
        assertEquals(expectedStatusCode, actualStatusCode);
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @DisplayName("Update category: returns 404 for invalid category ID")
    public void updateCategory_InvalidCategoryId_ThrowNotFound() throws Exception {
        // Arrange
        Long invalidId = 999L;
        int expectedStatusCode = 404;

        CategoryResponseDto categoryResponseDto = new CategoryResponseDto()
                .setId(5L)
                .setName("Non-existent category")
                .setDescription("This category does not exist");

        String jsonRequest = objectMapper.writeValueAsString(categoryResponseDto);

        // Act
        MvcResult mvcResult = mockMvc.perform(put("/category/{id}", invalidId)
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn();
        int actualStatusCode = mvcResult.getResponse().getStatus();

        // Assert
        assertEquals(expectedStatusCode, actualStatusCode);
    }
}
