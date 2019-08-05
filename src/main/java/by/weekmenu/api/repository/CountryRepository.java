package by.weekmenu.api.repository;

import by.weekmenu.api.entity.Country;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface CountryRepository extends CrudRepository<Country, Long> {

    Optional<Country> findByNameIgnoreCase(String name);
    Optional<Country> findByAlphaCode2IgnoreCase(String alphaCode2);
    List<Country> findAll ();
    List<Country> findAllByCurrency_Id(Integer currencyId);
}
