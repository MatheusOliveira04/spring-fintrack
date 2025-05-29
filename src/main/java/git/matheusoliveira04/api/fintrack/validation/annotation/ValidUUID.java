package git.matheusoliveira04.api.fintrack.validation.annotation;

import git.matheusoliveira04.api.fintrack.validation.ValidUUIDValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ValidUUIDValidator.class)
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidUUID {
    String message() default "UUID format must be: xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}


