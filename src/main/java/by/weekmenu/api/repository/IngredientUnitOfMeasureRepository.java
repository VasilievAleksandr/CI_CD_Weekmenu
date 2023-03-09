package by.weekmenu.api.repository;

import by.weekmenu.api.entity.IngredientUnitOfMeasure;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface IngredientUnitOfMeasureRepository extends CrudRepository<IngredientUnitOfMeasure, IngredientUnitOfMeasure.Id> {

    List<IngredientUnitOfMeasure> findAllById_IngredientId(Long ingredientId);
    void deleteIngredientUnitOfMeasuresById_IngredientId(Long ingredientId);

    Optional<IngredientUnitOfMeasure> findById_IngredientIdAndId_UnitOfMeasureId(Long ingredientId, Long unitOfMeasureId);
    List<IngredientUnitOfMeasure> findAllById_UnitOfMeasureId(Long unitOfMeasureId);
}
