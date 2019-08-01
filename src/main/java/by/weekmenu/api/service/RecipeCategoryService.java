package by.weekmenu.api.service;

import by.weekmenu.api.dto.RecipeCategoryDto;
import by.weekmenu.api.entity.RecipeCategory;

import java.util.List;

public interface RecipeCategoryService extends CrudService<RecipeCategoryDto, Long> {

    RecipeCategory findByName(String name);

    List<String> getAllRecipeCategoryNames();

}