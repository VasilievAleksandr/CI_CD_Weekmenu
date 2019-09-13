package by.weekmenu.api.repository;

import by.weekmenu.api.entity.Menu;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface MenuRepository extends CrudRepository<Menu, Long> {

      List<Menu> findAllByMenuCategory_Id(Integer menuCategoryId);

}
