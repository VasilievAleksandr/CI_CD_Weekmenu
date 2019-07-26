package by.weekmenu.api.repository;

import by.weekmenu.api.entity.Subcategory;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SubcategoryRepository extends CrudRepository<Subcategory, Long> {

    Optional<Subcategory> findByNameIgnoreCase(String name);
}
