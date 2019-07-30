package by.weekmenu.api.repository;

import by.weekmenu.api.entity.CookingMethod;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface CookingMethodRepository extends CrudRepository<CookingMethod, Integer> {

    Optional<CookingMethod> findByNameIgnoreCase(String name);
}
