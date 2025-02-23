package mate.academy.bookshop.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserLoginRequestDto {
    @NotBlank
    @Email
    @Size(max = 320,
            message = "Email cannot be longer than 320 characters")
    private String email;

    @NotBlank
    @Size(min = 8, max = 64,
            message = "Password must be between 8 and 64 characters")
    private String password;

}
