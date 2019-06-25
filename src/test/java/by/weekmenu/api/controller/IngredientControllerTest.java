package by.weekmenu.api.controller;

import by.weekmenu.api.dto.IngredientDto;
import by.weekmenu.api.entity.Ingredient;
import by.weekmenu.api.repository.OwnershipRepository;
import by.weekmenu.api.service.IngredientService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(IngredientController.class)
public class IngredientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IngredientService ingredientService;

    @MockBean
    private OwnershipRepository ownershipRepository;

    private IngredientDto createIngredientDto(Long id, String name) {
        IngredientDto ingredientDto = new IngredientDto();
        ingredientDto.setId(id);
        ingredientDto.setName(name);
        ingredientDto.setCalories(new BigDecimal("100"));
        ingredientDto.setCarbs(new BigDecimal("100"));
        ingredientDto.setFats(new BigDecimal("100"));
        ingredientDto.setProteins(new BigDecimal("100"));
        return ingredientDto;
    }

    private Ingredient createIngredient(Long id, String name) {
        Ingredient ingredient = new Ingredient();
        ingredient.setId(id);
        ingredient.setName(name);
        ingredient.setCalories(new BigDecimal("100"));
        ingredient.setCarbs(new BigDecimal("100"));
        ingredient.setFats(new BigDecimal("100"));
        ingredient.setProteins(new BigDecimal("100"));
        return ingredient;
    }

    @Test
    public void findAllIngredients() throws Exception {
        List<IngredientDto> list = new ArrayList<>();
        list.add(createIngredientDto(1L, "Курица"));
        list.add(createIngredientDto(2L, "Ананас"));
        when(ingredientService.findAll()).thenReturn(list);

        mockMvc.perform(get("/ingredients")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("Курица")))
                .andExpect(jsonPath("$[0].calories", is(100)))
                .andExpect(jsonPath("$[0].carbs", is(100)))
                .andExpect(jsonPath("$[0].fats", is(100)))
                .andExpect(jsonPath("$[0].proteins", is(100)))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].name", is("Ананас")))
                .andExpect(jsonPath("$[1].calories", is(100)))
                .andExpect(jsonPath("$[1].carbs", is(100)))
                .andExpect(jsonPath("$[1].fats", is(100)))
                .andExpect(jsonPath("$[1].proteins", is(100)))
                .andDo(print());
    }

    @Test
    public void findIngredientById() throws Exception{
        IngredientDto ingredientDto = createIngredientDto(1L, "Курица");
        when(ingredientService.findById(ingredientDto.getId())).thenReturn(ingredientDto);
        mockMvc.perform(get("/ingredients/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Курица")))
                .andExpect(jsonPath("$.calories", is(100)))
                .andExpect(jsonPath("$.carbs", is(100)))
                .andExpect(jsonPath("$.fats", is(100)))
                .andExpect(jsonPath("$.proteins", is(100)));
    }

    @Test
    public void addIngredientTest() throws Exception {
        IngredientDto ingredientDto = createIngredientDto(1L, "Курица");
        when(ingredientService.save(any(IngredientDto.class))).thenReturn(ingredientDto);
        ObjectMapper objectMapper = new ObjectMapper();
        mockMvc.perform(post("/ingredients").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(ingredientDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Курица")))
                .andExpect(jsonPath("$.calories", is(100)))
                .andExpect(jsonPath("$.carbs", is(100)))
                .andExpect(jsonPath("$.fats", is(100)))
                .andExpect(jsonPath("$.proteins", is(100)));
    }

    @Test
    public void updateIngredient() throws Exception{
        IngredientDto ingredientDto = createIngredientDto(1L, "Курица");
        when(ingredientService.findById(ingredientDto.getId())).thenReturn(ingredientDto);
        ingredientDto.setName("Ананас");
        ingredientDto.setFats(new BigDecimal("50"));
        when(ingredientService.save(any(IngredientDto.class))).thenReturn(ingredientDto);
        ObjectMapper objectMapper = new ObjectMapper();
        mockMvc.perform(put("/ingredients/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(ingredientDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Ананас")))
                .andExpect(jsonPath("$.calories", is(100)))
                .andExpect(jsonPath("$.carbs", is(100)))
                .andExpect(jsonPath("$.fats", is(50)))
                .andExpect(jsonPath("$.proteins", is(100)));
    }

    @Test
    public void deleteIngredient() throws Exception{
        IngredientDto ingredientDto = createIngredientDto(1L, "Курица");
        when(ingredientService.findById(ingredientDto.getId())).thenReturn(ingredientDto);
        mockMvc.perform(delete("/ingredients/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void checkUniqueName() throws Exception{
        Ingredient ingredient = createIngredient(1L, "Курица");
        String name = "Курица";
        when(ingredientService.findByName(name)).thenReturn(ingredient);
        mockMvc.perform(get("/ingredients/checkUniqueName?name=" + name)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(String.valueOf(-1)));
    }
}