package by.weekmenu.api.repository;

import by.weekmenu.api.entity.Ingredient;
import by.weekmenu.api.entity.IngredientCategory;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface IngredientRepository extends CrudRepository<Ingredient, Long> {

    Optional<Ingredient> findByNameIgnoreCase(String name);
    List<Ingredient> findAllByIsArchivedIsFalse();
    List<Ingredient> findAllByNameContainingIgnoreCaseAndIsArchivedIsFalse(String name);
    List<Ingredient> findAllByIngredientCategory_IdAndIsArchivedIsFalse(Integer ingredientCategoryId);

    @Modifying
    @Query("update Ingredient ingredient set ingredient.isArchived = true where ingredient.id = :ingredientId")
    void softDelete(@Param("ingredientId") Long ingredientId);

    @Modifying
    @Query("update Ingredient ingredient set ingredient.isArchived = false where ingredient.id = :ingredientId")
    void restore(@Param("ingredientId") Long ingredientId);
}
