package by.weekmenu.api.service;

import by.weekmenu.api.dto.IngredientDto;
import by.weekmenu.api.entity.Ingredient;

import java.util.List;

public interface IngredientService extends CrudService<IngredientDto, Long> {

    Ingredient findByName(String name);
    List<String> findAllUnitsOfMeasure(String name);
    List<String> checkConnectedElements(Long id);
    void moveToRecycleBin(IngredientDto ingredientDto);
}
