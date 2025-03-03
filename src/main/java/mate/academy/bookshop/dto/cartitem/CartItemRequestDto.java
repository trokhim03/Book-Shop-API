package mate.academy.bookshop.dto.cartitem;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CartItemRequestDto {
    @NotNull
    private Long bookId;

    @Min(value = 1,
            message = "Quantity must be at least 1")
    private int quantity;
}
