package repository;

import domain.Favoriet;
import domain.Gebruiker;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FavorietRepository extends CrudRepository<Favoriet, Long> {
    List<Favoriet> findByGebruikerOrderByEvent_DatumAscEvent_StartuurAscEvent_NaamAsc(Gebruiker gebruiker);

    boolean existsByGebruikerIdAndEventId(Long gebruikerId, Long eventId);

    long countByGebruikerId(Long gebruikerId);
}
