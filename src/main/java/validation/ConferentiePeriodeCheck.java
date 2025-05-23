package validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ConferentiePeriodeValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ConferentiePeriodeCheck {
    String message() default "{event.datum.conferentieperiode}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
