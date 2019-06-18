package by.weekmenu.api.repository;

import by.weekmenu.api.entity.BaseUnitOfMeasure;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface BaseUnitOfMeasureRepository extends CrudRepository<BaseUnitOfMeasure, Long> {

    Optional<BaseUnitOfMeasure> findByShortNameIgnoreCase(String shortName);
    Optional<BaseUnitOfMeasure> findByFullNameIgnoreCase(String fullName);
}
