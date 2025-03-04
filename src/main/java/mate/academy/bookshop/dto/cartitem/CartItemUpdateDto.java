package mate.academy.bookshop.dto.cartitem;

import jakarta.validation.constraints.Positive;

public record CartItemUpdateDto(@Positive int quantity) {
}
