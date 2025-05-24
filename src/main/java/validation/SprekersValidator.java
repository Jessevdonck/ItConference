package validation;

import domain.Event;
import domain.Spreker;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.HashSet;
import java.util.Set;

@Component
public class SprekersValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return Event.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Event event = (Event) target;
        Set<Spreker> sprekers = event.getSprekers();

        if (sprekers == null || sprekers.isEmpty()) {
            errors.rejectValue("sprekers", "event.spreker.verplicht", "Je moet minstens 1 spreker opgeven.");
            return;
        }

        // controleer of er dubbele sprekers zijn
        Set<String> uniekeNamen = new HashSet<>();
        for (Spreker spreker : sprekers) {
            String naam = spreker.getNaam().trim().toLowerCase();
            if (!uniekeNamen.add(naam)) {
                errors.rejectValue("sprekers", "event.sprekers.dubbel", "Sprekers moeten unieke namen hebben.");
                break;
            }
        }
    }
}
