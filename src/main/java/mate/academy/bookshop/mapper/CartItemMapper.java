package mate.academy.bookshop.mapper;

import mate.academy.bookshop.config.MapperConfig;
import mate.academy.bookshop.dto.cartitem.CartItemRequestDto;
import mate.academy.bookshop.dto.cartitem.CartItemResponseDto;
import mate.academy.bookshop.dto.cartitem.CartItemUpdateDto;
import mate.academy.bookshop.model.CartItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

@Mapper(config = MapperConfig.class, uses = BookMapper.class)
public interface CartItemMapper {
    @Named("cartItemToDto")
    @Mapping(source = "book.id", target = "bookId")
    @Mapping(source = "book.title", target = "bookTitle")
    CartItemResponseDto toDto(CartItem cartItem);

    @Mapping(target = "book", source = "bookId", qualifiedByName = "bookById")
    @Mapping(target = "shoppingCart", ignore = true)
    CartItem toEntity(CartItemRequestDto cartItemRequestDto);

    void updateCartItemFromDto(CartItemUpdateDto cartItemUpdateDto,
                               @MappingTarget CartItem cartItem);
}
