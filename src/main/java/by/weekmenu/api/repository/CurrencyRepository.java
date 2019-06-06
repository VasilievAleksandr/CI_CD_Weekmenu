package by.weekmenu.api.repository;

import by.weekmenu.api.entity.Currency;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface CurrencyRepository extends CrudRepository<Currency, Integer> {

    Currency findByCode(String code);

    List<Currency> findAllByIsActiveTrueOrderByCode();

    List<Currency> findAllByIsActiveTrueOrderByIsActive();

    List<Currency> findAllByIsActiveFalseOrderByIsActive();

    Optional<Currency> findByNameIgnoreCase(String name);

    Optional<Currency> findByCodeIgnoreCase(String code);

    Optional<Currency> findBySymbolIgnoreCase(String symbol);
}
