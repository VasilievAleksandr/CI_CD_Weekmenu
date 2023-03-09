package by.weekmenu.api.repository;

import by.weekmenu.api.entity.RecipePrice;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface RecipePriceRepository extends CrudRepository<RecipePrice, RecipePrice.Id> {

    void deleteById_RecipeId(Long recipeId);
    List<RecipePrice> findAllById_RecipeId(Long recipeId);
    List<RecipePrice> findAllById_RegionId(Long regionId);
}
