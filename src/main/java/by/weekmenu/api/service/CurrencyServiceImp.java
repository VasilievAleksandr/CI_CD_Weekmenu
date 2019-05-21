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
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Transactional(readOnly = true)
public class CurrencyServiceImp implements CrudService<CurrencyDto, Byte> {

    private final CurrencyRepository currencyRepository;
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public CurrencyDto save(CurrencyDto entityDto) {
        return convertToDto(currencyRepository.save(convertToEntity(entityDto)));
    }

    @Override
    public CurrencyDto findById(Byte id) {
        return convertToDto(currencyRepository.findById(id).orElse(null));
    }

    @Override
    @Transactional
    public void delete(Byte id) {
        currencyRepository.deleteById(id);
    }

    @Override
    public List<CurrencyDto> findAll() {
        List<Currency> list = new ArrayList<>();
        currencyRepository.findAll().forEach(list::add);

        return list.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private Currency convertToEntity(CurrencyDto currencyDto) {
        return modelMapper.map(currencyDto, Currency.class);
    }

    private CurrencyDto convertToDto(Currency currency) {
        return modelMapper.map(currency, CurrencyDto.class);
    }
}

