package validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = BeamerCheckValidator.class)
@Documented
public @interface ValidBeamerCheck {
    String message() default "Beamercheck komt niet overeen met de beamercode.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
