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
    @Query("update RecipeCategory e set e.isArchived = true where e.id = :recipecategoryId")
    void softDelete(@Param("recipecategoryId") Long recipecategoryId);

    @Modifying
    @Query("update RecipeCategory recipeCategory set recipeCategory.isArchived = false " +
            "where recipeCategory.id = :recipeCategoryId")
    void restore(@Param("recipeCategoryId") Long recipeCategoryId);
}