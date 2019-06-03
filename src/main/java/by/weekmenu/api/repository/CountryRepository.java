package by.weekmenu.api.repository;

import by.weekmenu.api.entity.Country;
import org.springframework.data.repository.CrudRepository;

public interface CountryRepository extends CrudRepository<Country, Long> {

    Country findByNameIgnoreCase(String name);
}
