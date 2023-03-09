package by.weekmenu.api.repository;

import by.weekmenu.api.entity.RecipeSubcategory;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RecipeSubcategoryRepository extends CrudRepository<RecipeSubcategory, Long> {

    Optional<RecipeSubcategory> findByNameIgnoreCase(String name);
    List<RecipeSubcategory> findAllByIsArchivedIsFalse();

    @Modifying
    @Query("update RecipeSubcategory e set e.isArchived = true where e.id = :recipeSubcategoryId")
    void softDelete(@Param("recipeSubcategoryId") Long recipeSubcategoryId);

    @Modifying
    @Query("update RecipeSubcategory e set e.isArchived = false where e.id = :recipeSubcategoryId")
    void restore(@Param("recipeSubcategoryId") Long recipeSubcategoryId);
}