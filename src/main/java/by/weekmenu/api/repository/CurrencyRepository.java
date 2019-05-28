package by.weekmenu.api.repository;

import by.weekmenu.api.entity.Currency;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CurrencyRepository extends CrudRepository<Currency, Integer> {

    Currency findByCode(String code);

    List<Currency> findAllByIsActiveTrueOrderByCode();
}
