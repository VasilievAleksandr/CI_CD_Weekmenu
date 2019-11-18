package by.weekmenu.api.repository;

import by.weekmenu.api.entity.MenuCategory;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MenuCategoryRepository extends CrudRepository<MenuCategory, Integer> {

    Optional<MenuCategory> findByNameIgnoreCase(String name);
    List<MenuCategory> findAllByIsArchivedIsFalse();
    Optional<MenuCategory> findByPriority (Integer priority);

    @Modifying
    @Query("update MenuCategory e set e.isArchived = true where e.id = :menuCategoryId")
    void softDelete(@Param("menuCategoryId") Integer menuCategoryId);

    @Modifying
    @Query("update MenuCategory menuCategory set menuCategory.isArchived = false " +
            "where menuCategory.id = :menuCategoryId")
    void restore(@Param("menuCategoryId") Integer menuCategoryId);
}
