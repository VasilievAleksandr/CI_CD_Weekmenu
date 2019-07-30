package by.weekmenu.api.service;

import by.weekmenu.api.dto.RecipeDTO;
import by.weekmenu.api.entity.Recipe;

public interface RecipeService extends CrudService<RecipeDTO, Long> {

    Recipe findByName(String name);
    void updateRecipes(Long ingredientId);
}
