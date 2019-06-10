package by.weekmenu.api.repository;

import by.weekmenu.api.entity.Country;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface CountryRepository extends CrudRepository<Country, Long> {

    Optional<Country> findByNameIgnoreCase(String name);
    Optional<Country> findByAlphaCode2IgnoreCase(String alphaCode2);
}
