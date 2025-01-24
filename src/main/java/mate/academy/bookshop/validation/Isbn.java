package mate.academy.bookshop.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = IsbnValidator.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Isbn {
    String message() default "ISBN must be 10 or 13 characters";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
