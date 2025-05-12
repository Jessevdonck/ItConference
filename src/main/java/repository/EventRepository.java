package repository;

import domain.Event;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EventRepository extends CrudRepository<Event, Long> {

    List<Event> findAllByOrderByDatumTijdAsc();

    List<Event> findByDatumTijd(LocalDateTime datumTijd);

    boolean existsByNaamAndDatumTijd(String naam, LocalDateTime datumTijd);

    boolean existsByLokaalIdAndDatumTijd(Long lokaalId, LocalDateTime datumTijd);
}
