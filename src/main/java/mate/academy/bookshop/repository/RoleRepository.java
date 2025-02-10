package mate.academy.bookshop.repository;

import java.util.Optional;
import mate.academy.bookshop.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(Role.RoleName name);
}
