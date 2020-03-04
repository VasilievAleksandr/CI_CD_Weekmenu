package by.weekmenu.api.repository;

import by.weekmenu.api.entity.MealType;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

import java.util.Optional;

public interface MealTypeRepository extends CrudRepository<MealType, Short> {

    Optional<MealType> findByNameIgnoreCase(String name);
    List<MealType> findAllByIsArchivedIsFalse();
    Optional<MealType> findByPriority(Integer priority);

    @Modifying
    @Query("update MealType e set e.isArchived = true where e.id = :mealTypeId")
    void softDelete(@Param("mealTypeId") Short mealTypeId);

    @Modifying
    @Query("update MealType mealType set mealType.isArchived = false " +
            "where mealType.id = :mealTypeId")
    void restore(@Param("mealTypeId") Short mealTypeId);

}
