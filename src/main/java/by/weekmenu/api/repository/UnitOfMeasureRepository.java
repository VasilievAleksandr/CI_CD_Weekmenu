package by.weekmenu.api.repository;

import by.weekmenu.api.entity.UnitOfMeasure;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UnitOfMeasureRepository extends CrudRepository<UnitOfMeasure, Long> {

    Optional<UnitOfMeasure> findByShortNameIgnoreCase(String shortName);
    Optional<UnitOfMeasure> findByFullNameIgnoreCase(String fullName);

    List<UnitOfMeasure> findAllByIsArchivedIsFalse();

    @Modifying
    @Query("update UnitOfMeasure unitOfMeasure set unitOfMeasure.isArchived = true where unitOfMeasure.id = :unitOfMeasureId")
    void softDelete(@Param("unitOfMeasureId") Long unitOfMeasureId);

    @Modifying
    @Query("update UnitOfMeasure unitOfMeasure set unitOfMeasure.isArchived = false where unitOfMeasure.id = :unitOfMeasureId")
    void restore(@Param("unitOfMeasureId") Long unitOfMeasureId);
}
