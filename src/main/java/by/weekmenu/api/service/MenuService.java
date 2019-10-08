package by.weekmenu.api.service;

import by.weekmenu.api.dto.MenuDTO;

import java.util.List;

public interface MenuService extends CrudService<MenuDTO, Long> {

    void updateMenus(Long recipeId);
    void delete (Long id);
    List<String> checkConnectedElements(Long id);
}
