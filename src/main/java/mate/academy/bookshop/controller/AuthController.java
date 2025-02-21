package mate.academy.bookshop.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mate.academy.bookshop.dto.user.UserLoginRequestDto;
import mate.academy.bookshop.dto.user.UserLoginResponseDto;
import mate.academy.bookshop.dto.user.UserRegistrationRequestDto;
import mate.academy.bookshop.dto.user.UserResponseDto;
import mate.academy.bookshop.exceptions.RegistrationException;
import mate.academy.bookshop.security.AuthenticationService;
import mate.academy.bookshop.service.user.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication",
        description = "Operations related to user authentication and registration")
public class AuthController {
    private final UserService userService;
    private final AuthenticationService authenticationService;

    @PostMapping ("/registration")
    @Operation(summary = "User Registration",
            description = "Registers a new user with the provided registration details.")
    public UserResponseDto registration(@RequestBody @Valid UserRegistrationRequestDto requestDto)
            throws RegistrationException {
        return userService.register(requestDto);
    }

    @PostMapping("/login")
    @Operation(summary = "User Login",
            description = "Authenticates a user with the provided login credentials"
                    + " and returns an access token."
    )
    public UserLoginResponseDto login(@RequestBody @Valid UserLoginRequestDto requestDto) {
        return authenticationService.authenticate(requestDto);
    }
}
