package repository;

import domain.Gebruiker;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GebruikerRepository extends CrudRepository<Gebruiker, Long> {
    Gebruiker findByGebruikersnaam(String gebruikersnaam);
}
