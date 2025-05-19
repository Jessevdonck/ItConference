package service;

import domain.Event;
import domain.Favoriet;
import domain.Gebruiker;

import java.util.List;

public interface FavorietService {
    boolean bestaatFavoriet(Gebruiker gebruiker, Event event);
    int aantalFavorieten(Gebruiker gebruiker);
    List<Favoriet> getFavorietenVoorGebruiker(Gebruiker gebruiker);
    void voegFavorietToe(Gebruiker gebruiker, Event event);
    void verwijderFavoriet(Gebruiker gebruiker, Event event);

}
