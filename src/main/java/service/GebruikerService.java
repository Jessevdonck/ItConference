package service;

import domain.Gebruiker;

public interface GebruikerService {

    Gebruiker getUserByUsername(String username);
}
