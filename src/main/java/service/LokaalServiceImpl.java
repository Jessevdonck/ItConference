package service;

import domain.Lokaal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import repository.EventRepository;
import repository.LokaalRepository;

import java.util.List;
import java.util.Optional;

@Service
public class LokaalServiceImpl implements LokaalService {

    @Autowired
    private LokaalRepository lokaalRepository;

    @Autowired
    EventRepository eventRepository;

    @Autowired
    MessageSource messageSource;

    @Override
    public List<Lokaal> getAlleLokalen() {
        return (List<Lokaal>) lokaalRepository.findAll();
    }

    @Override
    public Optional<Lokaal> getLokaalById(Long id) {
        return lokaalRepository.findById(id);
    }

    @Override
    public boolean bestaatLokaalMetNaam(String naam) {
        return lokaalRepository.existsByNaam(naam);
    }

    @Override
    public Lokaal bewaarLokaal(Lokaal lokaal) {
        return lokaalRepository.save(lokaal);
    }

    @Override
    public void verwijderLokaal(Long id) {
        if (eventRepository.existsByLokaal_Id(id)) {
            String msg = messageSource.getMessage("lokaal.evenement.gekoppeld", null, LocaleContextHolder.getLocale());
            throw new IllegalStateException(msg);
        }

        lokaalRepository.deleteById(id);
    }
}