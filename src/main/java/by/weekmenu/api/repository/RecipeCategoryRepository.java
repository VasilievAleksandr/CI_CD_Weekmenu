package by.weekmenu.api.repository;

import by.weekmenu.api.entity.RecipeCategory;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface RecipeCategoryRepository extends CrudRepository<RecipeCategory, Long> {

    Optional<RecipeCategory> findByNameIgnoreCase(String name);
    List<RecipeCategory> findAll();
}