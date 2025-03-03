package mate.academy.bookshop.repository;

import java.util.Optional;
import mate.academy.bookshop.model.ShoppingCart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, Long> {
    @Query("SELECT sc FROM ShoppingCart sc LEFT JOIN FETCH sc.cartItems "
            + "WHERE sc.user.id = :userId AND sc.user.isDeleted = false")
    Optional<ShoppingCart> findByUserId(Long userId);
}
