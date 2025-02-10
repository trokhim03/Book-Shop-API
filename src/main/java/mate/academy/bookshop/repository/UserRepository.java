package mate.academy.bookshop.repository;

import java.util.Optional;
import mate.academy.bookshop.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    Boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);
}
