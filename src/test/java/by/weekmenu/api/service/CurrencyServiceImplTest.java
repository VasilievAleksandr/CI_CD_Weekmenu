package by.weekmenu.api.service;

import by.weekmenu.api.dto.CurrencyDTO;
import by.weekmenu.api.entity.Country;
import by.weekmenu.api.entity.Currency;
import by.weekmenu.api.entity.RecycleBin;
import by.weekmenu.api.repository.CountryRepository;
import by.weekmenu.api.repository.CurrencyRepository;
import by.weekmenu.api.repository.RecycleBinRepository;
import by.weekmenu.api.utils.EntityNamesConsts;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
public class CurrencyServiceImplTest {

    @MockBean
    private CurrencyRepository currencyRepository;

    @MockBean
    private RecycleBinRepository recycleBinRepository;

    @MockBean
    private ModelMapper modelMapper;

    @MockBean
    private CountryRepository countryRepository;

    private CurrencyService currencyService;

    @Before
    public void setup() {
        currencyService = new CurrencyServiceImp(currencyRepository, recycleBinRepository, countryRepository, modelMapper);
    }

    @Test
    public void getAllCurrencyTest() {
        List<Currency> currencies = new ArrayList<>();
        currencies.add(new Currency("Рубль", "RUB", false));
        currencies.add(new Currency("Доллар", "USD", false));
        when(currencyRepository.findAllByIsArchivedIsFalse()).thenReturn(currencies);
        List<CurrencyDTO> result = currencyService.findAll();
        assertThat(currencies.size()).isEqualTo(result.size());
    }

    @Test
    public void saveCurrencyTest() {
        CurrencyDTO currencyDTO = new CurrencyDTO();
        currencyDTO.setId(1);
        currencyDTO.setName("Рубль");
        currencyDTO.setCode("RUB");
        Currency currency = new Currency("Рубль", "RUB", false);
        when(modelMapper.map(currencyDTO, Currency.class)).thenReturn(currency);
        when(currencyRepository.save(currency)).thenReturn(currency);
        when(modelMapper.map(currency, CurrencyDTO.class)).thenReturn(currencyDTO);
        CurrencyDTO saved = currencyService.save(currencyDTO);
        assertThat(saved).isNotNull();
        assertThat(saved.getName()).isEqualTo("Рубль");
        assertThat(saved.getCode()).isEqualTo("RUB");
    }

    @Test
    public void findCurrencyByIdTest() {
        CurrencyDTO currencyDTO = new CurrencyDTO();
        currencyDTO.setId(1);
        currencyDTO.setName("Рубль");
        currencyDTO.setCode("RUB");
        Currency currency = new Currency("Рубль", "RUB", false);
        when(modelMapper.map(currency, CurrencyDTO.class)).thenReturn(currencyDTO);
        when(currencyRepository.findById(currencyDTO.getId())).thenReturn(Optional.of(currency));
        CurrencyDTO found = currencyService.findById(currencyDTO.getId());
        assertThat(found).isNotNull();
        assertThat(found.getName()).isEqualTo("Рубль");
        assertThat(found.getCode()).isEqualTo("RUB");
    }

    @Test
    public void moveToRecycleBinTest() {
        CurrencyDTO currencyDTO = new CurrencyDTO();
        currencyDTO.setId(1);
        currencyDTO.setName("Рубль");
        currencyDTO.setCode("RUB");
        RecycleBin recycleBin = new RecycleBin();
        recycleBin.setElementName(currencyDTO.getName());
        recycleBin.setEntityName(EntityNamesConsts.CURRENCY);
        recycleBin.setDeleteDate(LocalDateTime.now());
        when(recycleBinRepository.save(recycleBin)).thenReturn(recycleBin);
        currencyService.moveToRecycleBin(currencyDTO);
        verify(currencyRepository, times(1)).softDelete(1);
        assertThat(recycleBin.getElementName()).isEqualTo("Рубль");
        assertThat(recycleBin.getEntityName()).isEqualTo("Валюта");
        assertThat(recycleBin.getDeleteDate()).isNotNull();
    }

    @Test
    public void checkUniqueCurrencyNameTest() {
        Currency currency = new Currency("Рубль", "RUB", false);
        currency.setId(1);
        when(currencyRepository.findByNameIgnoreCase(currency.getName())).thenReturn(Optional.of(currency));
        assertThat(currency.getName()).isEqualTo("Рубль");
    }

    @Test
    public void checkConnectedElementsTest() {
        Currency currency = new Currency("Рубль", "RUB", false);
        currency.setId(1);
        Country country = new Country();
        country.setCurrency(currency);
        List<Country> countries = new ArrayList<>();
        countries.add(country);
        when(countryRepository.findAllByCurrency_Id(currency.getId())).thenReturn(countries);
        List<String> list = currencyService.checkConnectedElements(currency.getId());
        assertThat(list.size()).isEqualTo(1);
    }
}
