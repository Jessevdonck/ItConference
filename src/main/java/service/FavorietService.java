package service;

import domain.Event;
import domain.Gebruiker;

public interface FavorietService {
    boolean bestaatFavoriet(Gebruiker gebruiker, Event event);
    int aantalFavorieten(Gebruiker gebruiker);
}
