package repository;

import domain.Spreker;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SprekerRepository extends CrudRepository<Spreker, Long> {
}
