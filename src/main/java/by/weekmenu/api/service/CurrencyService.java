package by.weekmenu.api.service;

import by.weekmenu.api.dto.CurrencyDto;

import by.weekmenu.api.entity.Currency;

import java.util.List;

public interface CurrencyService {

    List<String> getAllCurrencyCodes();

    List<CurrencyDto> findAllByIsActiveTrueOrderByIsActive();

    List<CurrencyDto> findAllByIsActiveFalseOrderByIsActive();

    Currency findByName(String name);

    Currency findByCode(String code);

    Currency findBySymbol(String symbol);
}
