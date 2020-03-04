package by.weekmenu.api.integrationTests;

import by.weekmenu.api.ApiApplication;
import by.weekmenu.api.dto.CountryDTO;
import by.weekmenu.api.entity.Country;
import by.weekmenu.api.entity.Currency;
import by.weekmenu.api.entity.RecycleBin;
import by.weekmenu.api.entity.Region;
import by.weekmenu.api.repository.CountryRepository;
import by.weekmenu.api.repository.CurrencyRepository;
import by.weekmenu.api.repository.RecycleBinRepository;
import by.weekmenu.api.repository.RegionRepository;
import by.weekmenu.api.utils.UrlConsts;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ApiApplication.class)
@AutoConfigureMockMvc
public class CountryIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CurrencyRepository currencyRepository;

    @Autowired
    private RecycleBinRepository recycleBinRepository;

    @Autowired
    private CountryRepository countryRepository;

    @Autowired
    private RegionRepository regionRepository;

    @After
    public void cleanDB() {
        recycleBinRepository.deleteAll();
        countryRepository.deleteAll();
        currencyRepository.deleteAll();
        regionRepository.deleteAll();
    }

    @Test
    @Transactional
    public void saveCountryIntegrationTest() throws Exception {
        CountryDTO countryDTO = new CountryDTO();
        countryDTO.setId(1L);
        countryDTO.setName("РФ");
        countryDTO.setAlphaCode2("RU");
        countryDTO.setCurrencyCode("BUN");
        Currency currency = new Currency("Рубль", "BUN", false);
        currencyRepository.save(currency);
        ObjectMapper objectMapper = new ObjectMapper();
        mockMvc.perform(post(UrlConsts.PATH_COUNTRIES)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(countryDTO)));
        Iterable<Country> countries = countryRepository.findAll();
        assertThat(countries).extracting(Country::getName).containsOnly("РФ");
        assertThat(countries).extracting(Country::getAlphaCode2).containsOnly("RU");
    }

    @Test
    public void getAllCountriesIntegrationTest() throws Exception {
        Currency currency1 = new Currency("Российский рубль", "RUB", false);
        Currency currency2 = new Currency("Белорусский рубль", "BUN", false);
        Country country1 = new Country("РБ", "RB", currency2);
        Country country2 = new Country("РФ", "RU", currency1);
        countryRepository.save(country1);
        countryRepository.save(country2);
        mockMvc.perform(get(UrlConsts.PATH_COUNTRIES)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", is("РБ")))
                .andExpect(jsonPath("$[0].alphaCode2", is("RB")))
                .andExpect(jsonPath("$[1].name", is("РФ")))
                .andExpect(jsonPath("$[1].alphaCode2", is("RU")));
    }

    @Test
    public void updateCountryIntegrationTest() throws Exception {
        Currency currency = new Currency("Рубль", "RUB", false);
        Country country = new Country("РБ", "RB", currency);
        countryRepository.save(country);
        CountryDTO countryDTO = new CountryDTO();
        countryDTO.setId(1L);
        countryDTO.setName("РФ");
        countryDTO.setAlphaCode2("RU");
        countryDTO.setCurrencyCode("RUB");
        ObjectMapper objectMapper = new ObjectMapper();
        mockMvc.perform(put(UrlConsts.PATH_COUNTRIES+"/" + country.getId().toString())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(countryDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("РФ")))
                .andExpect(jsonPath("$.alphaCode2", is("RU")));
    }

    @Test
    public void deleteCountryIntegrationTest() throws Exception {
        Currency currency = new Currency("Рубль", "RUB", false);
        Country country = new Country("РБ", "RB", currency);
        countryRepository.save(country);
        mockMvc.perform(delete(UrlConsts.PATH_COUNTRIES+"/" + country.getId().toString())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
        Iterable<RecycleBin> recycleBins = recycleBinRepository.findAll();
        assertThat(recycleBins).extracting(RecycleBin::getElementName).containsOnly("РБ");
        assertThat(recycleBins).extracting(RecycleBin::getEntityName).containsOnly("Страна");
        assertThat(recycleBins).extracting(RecycleBin::getDeleteDate).isNotNull();
        Optional<Country> countryAfterSoftDelete = countryRepository.findById(country.getId());
        assertThat(countryAfterSoftDelete.get().isArchived()).isTrue();
    }

    @Test
    public void checkUniqueAlphaCode2CountryIntegrationTest() throws Exception {
        Currency currency = new Currency("Рубль", "RUB", false);
        Country country = new Country("РБ", "RB", currency);
        countryRepository.save(country);
        mockMvc.perform(get(UrlConsts.PATH_COUNTRIES+"/checkUniqueAlphaCode2?alphaCode2=" + country.getAlphaCode2())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(String.valueOf(-1)));
    }

    @Test
    @Transactional
    public void checkConnectedElementsTest() throws Exception {
        Currency currency = new Currency("Рубль", "RUB", false);
        currencyRepository.save(currency);
        Country country = new Country("Россия", "RU", currency);
        countryRepository.save(country);
        Region region = new Region("Томь", country);
        regionRepository.save(region);
        mockMvc.perform(get(UrlConsts.PATH_COUNTRIES+"/checkConnectedElements/" + country.getId().toString())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }
}
