package by.weekmenu.api.repository;

import by.weekmenu.api.entity.Ingredient;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface IngredientRepository extends CrudRepository<Ingredient, Long> {

    Optional<Ingredient> findByNameIgnoreCase(String name);
}
