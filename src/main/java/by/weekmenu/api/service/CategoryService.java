package by.weekmenu.api.service;

import by.weekmenu.api.dto.CategoryDto;
import by.weekmenu.api.entity.Category;

import java.util.List;

public interface CategoryService extends CrudService<CategoryDto, Long> {

    Category findByName(String name);

    List<String> getAllCategoryNames();

}