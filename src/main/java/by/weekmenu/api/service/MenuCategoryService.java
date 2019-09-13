package by.weekmenu.api.service;

import by.weekmenu.api.dto.MenuCategoryDTO;
import by.weekmenu.api.entity.MenuCategory;
import java.util.List;

public interface MenuCategoryService extends CrudService<MenuCategoryDTO, Integer> {

    MenuCategory findByName(String name);
    List<String> checkConnectedElements(Integer id);

}
