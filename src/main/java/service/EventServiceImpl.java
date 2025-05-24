package service;

import domain.Event;
import domain.Lokaal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repository.EventRepository;
import repository.LokaalRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
public class EventServiceImpl implements EventService {

    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private LokaalRepository lokaalRepository;

    @Override
    public List<Event> getAllEventsSorted() {
        return eventRepository.findAllByOrderByDatumAscStartuurAsc();
    }

    @Override
    public Optional<Event> getEventById(Long id) {
        return eventRepository.findById(id);
    }

    @Override
    public boolean bestaatEventMetZelfdeNaamEnDatum(Event event) {
        return eventRepository.existsByNaamAndDatum(event.getNaam(), event.getDatum());
    }

    @Override
    public boolean isLokaalBezet(Long lokaalId, LocalDate datum, LocalTime tijd) {
        return eventRepository.existsByLokaalIdAndDatumAndStartuur(lokaalId, datum, tijd);
    }

    @Override
    public Event bewaarEvent(Event event) {
        return eventRepository.save(event);
    }

    @Override
    public List<Event> getEventsOpDatum(LocalDate datum) {
        return eventRepository.findByDatumOrderByStartuurAsc(datum);
    }

    @Override
    public Lokaal getLokaalById(Long lokaalId) {
        return lokaalRepository.findById(lokaalId)
                .orElseThrow(() -> new IllegalArgumentException("Lokaal niet gevonden"));
    }

    @Override
    public void deleteEvent(Long eventId) {
        eventRepository.deleteById(eventId);
    }

    @Override
    public boolean bestaatEventMetZelfdeNaamEnDatumVoorAndereEvent(Event event) {
        Optional<Event> bestaandEvent = eventRepository.findByNaamAndDatum(event.getNaam(), event.getDatum());
        return bestaandEvent.isPresent() && !bestaandEvent.get().getId().equals(event.getId());
    }

    @Override
    public boolean isLokaalBezet(Long lokaalId, LocalDate datum, LocalTime tijd, Long huidigEventId) {
        Optional<Event> bestaandEvent = eventRepository.findByLokaalIdAndDatumAndStartuur(lokaalId, datum, tijd);
        return bestaandEvent.isPresent() && !bestaandEvent.get().getId().equals(huidigEventId);
    }

}
