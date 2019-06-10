package by.weekmenu.api.repository;

import by.weekmenu.api.entity.UnitOfMeasure;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UnitOfMeasureRepository extends CrudRepository<UnitOfMeasure, Long> {

    Optional<UnitOfMeasure> findByShortNameIgnoreCase(String shortName);
    Optional<UnitOfMeasure> findByFullNameIgnoreCase(String fullName);
}
