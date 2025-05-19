package service;

import domain.Event;
import domain.Favoriet;
import domain.Gebruiker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repository.FavorietRepository;

import java.util.List;

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

    @Override
    public List<Favoriet> getFavorietenVoorGebruiker(Gebruiker gebruiker) {
        return favorietRepository.findByGebruikerOrderByEvent_DatumAscEvent_StartuurAscEvent_NaamAsc(gebruiker);
    }

    @Override
    public void voegFavorietToe(Gebruiker gebruiker, Event event){
        Favoriet favoriet = new Favoriet();
        favoriet.setGebruiker(gebruiker);
        favoriet.setEvent(event);
        favorietRepository.save(favoriet);
    }

    @Override
    public void verwijderFavoriet(Gebruiker gebruiker, Event event){
        favorietRepository.findByGebruikerAndEvent(gebruiker, event).ifPresent(favorietRepository::delete);
    }
}
