package by.weekmenu.api.repository;

import by.weekmenu.api.entity.MenuPrice;
import org.springframework.data.repository.CrudRepository;

public interface MenuPriceRepository extends CrudRepository<MenuPrice, MenuPrice.Id> {

    void deleteAllById_MenuId(Long menuId);
}
