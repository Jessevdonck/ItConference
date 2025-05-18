package validation;

import domain.Event;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class BeamerCheckValidator implements ConstraintValidator<ValidBeamerCheck, Event> {

    @Override
    public boolean isValid(Event event, ConstraintValidatorContext context) {
        if (event.getBeamerCode() == null || event.getBeamercheck() == null) {
            return true;
        }

        try {
            int code = Integer.parseInt(event.getBeamerCode());
            int check = Integer.parseInt(event.getBeamercheck());
            if (code % 97 == check) {
                return true;
            }
        } catch (NumberFormatException ignored) {}

        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(context.getDefaultConstraintMessageTemplate())
                .addPropertyNode("beamercheck")
                .addConstraintViolation();
        return false;
    }
}
