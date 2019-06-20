package by.weekmenu.api.repository;

import by.weekmenu.api.entity.IngredientUnitOfMeasure;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface IngredientUnitOfMeasureRepository extends CrudRepository<IngredientUnitOfMeasure, IngredientUnitOfMeasure.Id> {

    List<IngredientUnitOfMeasure> findAllById_IngredientId(Long ingredientId);
    void deleteIngredientUnitOfMeasuresById_IngredientId(Long ingredientId);
}
