package repository;

import domain.Event;
import domain.Favoriet;
import domain.Gebruiker;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FavorietRepository extends CrudRepository<Favoriet, Long> {
    List<Favoriet> findByGebruikerOrderByEvent_DatumAscEvent_StartuurAscEvent_NaamAsc(Gebruiker gebruiker);

    boolean existsByGebruikerIdAndEventId(Long gebruikerId, Long eventId);

    long countByGebruikerId(Long gebruikerId);
    Optional<Favoriet> findByGebruikerAndEvent(Gebruiker gebruiker, Event event);

}
