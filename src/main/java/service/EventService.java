package service;

import domain.Event;
import domain.Lokaal;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface EventService {

    List<Event> getAllEventsSorted();

    Optional<Event> getEventById(Long id);

    boolean bestaatEventMetZelfdeNaamEnDatum(Event event);

    boolean isLokaalBezet(Long lokaalId, LocalDate datum, LocalTime tijd);

    Event bewaarEvent(Event event);

    List<Event> getEventsOpDatum(LocalDate datum);

    Lokaal getLokaalById(Long lokaalId);

    void deleteEvent(Long eventId);

    boolean bestaatEventMetZelfdeNaamEnDatumVoorAndereEvent(Event event);

    public boolean isLokaalBezet(Long lokaalId, LocalDate datum, LocalTime tijd, Long huidigEventId);
}
