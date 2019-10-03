package by.weekmenu.api.repository;

import by.weekmenu.api.entity.MenuRecipe;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface MenuRecipeRepository extends CrudRepository<MenuRecipe, MenuRecipe.Id> {

    List<MenuRecipe> findAllById_RecipeId(Long recipeId);
    List<MenuRecipe> findAllByMealType_Id (Short mealTypeId);
}
