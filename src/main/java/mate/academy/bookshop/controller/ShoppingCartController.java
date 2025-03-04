package mate.academy.bookshop.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mate.academy.bookshop.dto.cartitem.CartItemRequestDto;
import mate.academy.bookshop.dto.cartitem.CartItemUpdateDto;
import mate.academy.bookshop.dto.shoppingcart.ShoppingCartResponseDto;
import mate.academy.bookshop.model.User;
import mate.academy.bookshop.service.shoppingcart.ShoppingCartService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "Shopping Cart",
        description = "Operations for managing shopping cart")
@RequestMapping("/cart")
public class ShoppingCartController {
    private final ShoppingCartService shoppingCartService;

    @Operation(summary = "Get the authenticated user's shopping cart",
            description = "Retrieves the shopping cart of the currently authenticated user.")
    @GetMapping
    public ShoppingCartResponseDto getShoppingCart(Authentication authentication) {
        Long authenticatedUserId = getAuthenticatedUserId(authentication);
        return shoppingCartService.getShoppingCart(authenticatedUserId);
    }

    @Operation(summary = "Add an item to the shopping cart",
            description = "Adds a new item to the shopping cart for the authenticated user.")
    @PostMapping
    public ShoppingCartResponseDto addCartItem(
            @RequestBody @Valid CartItemRequestDto cartItemRequestDto,
            Authentication authentication) {
        Long authenticatedUserId = getAuthenticatedUserId(authentication);
        return shoppingCartService.addCartItem(cartItemRequestDto, authenticatedUserId);
    }

    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Update an item in the shopping cart",
            description = "Updates the details of a specific item"
                    + " in the shopping cart for the authenticated user.")
    @PutMapping("/cart-items/{cartItemId}")
    public ShoppingCartResponseDto updateCartItems(
            @RequestBody @Valid CartItemUpdateDto cartItemUpdateDto,
            @PathVariable Long cartItemId,
            Authentication authentication) {
        Long authenticatedUserId = getAuthenticatedUserId(authentication);
        return shoppingCartService.updateCartItems(
                authenticatedUserId,
                cartItemId,
                cartItemUpdateDto
        );
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete an item from the shopping cart",
            description = "Removes a specific item from the shopping cart"
                    + " for the authenticated user.")
    @DeleteMapping("/cart-items/{cartItemId}")
    public void deleteCartItem(@PathVariable Long cartItemId, Authentication authentication) {
        Long authenticatedUserId = getAuthenticatedUserId(authentication);
        shoppingCartService.deleteCartItemFromCart(authenticatedUserId, cartItemId);
    }

    private Long getAuthenticatedUserId(Authentication authentication) {
        return ((User) authentication.getPrincipal()).getId();
    }
}
