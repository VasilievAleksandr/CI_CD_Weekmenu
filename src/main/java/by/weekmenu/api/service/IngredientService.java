package by.weekmenu.api.service;

import by.weekmenu.api.dto.IngredientDto;
import by.weekmenu.api.entity.Ingredient;

public interface IngredientService extends CrudService<IngredientDto, Long> {

    Ingredient findByName(String name);
}
