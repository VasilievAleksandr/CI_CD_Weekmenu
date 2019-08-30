package by.weekmenu.api.integrationTests;

import by.weekmenu.api.ApiApplication;
import by.weekmenu.api.dto.CurrencyDTO;
import by.weekmenu.api.entity.*;
import by.weekmenu.api.repository.*;
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
public class CurrencyIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CurrencyRepository currencyRepository;

    @Autowired
    private RecycleBinRepository recycleBinRepository;

    @Autowired
    private CountryRepository countryRepository;

//    @Autowired
//    private OwnershipRepository ownershipRepository;

    @After
    public void cleanDB() {
        currencyRepository.deleteAll();
        recycleBinRepository.deleteAll();
        countryRepository.deleteAll();
    }

    @Test
    @Transactional
    public void saveCurrencyIntegrationTest() throws Exception {
        CurrencyDTO currencyDTO = new CurrencyDTO();
        currencyDTO.setId(1);
        currencyDTO.setName("Рубль");
        currencyDTO.setCode("RUB");
        ObjectMapper objectMapper = new ObjectMapper();
        mockMvc.perform(post("/currencies")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(currencyDTO)));
        Iterable<Currency> currencies = currencyRepository.findAll();
        assertThat(currencies).extracting(Currency::getName).containsOnly("Рубль");
        assertThat(currencies).extracting(Currency::getCode).containsOnly("RUB");
    }

    @Test
    public void getAllCurrenciesIntegrationTest() throws Exception {
        Currency currency1 = new Currency("Рубль", "RUB", false);
        Currency currency2 = new Currency("Доллар", "USD", false);
        currencyRepository.save(currency1);
        currencyRepository.save(currency2);
        mockMvc.perform(get("/currencies")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", is("Рубль")))
                .andExpect(jsonPath("$[0].code", is("RUB")))
                .andExpect(jsonPath("$[1].name", is("Доллар")))
                .andExpect(jsonPath("$[1].code", is("USD")));
    }

    @Test
    public void updateCurrencyIntegrationTest() throws Exception {
        Currency currency = new Currency("Рубль", "RUB", false);
        currencyRepository.save(currency);
        CurrencyDTO currencyDTO = new CurrencyDTO();
        currencyDTO.setId(1);
        currencyDTO.setName("Доллар");
        currencyDTO.setCode("USD");
        ObjectMapper objectMapper = new ObjectMapper();
        mockMvc.perform(put("/currencies/" + currency.getId().toString())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(currencyDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Доллар")))
                .andExpect(jsonPath("$.code", is("USD")));
    }

    @Test
    public void deleteCurrencyIntegrationTest() throws Exception {
        Currency currency = currencyRepository.save(new Currency("Рубль", "RUB", false));
        mockMvc.perform(delete("/currencies/" + currency.getId().toString())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
        Iterable<RecycleBin> recycleBins = recycleBinRepository.findAll();
        assertThat(recycleBins).extracting(RecycleBin::getElementName).containsOnly("Рубль");
        assertThat(recycleBins).extracting(RecycleBin::getEntityName).containsOnly("Валюта");
        assertThat(recycleBins).extracting(RecycleBin::getDeleteDate).isNotNull();
        Optional<Currency> currencyAfterSoftDelete = currencyRepository.findById(currency.getId());
        assertThat(currencyAfterSoftDelete.get().isArchived()).isTrue();
    }

    @Test
    public void checkUniqueNameCurrencyIntegrationTest() throws Exception {
        Currency currency = new Currency("Рубль", "RUB", false);
        currencyRepository.save(currency);
        mockMvc.perform(get("/currencies/checkCurrencyUniqueName?name=" + currency.getName())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(String.valueOf(-1)));
    }

    @Test
    public void checkUniqueCodeCurrencyIntegrationTest() throws Exception {
        Currency currency = new Currency("Рубль", "RUB", false);
        currencyRepository.save(currency);
        mockMvc.perform(get("/currencies/checkCurrencyUniqueCode?code=" + currency.getCode())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(String.valueOf(-1)));
    }

    @Test
    @Transactional
    public void checkConnectedElementsTest() throws Exception {
        Currency currency = new Currency("Рубль", "RUB", false);
        currencyRepository.save(currency);
        Country country =  new Country("Россия", "RU", currency);
        countryRepository.save(country);
        mockMvc.perform(get("/currencies/checkConnectedElements/" + currency.getId().toString())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }
}
