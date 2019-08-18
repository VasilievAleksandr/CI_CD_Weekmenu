package by.weekmenu.api.service;

import by.weekmenu.api.dto.RecipeSubcategoryDTO;
import by.weekmenu.api.entity.RecipeSubcategory;

import java.util.List;

public interface RecipeSubcategoryService extends CrudService<RecipeSubcategoryDTO, Long> {

    RecipeSubcategory findByName(String name);
    List<String> checkConnectedElements(Long id);
    void moveToRecycleBin(RecipeSubcategoryDTO recipeSubcategoryDTO);
}