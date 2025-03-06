package mate.academy.bookshop.service.order;

import java.util.List;
import mate.academy.bookshop.dto.order.OrderRequestDto;
import mate.academy.bookshop.dto.order.OrderResponseDto;
import mate.academy.bookshop.dto.order.OrderUpdateStatusDto;
import mate.academy.bookshop.dto.orderitem.OrderItemResponseDto;
import org.springframework.data.domain.Pageable;

public interface OrderService {
    OrderResponseDto createOrderByUserId(Long userId, OrderRequestDto orderRequestDto);

    List<OrderResponseDto> getOrderByUserId(Pageable pageable, Long userId);

    OrderResponseDto updateStatusByOrderId(Long orderId,
                                           OrderUpdateStatusDto orderUpdateStatusDto);

    List<OrderItemResponseDto> getOrderItemsByOrderId(Long orderId, Long userId);

    OrderItemResponseDto getOrderItemFromOrderById(Long orderId,
                                                   Long orderItemId,
                                                   Long userId);
}
