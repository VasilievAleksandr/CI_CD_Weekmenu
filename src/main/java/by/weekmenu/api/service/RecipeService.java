package by.weekmenu.api.service;

import by.weekmenu.api.dto.RecipeDTO;
import by.weekmenu.api.entity.Recipe;

import java.math.BigDecimal;
import java.util.List;

public interface RecipeService extends CrudService<RecipeDTO, Long> {

    Recipe findByName(String name);
    void updateRecipes(Long ingredientId);
    List<String> checkConnectedElements(Long id);
    void delete (Long id);
    List<RecipeDTO> findAllByFilter(String recipeName, Short totalCookingTime,
                                    String recipeCategoryName, String recipeSubcategoryName,
                                    BigDecimal recipeCalories);
}
