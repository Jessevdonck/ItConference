package service;

import domain.Gebruiker;
import exceptions.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repository.GebruikerRepository;

@Service
public class GebruikerServiceImpl implements GebruikerService{

    @Autowired
    GebruikerRepository gebruikerRepository;

    @Override
    public Gebruiker getUserByUsername(String username) {
        Gebruiker user = gebruikerRepository.findByGebruikersnaam(username);
        if (user == null) {
            throw new UserNotFoundException();
        }
        return user;
    }
}