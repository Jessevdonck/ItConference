package service;

import domain.Event;
import domain.Lokaal;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface EventService {

    List<Event> getAllEventsSorted();

    Optional<Event> getEventById(Long id);

    boolean bestaatEventMetZelfdeNaamEnTijd(Event event);

    boolean isLokaalBezet(Long lokaalId, LocalDateTime tijd);

    Event bewaarEvent(Event event);

    List<Event> getEventsOpDatum(LocalDateTime datum);

    Lokaal getLokaalById(Long lokaalId);

    void deleteEvent(Long eventId);
}
