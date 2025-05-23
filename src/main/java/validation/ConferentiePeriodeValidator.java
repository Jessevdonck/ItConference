package validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;

public class ConferentiePeriodeValidator implements ConstraintValidator<ConferentiePeriodeCheck, LocalDate> {

    private final LocalDate START = LocalDate.of(2025, 6, 1);
    private final LocalDate EIND = LocalDate.of(2025, 6, 7);

    @Override
    public boolean isValid(LocalDate value, ConstraintValidatorContext context) {
        if (value == null) return true;
        return !value.isBefore(START) && !value.isAfter(EIND);
    }
}
