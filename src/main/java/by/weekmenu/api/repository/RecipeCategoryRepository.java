package by.weekmenu.api.repository;

import by.weekmenu.api.entity.RecipeCategory;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RecipeCategoryRepository extends CrudRepository<RecipeCategory, Long> {

    Optional<RecipeCategory> findByNameIgnoreCase(String name);
    List<RecipeCategory> findAllByIsArchivedIsFalse();

    @Modifying
    @Query("update RecipeCategory e set e.isArchived = true where e.id = :recipeCategoryId")
    void softDelete(@Param("recipeCategoryId") Long recipeCategoryId);

    @Modifying
    @Query("update RecipeCategory recipeCategory set recipeCategory.isArchived = false " +
            "where recipeCategory.id = :recipeCategoryId")
    void restore(@Param("recipeCategoryId") Long recipeCategoryId);
}