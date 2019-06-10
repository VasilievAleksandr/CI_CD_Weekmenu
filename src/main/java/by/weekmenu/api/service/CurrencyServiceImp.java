package by.weekmenu.api.service;

import by.weekmenu.api.dto.CurrencyDto;
import by.weekmenu.api.entity.Currency;
import by.weekmenu.api.repository.CurrencyRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Transactional(readOnly = true)
public class CurrencyServiceImp implements CrudService<CurrencyDto, Integer>, CurrencyService {

    private final CurrencyRepository currencyRepository;
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public CurrencyDto save(CurrencyDto entityDto) {
        return convertToDto(currencyRepository.save(convertToEntity(entityDto)));
    }

    @Override
    public CurrencyDto findById(Integer id) {
        return convertToDto(currencyRepository.findById(id).orElse(null));
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        currencyRepository.deleteById(id);
    }

    @Override
    public List<CurrencyDto> findAll() {
        List<Currency> list = new ArrayList<>();
        currencyRepository.findAll().forEach(list::add);

        return list.stream()
                .filter(Objects::nonNull)
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    @Override
    public List<String> getAllCurrencyCodes() {
        return currencyRepository.findAllByIsActiveTrueOrderByCode()
                .stream()
                .filter(Objects::nonNull)
                .map(Currency::getCode)
                .collect(Collectors.toList());
    }

    @Override
    public Currency findBySymbol(String symbol) {
        return currencyRepository.findBySymbolIgnoreCase(symbol).orElse(null);
    }

    @Override
    public List<CurrencyDto> findAllByIsActiveTrueOrderByIsActive() {
        return currencyRepository.findAllByIsActiveTrueOrderByIsActive()
                .stream()
                .filter(Objects::nonNull)
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<CurrencyDto> findAllByIsActiveFalseOrderByIsActive() {
        return currencyRepository.findAllByIsActiveFalseOrderByIsActive()
                .stream()
                .filter(Objects::nonNull)
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public Currency findByName(String name) {
        return currencyRepository.findByNameIgnoreCase(name).orElse(null);
    }

    @Override
    public Currency findByCode(String code) {
        return currencyRepository.findByCodeIgnoreCase(code).orElse(null);
    }

    private Currency convertToEntity(CurrencyDto currencyDto) {
        return modelMapper.map(currencyDto, Currency.class);
    }

    private CurrencyDto convertToDto(Currency currency) {
        return modelMapper.map(currency, CurrencyDto.class);
    }
}
