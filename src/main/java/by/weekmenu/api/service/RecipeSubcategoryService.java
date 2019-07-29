package by.weekmenu.api.service;

import by.weekmenu.api.dto.RecipeSubcategoryDto;
import by.weekmenu.api.entity.RecipeSubcategory;

import java.util.List;

public interface RecipeSubcategoryService extends CrudService<RecipeSubcategoryDto, Long> {

    RecipeSubcategory findByName(String name);

    List<String> getAllRecipeSubcategoryNames();

}