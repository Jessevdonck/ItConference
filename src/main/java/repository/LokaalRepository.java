package repository;

import domain.Lokaal;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LokaalRepository extends CrudRepository<Lokaal, Long> {
    boolean existsByNaam(String naam);
}
