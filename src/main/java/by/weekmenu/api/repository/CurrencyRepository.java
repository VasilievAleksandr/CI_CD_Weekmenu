package by.weekmenu.api.repository;

import by.weekmenu.api.entity.Currency;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CurrencyRepository extends CrudRepository<Currency, Integer> {

    Optional<Currency> findByNameIgnoreCase(String name);

    Optional<Currency> findByCodeIgnoreCase(String code);

    List<Currency> findAllByIsArchivedIsFalse();

    @Modifying
    @Query("update Currency currency set currency.isArchived = true where currency.id = :currencyId")
    void softDelete(@Param("currencyId") Integer currencyId);

    @Modifying
    @Query("update Currency currency set currency.isArchived = false where currency.id = :currencyId")
    void restore(@Param("currencyId") Integer currencyId);
}
