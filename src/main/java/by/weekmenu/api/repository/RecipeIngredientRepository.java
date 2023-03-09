package by.weekmenu.api.repository;

import by.weekmenu.api.entity.RecipeIngredient;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Set;

public interface RecipeIngredientRepository extends CrudRepository<RecipeIngredient, RecipeIngredient.Id> {

    Set<RecipeIngredient> findAllById_RecipeId(Long recipeId);
    void deleteById_RecipeId(Long recipeId);
    Set<RecipeIngredient> findAllById_IngredientId(Long ingredientId);
    List<RecipeIngredient> findAllByUnitOfMeasure_Id(Long unitOfMeasureId);
}
