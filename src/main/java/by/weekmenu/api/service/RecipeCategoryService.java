package by.weekmenu.api.service;

import by.weekmenu.api.dto.RecipeCategoryDTO;
import by.weekmenu.api.entity.RecipeCategory;

import java.util.List;

public interface RecipeCategoryService extends CrudService<RecipeCategoryDTO, Long> {

    RecipeCategory findByName(String name);

    List<String> getAllRecipeCategoryNames();

}