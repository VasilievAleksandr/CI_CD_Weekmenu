package by.weekmenu.api.repository;

import by.weekmenu.api.entity.Recipe;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface RecipeRepository extends CrudRepository<Recipe, Long> {

    Optional<Recipe> findByNameIgnoreCase(String name);
}
