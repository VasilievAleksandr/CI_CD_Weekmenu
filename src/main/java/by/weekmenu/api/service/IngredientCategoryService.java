package by.weekmenu.api.service;

import by.weekmenu.api.dto.IngredientCategoryDTO;
import by.weekmenu.api.entity.IngredientCategory;

import java.util.List;

public interface IngredientCategoryService extends CrudService<IngredientCategoryDTO, Integer> {

    IngredientCategory findByName(String name);
    List<String> checkConnectedElements(Integer id);
    IngredientCategory findByPriority (Integer priority);

}
