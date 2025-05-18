package service;

import domain.Lokaal;

import java.util.List;
import java.util.Optional;

public interface LokaalService {

    List<Lokaal> getAlleLokalen();

    Optional<Lokaal> getLokaalById(Long id);

    boolean bestaatLokaalMetNaam(String naam);

    Lokaal bewaarLokaal(Lokaal lokaal);

    void verwijderLokaal(Long id);
}