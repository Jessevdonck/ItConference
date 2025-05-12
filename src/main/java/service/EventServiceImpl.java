package service;

import domain.Event;
import domain.Lokaal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repository.EventRepository;
import repository.LokaalRepository;

import java.time.LocalDateTime;
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
        return eventRepository.findAllByOrderByDatumTijdAsc();
    }

    @Override
    public Optional<Event> getEventById(Long id) {
        return eventRepository.findById(id);
    }

    @Override
    public boolean bestaatEventMetZelfdeNaamEnTijd(Event event) {
        return true;//eventRepository.existsByNaamAndDatumTijd(event.getNaam(), event.getDatumTijd());
    }

    @Override
    public boolean isLokaalBezet(Long lokaalId, LocalDateTime tijd) {
        return eventRepository.existsByLokaalIdAndDatumTijd(lokaalId, tijd);
    }

    @Override
    public Event bewaarEvent(Event event) {
        return eventRepository.save(event);
    }

    @Override
    public List<Event> getEventsOpDatum(LocalDateTime datum) {
        return eventRepository.findByDatumTijd(datum);
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
}
