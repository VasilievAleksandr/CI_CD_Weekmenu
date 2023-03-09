package by.weekmenu.api.repository;

import by.weekmenu.api.entity.Country;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CountryRepository extends CrudRepository<Country, Long> {

    Optional<Country> findByNameIgnoreCase(String name);
    Optional<Country> findByAlphaCode2IgnoreCase(String alphaCode2);
    List<Country> findAllByIsArchivedIsFalse();
    List<Country> findAllByCurrency_Id(Integer currencyId);

    @Modifying
    @Query("update Country country set country.isArchived = true where country.id = :countryId")
    void softDelete(@Param("countryId") Long countryId);

    @Modifying
    @Query("update Country country set country.isArchived = false where country.id = :countryId")
    void restore(@Param("countryId") Long countryId);
}
