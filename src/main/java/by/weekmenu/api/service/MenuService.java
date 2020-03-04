package by.weekmenu.api.service;

import by.weekmenu.api.dto.MenuDTO;

public interface MenuService extends CrudService<MenuDTO, Long> {

    void updateMenus(Long recipeId);
    void delete (Long id);
}
