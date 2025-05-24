package validation;

import domain.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import service.EventService;

@Component
public class EventValidator implements Validator {

    @Autowired
    private EventService eventService;

    @Override
    public boolean supports(Class<?> clazz) {
        return Event.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Event event = (Event) target;

        if (event.getId() != null) {
            if (eventService.bestaatEventMetZelfdeNaamEnDatumVoorAndereEvent(event)) {
                errors.rejectValue("naam", "event.naam.datum.bestaat", "Dit evenement gaat al door vandaag");
            }
        } else {
            if (eventService.bestaatEventMetZelfdeNaamEnDatum(event)) {
                errors.rejectValue("naam", "event.naam.datum.bestaat", "Dit evenement gaat al door vandaag");
            }
        }

        // Check lokaal bezet
        if (event.getLokaal() != null && eventService.isLokaalBezet(
                event.getLokaal().getId(), event.getDatum(), event.getStartuur(), event.getId())) {
            errors.rejectValue("startuur", "event.bestaat", "Er bestaat al een evenement op dit tijdstip");
        }
    }

}
