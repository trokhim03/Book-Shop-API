package mate.academy.bookshop.service.shoppingcart;

import mate.academy.bookshop.dto.cartitem.CartItemRequestDto;
import mate.academy.bookshop.dto.cartitem.CartItemUpdateDto;
import mate.academy.bookshop.dto.shoppingcart.ShoppingCartResponseDto;
import mate.academy.bookshop.model.User;

public interface ShoppingCartService {
    void createShoppingCart(User user);

    ShoppingCartResponseDto getShoppingCart(Long userId);

    ShoppingCartResponseDto addCartItem(CartItemRequestDto cartItemUpdateDto, Long userId);

    ShoppingCartResponseDto updateCartItems(Long userId,
                                            Long cartItemId,
                                            CartItemUpdateDto cartItemUpdateDto
    );

    void deleteCartItemFromCart(Long userId, Long cartItemId);
}
