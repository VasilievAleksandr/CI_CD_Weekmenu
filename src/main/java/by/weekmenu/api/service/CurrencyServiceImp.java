package by.weekmenu.api.service;

import by.weekmenu.api.dto.CurrencyDTO;
import by.weekmenu.api.entity.Country;
import by.weekmenu.api.entity.Currency;
import by.weekmenu.api.entity.RecycleBin;
import by.weekmenu.api.repository.CountryRepository;
import by.weekmenu.api.repository.CurrencyRepository;
import by.weekmenu.api.repository.RecycleBinRepository;
import by.weekmenu.api.utils.EntityNamesConsts;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Transactional(readOnly = true)
public class CurrencyServiceImp implements CurrencyService {

    private final CurrencyRepository currencyRepository;
    private final RecycleBinRepository recycleBinRepository;
    private final CountryRepository countryRepository;
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public CurrencyDTO save(CurrencyDTO entityDto) {
        return convertToDto(currencyRepository.save(convertToEntity(entityDto)));
    }

    @Override
    public CurrencyDTO findById(Integer id) {
        return convertToDto(currencyRepository.findById(id).orElse(null));
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        Currency currency = currencyRepository.findById(id).orElse(null);
        if (currency!=null) {
            RecycleBin recycleBin = new RecycleBin();
            recycleBin.setElementName(currency.getName());
            recycleBin.setEntityName(EntityNamesConsts.CURRENCY);
            recycleBin.setDeleteDate(LocalDateTime.now());
            recycleBinRepository.save(recycleBin);
            currencyRepository.softDelete(id);
        }
    }

    @Override
    public List<CurrencyDTO> findAll() {
        return currencyRepository.findAllByIsArchivedIsFalse()
                .stream()
                .filter(Objects::nonNull)
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<String> getAllCurrencyCodes() {
        return currencyRepository.findAllByIsArchivedIsFalse()
                .stream()
                .filter(Objects::nonNull)
                .map(Currency::getCode)
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

    @Override
    public List<String> checkConnectedElements(Integer id) {
        List<String> list = new ArrayList<>();
        List<Country> countries = countryRepository.findAllByCurrency_Id(id);
        if (countries.size()>0) {
            list.add("страны: " + countries.size());
        }
        return list;
    }

    private Currency convertToEntity(CurrencyDTO currencyDto) {
        return modelMapper.map(currencyDto, Currency.class);
    }

    private CurrencyDTO convertToDto(Currency currency) {
        return modelMapper.map(currency, CurrencyDTO.class);
    }
}
