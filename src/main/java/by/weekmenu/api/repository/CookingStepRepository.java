package by.weekmenu.api.repository;

import by.weekmenu.api.entity.CookingStep;
import org.springframework.data.repository.CrudRepository;

import java.util.Set;

public interface CookingStepRepository extends CrudRepository<CookingStep, Integer> {

    Set<CookingStep> findAllByRecipe_Id(Long recipeId);
    void deleteAllByRecipe_Id(Long recipeId);
}
