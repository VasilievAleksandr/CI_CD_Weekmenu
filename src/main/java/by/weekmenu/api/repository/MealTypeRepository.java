package by.weekmenu.api.repository;

import by.weekmenu.api.entity.DishType;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface MealTypeRepository extends CrudRepository<MealType, Short> {

    Optional<MealType> findByNameIgnoreCase(String name);
}
