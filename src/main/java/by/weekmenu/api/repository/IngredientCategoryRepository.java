package by.weekmenu.api.repository;

import by.weekmenu.api.entity.IngredientCategory;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface IngredientCategoryRepository extends CrudRepository<IngredientCategory, Integer> {

    Optional<IngredientCategory> findByNameIgnoreCase(String name);
    List<IngredientCategory> findAllByIsArchivedIsFalse();

    @Modifying
    @Query("update IngredientCategory e set e.isArchived = true where e.id = :ingredientCategoryyId")
    void softDelete(@Param("ingredientCategoryyId") Integer ingredientCategoryyId);

    @Modifying
    @Query("update IngredientCategory ingredientCategory set ingredientCategory.isArchived = false " +
            "where ingredientCategory.id = :ingredientCategoryId")
    void restore(@Param("ingredientCategoryId") Integer ingredientCategoryId);
}
