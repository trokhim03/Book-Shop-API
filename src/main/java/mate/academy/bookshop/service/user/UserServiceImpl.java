package mate.academy.bookshop.service.user;

import jakarta.transaction.Transactional;
import java.util.Optional;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import mate.academy.bookshop.dto.user.UserRegistrationRequestDto;
import mate.academy.bookshop.dto.user.UserResponseDto;
import mate.academy.bookshop.exceptions.RegistrationException;
import mate.academy.bookshop.exceptions.RoleNotFoundException;
import mate.academy.bookshop.mapper.UserMapper;
import mate.academy.bookshop.model.Role;
import mate.academy.bookshop.model.User;
import mate.academy.bookshop.repository.RoleRepository;
import mate.academy.bookshop.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    @Override
    public UserResponseDto register(UserRegistrationRequestDto userRegistrationRequestDto)
            throws RegistrationException {
        if (userRepository.existsByEmail(userRegistrationRequestDto.getEmail())) {
            throw new RegistrationException("Can't register user");
        }
        User user = userMapper.toModel(userRegistrationRequestDto);
        user.setPassword(passwordEncoder.encode(userRegistrationRequestDto.getPassword()));

        Role defaultRole = roleRepository.findByName(Role.RoleName.ROLE_USER)
                .orElseThrow(() -> new RoleNotFoundException("Default role not found"));
        user.setRoles(Set.of(defaultRole));
        userRepository.save(user);
        return userMapper.toUserResponse(user);
    }

    @Transactional
    public void assignRole(String email, Role.RoleName roleName) {
        Optional<User> byEmail = userRepository.findByEmail(email);
        Optional<Role> byName = roleRepository.findByName(roleName);

        if (byEmail.isPresent() && byName.isPresent()) {
            User user = byEmail.get();
            user.getRoles().add(byName.get());
            userRepository.save(user);
        }

    }
}
