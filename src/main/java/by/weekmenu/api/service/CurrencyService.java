package by.weekmenu.api.service;

import by.weekmenu.api.dto.CurrencyDTO;
import by.weekmenu.api.entity.Currency;

import java.util.List;

public interface CurrencyService extends CrudService<CurrencyDTO, Integer> {

    Currency findByName(String name);
    Currency findByCode(String code);
    List<String> checkConnectedElements(Integer id);
    void moveToRecycleBin(CurrencyDTO currencyDTO);
}
