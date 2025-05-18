package repository;

import domain.Event;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface EventRepository extends CrudRepository<Event, Long> {

    List<Event> findAllByOrderByDatumAscStartuurAsc();

    List<Event> findByDatumOrderByStartuurAsc(LocalDate datum);

    boolean existsByNaamAndDatumAndStartuur(String naam, LocalDate datum, LocalTime startuur);

    boolean existsByLokaalIdAndDatumAndStartuur(Long lokaalId, LocalDate datum, LocalTime startuur);

    boolean existsByLokaal_Id(Long lokaalId);
}
