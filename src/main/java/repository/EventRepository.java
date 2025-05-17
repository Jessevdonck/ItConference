package repository;

import domain.Event;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EventRepository extends CrudRepository<Event, Long> {

    List<Event> findAllByOrderByDatumTijdAsc();

    @Query("SELECT e FROM Event e WHERE e.datumTijd >= :start AND e.datumTijd < :end")
    List<Event> findByDatum(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);


    boolean existsByNaamAndDatumTijd(String naam, LocalDateTime datumTijd);

    boolean existsByLokaalIdAndDatumTijd(Long lokaalId, LocalDateTime datumTijd);
}
