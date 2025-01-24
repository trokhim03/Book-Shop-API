package mate.academy.bookshop.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class IsbnValidator implements ConstraintValidator<Isbn, String> {
    private static final Integer ISBN_10 = 10;
    private static final Integer ISBN_13 = 13;

    @Override
    public boolean isValid(String isbn, ConstraintValidatorContext constraintValidatorContext) {
        if (isbn == null) {
            return false;
        }
        return (isbn.length() == ISBN_10 || isbn.length() == ISBN_13);
    }
}
