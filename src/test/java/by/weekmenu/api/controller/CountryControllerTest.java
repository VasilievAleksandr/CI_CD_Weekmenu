package by.weekmenu.api.controller;

import by.weekmenu.api.dto.CountryDTO;
import by.weekmenu.api.entity.Country;
import by.weekmenu.api.entity.Currency;
import by.weekmenu.api.repository.OwnershipRepository;
import by.weekmenu.api.repository.UnitOfMeasureRepository;
import by.weekmenu.api.service.CountryService;
import by.weekmenu.api.utils.UrlConsts;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(CountryController.class)
public class CountryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CountryService countryService;

    @MockBean
    private ModelMapper modelMapper;

    @MockBean
    private OwnershipRepository ownershipRepository;

    @MockBean
    private UnitOfMeasureRepository unitOfMeasureRepository;

    private CountryDTO createCountryDTO(Long id, String name, String alphaCode2, String currencyCode) {
        CountryDTO countryDTO = new CountryDTO();
        countryDTO.setId(id);
        countryDTO.setName(name);
        countryDTO.setAlphaCode2(alphaCode2);
        countryDTO.setCurrencyCode(currencyCode);
        return countryDTO;
    }

    @Test
    public void saveCountryTest() throws Exception {
        CountryDTO countryDTO = createCountryDTO(1L, "РБ", "RB", "BUN");
        when(countryService.save(any(CountryDTO.class))).thenReturn(countryDTO);
        ObjectMapper objectMapper = new ObjectMapper();
        mockMvc.perform(post(UrlConsts.PATH_COUNTRIES)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(countryDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("РБ")))
                .andExpect(jsonPath("$.alphaCode2", is("RB")))
                .andExpect(jsonPath("$.currencyCode", is("BUN")));

    }

    @Test
    public void findAllCountriesTest() throws Exception {
        List countryDTOs = new ArrayList();
        countryDTOs.add(createCountryDTO(1L, "РБ", "RB", "BUN"));
        countryDTOs.add(createCountryDTO(2L, "РФ", "RU", "RUB"));
        when(countryService.findAll()).thenReturn(countryDTOs);
        mockMvc.perform(get(UrlConsts.PATH_COUNTRIES)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("РБ")))
                .andExpect(jsonPath("$[0].alphaCode2", is("RB")))
                .andExpect(jsonPath("$[0].currencyCode", is("BUN")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].name", is("РФ")))
                .andExpect(jsonPath("$[1].alphaCode2", is("RU")))
                .andExpect(jsonPath("$[1].currencyCode", is("RUB")))
                .andDo(print());
    }

    @Test
    public void updateCountryTest() throws Exception {
        CountryDTO countryDTO = createCountryDTO(1L, "РБ", "RB", "BUN");
        when(countryService.findById(countryDTO.getId())).thenReturn(countryDTO);
        countryDTO.setName("РФ");
        countryDTO.setAlphaCode2("RU");
        countryDTO.setCurrencyCode("RUB");
        when(countryService.save(any(CountryDTO.class))).thenReturn(countryDTO);
        ObjectMapper objectMapper = new ObjectMapper();
        mockMvc.perform(put(UrlConsts.PATH_COUNTRIES+"/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(countryDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("РФ")))
                .andExpect(jsonPath("$.alphaCode2", is("RU")))
                .andExpect(jsonPath("$.currencyCode", is("RUB")));
    }

    @Test
    public void deleteCountryTest() throws Exception {
        CountryDTO countryDTO = createCountryDTO(1L, "РБ", "RB", "BUN");
        when(countryService.findById(countryDTO.getId())).thenReturn(countryDTO);
        mockMvc.perform(delete(UrlConsts.PATH_COUNTRIES+"/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    public void checkCountryUniqueNameTest() throws Exception {
        Currency currency = new Currency("Рубль", "BUN", false);
        Country country = new Country( "РБ", "RB", currency);
        String name = "РБ";
        when(countryService.findByName(name)).thenReturn(country);
        mockMvc.perform(get(UrlConsts.PATH_COUNTRIES+"/checkUniqueName?name=" + name)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(String.valueOf(-1)));
    }

    @Test
    public void checkCountryUniqueAlphaCode2Test() throws Exception {
        Currency currency = new Currency("Рубль", "BUN", false);
        Country country = new Country( "РБ", "RB", currency);
        String alphaCode2 = "RB";
        when(countryService.findByAlphaCode2(alphaCode2)).thenReturn(country);
        mockMvc.perform(get(UrlConsts.PATH_COUNTRIES+"/checkUniqueAlphaCode2?alphaCode2=" + alphaCode2)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(String.valueOf(-1)));
    }

    @Test
    public void checkConnectedElementsTest() throws Exception {
        List<String> result = Collections.singletonList("Регионы: 1");
        when(countryService.checkConnectedElements(1L)).thenReturn(result);
        mockMvc.perform(get(UrlConsts.PATH_COUNTRIES+"/checkConnectedElements/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }
}
