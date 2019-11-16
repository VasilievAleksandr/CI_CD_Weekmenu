package by.weekmenu.api.service;

import by.weekmenu.api.dto.IngredientDTO;
import by.weekmenu.api.entity.Ingredient;

import java.util.List;

public interface IngredientService extends CrudService<IngredientDTO, Long> {

    Ingredient findByName(String name);
    List<String> findAllUnitsOfMeasure(String name);
    List<String> checkConnectedElements(Long id);
    void delete(Long id);
    List<IngredientDTO> findIngredientByName(String name);
}
