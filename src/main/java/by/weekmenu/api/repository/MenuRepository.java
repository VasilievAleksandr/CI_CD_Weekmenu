package by.weekmenu.api.repository;

import by.weekmenu.api.entity.Menu;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MenuRepository extends CrudRepository<Menu, Long> {

      List<Menu> findAllByMenuCategory_IdAndIsArchivedIsFalse(Integer menuCategoryId);
      List<Menu> findAllByIsArchivedIsFalse();
      List<Menu> findAllByIsActiveIsFalse();
      Optional<Menu> findByNameIgnoreCase(String name);

      @Modifying
      @Query("update Menu menu set menu.isArchived = true where menu.id = :menuId")
      void softDelete(@Param("menuId") Long menuId);

      @Modifying
      @Query("update Menu menu set menu.isArchived = false where menu.id = :menuId")
      void restore(@Param("menuId") Long menuId);
}
