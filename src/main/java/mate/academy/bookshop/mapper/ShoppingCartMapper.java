package mate.academy.bookshop.mapper;

import mate.academy.bookshop.config.MapperConfig;
import mate.academy.bookshop.dto.shoppingcart.ShoppingCartResponseDto;
import mate.academy.bookshop.model.ShoppingCart;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class, uses = CartItemMapper.class)
public interface ShoppingCartMapper {
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "cartItems", target = "cartItemsDto", qualifiedByName = "cartItemToDto")
    ShoppingCartResponseDto toDto(ShoppingCart shoppingCart);

}
