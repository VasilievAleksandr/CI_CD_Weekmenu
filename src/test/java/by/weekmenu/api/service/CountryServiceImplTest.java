package by.weekmenu.api.service;

import by.weekmenu.api.dto.CountryDTO;
import by.weekmenu.api.entity.Country;
import by.weekmenu.api.entity.Currency;
import by.weekmenu.api.entity.RecycleBin;
import by.weekmenu.api.entity.Region;
import by.weekmenu.api.repository.CountryRepository;
import by.weekmenu.api.repository.CurrencyRepository;
import by.weekmenu.api.repository.RecycleBinRepository;
import by.weekmenu.api.repository.RegionRepository;
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
public class CountryServiceImplTest {

    @MockBean
    private CurrencyRepository currencyRepository;

    @MockBean
    private RecycleBinRepository recycleBinRepository;

    @MockBean
    private ModelMapper modelMapper;

    @MockBean
    private CountryRepository countryRepository;

    @MockBean
    private RegionRepository regionRepository;


    private CountryService countryService;

    @Before
    public void setup() {
        countryService = new CountryServiceImpl(countryRepository, currencyRepository, recycleBinRepository, regionRepository, modelMapper);
    }

    @Test
    public void getAllCountryTest() {
        List<Country> countries = new ArrayList<>();
        Currency currency = new Currency("Рубль", "RUB", false);
        countries.add(new Country("РБ", "RB", currency));
        countries.add(new Country("РФ", "RU", currency));
        when(countryRepository.findAllByIsArchivedIsFalse()).thenReturn(countries);
        List<CountryDTO> result = countryService.findAll();
        assertThat(countries.size()).isEqualTo(result.size());
    }

    @Test
    public void saveCountryTest() {
        CountryDTO countryDTO = new CountryDTO();
        countryDTO.setId(1L);
        countryDTO.setName("РФ");
        countryDTO.setAlphaCode2("RU");
        countryDTO.setCurrencyCode("RUB");
        Currency currency = new Currency("Рубль", "RUB", false);
        Country country = new Country("РБ", "RB", currency);
        when(modelMapper.map(countryDTO, Country.class)).thenReturn(country);
        when(countryRepository.save(country)).thenReturn(country);
        when(modelMapper.map(country, CountryDTO.class)).thenReturn(countryDTO);
        CountryDTO saved = countryService.save(countryDTO);
        assertThat(saved).isNotNull();
        assertThat(saved.getName()).isEqualTo("РФ");
        assertThat(saved.getAlphaCode2()).isEqualTo("RU");
        assertThat(saved.getCurrencyCode()).isEqualTo("RUB");
    }

    @Test
    public void findCountryByIdTest() {
        CountryDTO countryDTO = new CountryDTO();
        countryDTO.setId(1L);
        countryDTO.setName("РФ");
        countryDTO.setAlphaCode2("RU");
        countryDTO.setCurrencyCode("RUB");
        Currency currency = new Currency("Рубль", "RUB", false);
        Country country = new Country("РБ", "RB", currency);
        when(modelMapper.map(country, CountryDTO.class)).thenReturn(countryDTO);
        when(countryRepository.findById(countryDTO.getId())).thenReturn(Optional.of(country));
        CountryDTO found = countryService.findById(countryDTO.getId());
        assertThat(found).isNotNull();
        assertThat(found.getName()).isEqualTo("РФ");
        assertThat(found.getAlphaCode2()).isEqualTo("RU");
        assertThat(found.getCurrencyCode()).isEqualTo("RUB");
    }

    @Test
    public void moveToRecycleBinTest() {
        CountryDTO countryDTO = new CountryDTO();
        countryDTO.setId(1L);
        countryDTO.setName("РФ");
        countryDTO.setAlphaCode2("RU");
        countryDTO.setCurrencyCode("RUB");
        RecycleBin recycleBin = new RecycleBin();
        recycleBin.setElementName(countryDTO.getName());
        recycleBin.setEntityName(EntityNamesConsts.COUNTRY);
        recycleBin.setDeleteDate(LocalDateTime.now());
        when(recycleBinRepository.save(recycleBin)).thenReturn(recycleBin);
        countryService.moveToRecycleBin(countryDTO);
        verify(countryRepository, times(1)).softDelete(1L);
        assertThat(recycleBin.getElementName()).isEqualTo("РФ");
        assertThat(recycleBin.getEntityName()).isEqualTo("Страна");
        assertThat(recycleBin.getDeleteDate()).isNotNull();
    }

    @Test
    public void checkConnectedElementsTest() {
        Currency currency = new Currency("Рубль", "RUB", false);
        Country country = new Country("РБ", "RB", currency);
        country.setId(1L);
        Region region = new Region("Томь", country);
        List<Region> regions = new ArrayList<>();
        regions.add(region);
        when(regionRepository.findAllByCountry_Id(country.getId())).thenReturn(regions);
        List<String> list = countryService.checkConnectedElements(country.getId());
        assertThat(list.size()).isEqualTo(1);
    }
}
