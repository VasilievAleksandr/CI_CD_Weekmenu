package by.weekmenu.api.repository;

import by.weekmenu.api.entity.RecipeSubcategory;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface RecipeSubcategoryRepository extends CrudRepository<RecipeSubcategory, Long> {

    Optional<RecipeSubcategory> findByNameIgnoreCase(String name);
    List<RecipeSubcategory> findAll();
}