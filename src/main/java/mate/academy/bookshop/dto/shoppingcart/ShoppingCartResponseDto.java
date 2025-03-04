package mate.academy.bookshop.dto.shoppingcart;

import java.util.Set;
import lombok.Data;
import mate.academy.bookshop.dto.cartitem.CartItemResponseDto;

@Data
public class ShoppingCartResponseDto {
    private Long id;

    private Long userId;

    private Set<CartItemResponseDto> cartItemsDto;
}
