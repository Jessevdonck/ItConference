package service;

import domain.Event;
import domain.Gebruiker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repository.FavorietRepository;

@Service
public class FavorietServiceImpl implements FavorietService {

    @Autowired
    private FavorietRepository favorietRepository;

    @Override
    public boolean bestaatFavoriet(Gebruiker gebruiker, Event event) {
        return favorietRepository.existsByGebruikerIdAndEventId(gebruiker.getId(), event.getId());
    }

    @Override
    public int aantalFavorieten(Gebruiker gebruiker) {
        return (int) favorietRepository.countByGebruikerId(gebruiker.getId());
    }
}
