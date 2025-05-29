package git.matheusoliveira04.api.fintrack.validation;

import git.matheusoliveira04.api.fintrack.validation.annotation.ValidUUID;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.UUID;

public class ValidUUIDValidator implements ConstraintValidator<ValidUUID, String> {

    private ValidUUID acceptedValue;

    @Override
    public void initialize(ValidUUID annotation) {
        acceptedValue = annotation;
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isEmpty()) return false;
        try {
            UUID.fromString(value);
            return true;
        } catch (IllegalArgumentException ex) {
            return false;
        }
    }
}
