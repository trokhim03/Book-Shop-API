package mate.academy.bookshop.dto.book;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import lombok.Data;
import org.hibernate.validator.constraints.ISBN;
import org.hibernate.validator.constraints.URL;

@Data
public class CreateBookRequestDto {
    @NotNull
    @Size(min = 1, max = 100,
            message = "Title must be between 1 and 100 characters.")
    private String title;

    @NotNull
    @Size(min = 0, max = 50,
            message = "Author must be between 1 and 50 characters.")
    private String author;

    @NotNull
    @ISBN(type = ISBN.Type.ISBN_13,
            message = "Invalid ISBN-13 format! Please check the entered value.")
    private String isbn;

    @NotNull
    @Min(0)
    private BigDecimal price;

    @NotNull
    @Size(max = 1000,
            message = "Description can be up to 1000 characters.")
    private String description;

    @NotNull
    @URL(protocol = "https", message = "Cover image URL must use HTTPS.")
    private String coverImage;
}
