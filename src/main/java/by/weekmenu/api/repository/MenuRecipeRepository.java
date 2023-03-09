package by.weekmenu.api.repository;

import by.weekmenu.api.entity.MenuRecipe;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface MenuRecipeRepository extends CrudRepository<MenuRecipe, Long> {

    List<MenuRecipe> findAllByRecipe_Id(Long recipeId);
    List<MenuRecipe> findAllByMenu_Id(Long menuId);
    List<MenuRecipe> findAllByMealType_Id(Short id);
    void deleteAllByMenu_Id(Long id);
}
