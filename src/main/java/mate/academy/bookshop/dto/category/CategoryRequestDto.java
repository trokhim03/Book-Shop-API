package mate.academy.bookshop.dto.category;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CategoryRequestDto {
    @NotBlank
    @Size(min = 2, max = 100,
            message = "Author must be between 2 and 100 characters.")
    private String name;

    @NotBlank
    @Size(max = 1000,
            message = "Description can be up to 1000 characters.")
    private String description;
}
