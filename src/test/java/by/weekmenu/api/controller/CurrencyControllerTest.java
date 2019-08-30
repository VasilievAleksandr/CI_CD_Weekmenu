package by.weekmenu.api.controller;

import by.weekmenu.api.dto.CurrencyDTO;
import by.weekmenu.api.entity.Currency;
import by.weekmenu.api.repository.OwnershipRepository;
import by.weekmenu.api.repository.UnitOfMeasureRepository;
import by.weekmenu.api.service.CurrencyService;
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
@WebMvcTest(CurrencyController.class)
public class CurrencyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CurrencyService currencyService;

    @MockBean
    private ModelMapper modelMapper;

    @MockBean
    private OwnershipRepository ownershipRepository;

    @MockBean
    private UnitOfMeasureRepository unitOfMeasureRepository;

    private CurrencyDTO createCurrencyDTO(Integer id, String name, String code) {
        CurrencyDTO currencyDTO = new CurrencyDTO();
        currencyDTO.setId(id);
        currencyDTO.setName(name);
        currencyDTO.setCode(code);
        return currencyDTO;
    }

    @Test
    public void saveCurrencyTest() throws Exception {
        CurrencyDTO currencyDTO = createCurrencyDTO(1, "Рубль", "RUB");
        when(currencyService.save(any(CurrencyDTO.class))).thenReturn(currencyDTO);
        ObjectMapper objectMapper = new ObjectMapper();
        mockMvc.perform(post("/currencies")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(currencyDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Рубль")))
                .andExpect(jsonPath("$.code", is("RUB")));
    }

    @Test
    public void findAllCurrenciesTest() throws Exception {
        List currencyDTOs = new ArrayList();
        currencyDTOs.add(createCurrencyDTO(1, "Рубль", "RUB"));
        currencyDTOs.add(createCurrencyDTO(2, "Доллар", "USD"));
        when(currencyService.findAll()).thenReturn(currencyDTOs);
        mockMvc.perform(get("/currencies")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("Рубль")))
                .andExpect(jsonPath("$[0].code", is("RUB")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].name", is("Доллар")))
                .andExpect(jsonPath("$[1].code", is("USD")))
                .andDo(print());
    }

    @Test
    public void updateCurrencyTest() throws Exception {
        CurrencyDTO currencyDTO = createCurrencyDTO(1, "Рубль", "RUB");
        when(currencyService.findById(currencyDTO.getId())).thenReturn(currencyDTO);
        currencyDTO.setName("Доллар");
        currencyDTO.setCode("USD");
        when(currencyService.save(any(CurrencyDTO.class))).thenReturn(currencyDTO);
        ObjectMapper objectMapper = new ObjectMapper();
        mockMvc.perform(put("/currencies/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(currencyDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Доллар")))
                .andExpect(jsonPath("$.code", is("USD")));
    }

    @Test
    public void deleteCurrencyTest() throws Exception {
        CurrencyDTO currencyDTO = createCurrencyDTO(1, "Рубль", "RUB");
        when(currencyService.findById(currencyDTO.getId())).thenReturn(currencyDTO);
        mockMvc.perform(delete("/currencies/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    public void checkCurrencyUniqueNameTest() throws Exception {
        Currency currency = new Currency("Рубль", "RUB", false);
        String name = "Рубль";
        when(currencyService.findByName(name)).thenReturn(currency);
        mockMvc.perform(get("/currencies/checkCurrencyUniqueName?name=" + name)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(String.valueOf(-1)));
    }

    @Test
    public void checkCurrencyUniqueCodeTest() throws Exception {
        Currency currency = new Currency("Рубль", "RUB", false);
        String code = "RUB";
        when(currencyService.findByCode(code)).thenReturn(currency);
        mockMvc.perform(get("/currencies/checkCurrencyUniqueCode?code=" + code)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(String.valueOf(-1)));
    }

    @Test
    public void checkConnectedElementsTest() throws Exception {
        List<String> result = Collections.singletonList("Страны: 1");
        when(currencyService.checkConnectedElements(1)).thenReturn(result);
        mockMvc.perform(get("/currencies/checkConnectedElements/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }
}
