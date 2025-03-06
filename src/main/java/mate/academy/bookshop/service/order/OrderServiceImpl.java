package mate.academy.bookshop.service.order;

import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import mate.academy.bookshop.dto.order.OrderRequestDto;
import mate.academy.bookshop.dto.order.OrderResponseDto;
import mate.academy.bookshop.dto.order.OrderUpdateStatusDto;
import mate.academy.bookshop.dto.orderitem.OrderItemResponseDto;
import mate.academy.bookshop.exceptions.EntityNotFoundException;
import mate.academy.bookshop.exceptions.OrderProcessingException;
import mate.academy.bookshop.mapper.OrderItemMapper;
import mate.academy.bookshop.mapper.OrderMapper;
import mate.academy.bookshop.model.CartItem;
import mate.academy.bookshop.model.Order;
import mate.academy.bookshop.model.OrderItem;
import mate.academy.bookshop.model.ShoppingCart;
import mate.academy.bookshop.repository.OrderItemRepository;
import mate.academy.bookshop.repository.OrderRepository;
import mate.academy.bookshop.repository.ShoppingCartRepository;
import mate.academy.bookshop.repository.UserRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderItemMapper orderItemMapper;
    private final OrderMapper orderMapper;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ShoppingCartRepository shoppingCartRepository;
    private final UserRepository userRepository;

    @Override
    public OrderResponseDto createOrderByUserId(Long userId, OrderRequestDto orderRequestDto) {
        ShoppingCart shoppingCart = shoppingCartRepository
                .findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException("Shopping cart not found"
                        + " for user with id: " + userId
                ));
        if (shoppingCart.getCartItems().isEmpty()) {
            throw new OrderProcessingException("Shopping cart is empty "
                    + "for user id: " + userId);
        }
        Order order = orderMapper.toEntity(orderRequestDto);
        order.setUser(userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User with id "
                        + userId + " not found")));
        order.setStatus(Order.Status.PENDING);
        order.setOrderDate(LocalDateTime.now());

        Set<OrderItem> orderItemSet = shoppingCart
                .getCartItems()
                .stream()
                .map(cart -> mapToOrderItem(cart, order))
                .collect(Collectors.toSet());

        order.setOrderItems(orderItemSet);

        BigDecimal totalPrice = orderItemSet
                .stream()
                .map(OrderItem::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        order.setTotal(totalPrice);
        orderRepository.save(order);
        return orderMapper.toDto(order);
    }

    @Override
    public List<OrderResponseDto> getOrderByUserId(Pageable pageable, Long userId) {
        List<Order> ordersByUser = orderRepository.findAllByUserId(pageable, userId);
        return orderMapper.toDto(ordersByUser);
    }

    @Override
    public OrderResponseDto updateStatusByOrderId(Long orderId,
                                                  OrderUpdateStatusDto orderUpdateStatusDto) {
        Order order = orderRepository
                .findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order not found with id: "
                        + orderId
                ));
        orderMapper.updateOrderStatusFromDto(orderUpdateStatusDto, order);
        orderRepository.save(order);
        return orderMapper.toDto(order);
    }

    @Override
    public List<OrderItemResponseDto> getOrderItemsByOrderId(Long orderId, Long userId) {
        Order order = orderRepository
                .findByIdAndUserId(orderId, userId)
                .orElseThrow(() -> new EntityNotFoundException("Order not found "
                        + "for order id: " + orderId + " and user id: " + userId
                ));
        return orderItemMapper.toOrderItemDtoList(orderItemRepository.findAllByOrder(order));
    }

    @Override
    public OrderItemResponseDto getOrderItemFromOrderById(Long orderId,
                                                          Long orderItemId,
                                                          Long userId) {
        OrderItem orderItem = orderItemRepository
                .findByIdAndOrderIdAndOrderUserId(orderItemId, orderId, userId)
                .orElseThrow(() -> new EntityNotFoundException("Can't find order item by order id: "
                        + orderId + "  and order item id: " + orderItemId));

        return orderItemMapper.toDto(orderItem);
    }

    private OrderItem mapToOrderItem(CartItem cart, Order order) {
        OrderItem orderItem = new OrderItem();
        orderItem.setOrder(order);
        orderItem.setBook(cart.getBook());
        orderItem.setQuantity(cart.getQuantity());
        orderItem.setPrice(cart.getBook().getPrice().multiply(new BigDecimal(cart.getQuantity())));
        return orderItem;
    }
}
