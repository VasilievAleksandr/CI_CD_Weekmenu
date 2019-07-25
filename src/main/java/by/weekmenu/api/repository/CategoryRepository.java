package by.weekmenu.api.repository;

import by.weekmenu.api.entity.Category;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends CrudRepository<Category, Long> {

    Optional<Category> findByNameIgnoreCase(String name);
    List<Category> findAll ();
}