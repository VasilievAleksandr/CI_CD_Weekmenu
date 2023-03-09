package by.weekmenu.api.repository;

import by.weekmenu.api.entity.MenuPrice;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface MenuPriceRepository extends CrudRepository<MenuPrice, MenuPrice.Id> {

    void deleteAllById_MenuId(Long menuId);
    List<MenuPrice> findAllById_MenuId(Long menuId);
}
