package by.weekmenu.api.repository;

import by.weekmenu.api.entity.CookingMethod;
import by.weekmenu.api.entity.Recipe;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RecipeRepository extends CrudRepository<Recipe, Long> {

    Optional<Recipe> findByNameIgnoreCase(String name);
    List<Recipe> findAllByIsArchivedIsFalse();
    List<Recipe> findAllByCookingMethod(CookingMethod cookingMehtod);

    @Modifying
    @Query("update Recipe recipe set recipe.isArchived = true where recipe.id = :recipeId")
    void softDelete(@Param("recipeId") Long recipeId);

    @Modifying
    @Query("update Recipe recipe set recipe.isArchived = false where recipe.id = :recipeId")
    void restore(@Param("recipeId") Long recipeId);

    @Query("select recipe from Recipe recipe " +
            "where ( :recipeName is null or lower(recipe.name) like %:recipeName%) " +
            "and ( :totalCookingTime is null or (recipe.cookingTime + recipe.preparingTime) <= :totalCookingTime)")
    List<Recipe> findAllByFilter(@Param("recipeName") String recipeName,
                                 @Param("totalCookingTime") Short totalCookingTime);

    @Query("select recipe from Recipe recipe " +
            "join recipe.recipeCategories recipeCategories " +
            "where recipeCategories.name = :recipeCategoryName")
    List<Recipe> findAllByRecipeCategoryName(@Param("recipeCategoryName") String recipeCategoryName);

    @Query("select recipe from Recipe recipe " +
            "join  recipe.recipeSubcategories recipeSubcategories " +
            "where recipeSubcategories.name = :recipeSubcategoryName")
    List<Recipe> findAllByRecipeSubcategoryName(@Param("recipeSubcategoryName") String recipeSubcategoryName);
}
