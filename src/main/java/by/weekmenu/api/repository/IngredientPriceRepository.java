package by.weekmenu.api.repository;

import by.weekmenu.api.entity.IngredientPrice;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Set;

public interface IngredientPriceRepository extends CrudRepository<IngredientPrice, IngredientPrice.Id> {

    Set<IngredientPrice> findAllById_IngredientId(Long ingredientId);
    void deleteIngredientPricesById_IngredientId(Long ingredientId);
    List<IngredientPrice> findAllByUnitOfMeasure_Id(Long unitOfMeasureId);
}
