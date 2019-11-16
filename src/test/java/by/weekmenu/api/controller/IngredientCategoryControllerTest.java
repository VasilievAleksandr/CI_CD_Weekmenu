package by.weekmenu.api.controller;

import by.weekmenu.api.dto.IngredientCategoryDTO;
import by.weekmenu.api.entity.IngredientCategory;
import by.weekmenu.api.repository.MealTypeRepository;
import by.weekmenu.api.repository.OwnershipRepository;
import by.weekmenu.api.repository.UnitOfMeasureRepository;
import by.weekmenu.api.service.IngredientCategoryService;
import by.weekmenu.api.utils.UrlConsts;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
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
@WebMvcTest(IngredientCategoryController.class)
public class IngredientCategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    IngredientCategoryService ingredientCategoryService;

    @MockBean
    private OwnershipRepository ownershipRepository;

    @MockBean
    private UnitOfMeasureRepository unitOfMeasureRepository;

    @MockBean
    private MealTypeRepository mealTypeRepository;

    private IngredientCategoryDTO createIngredientCategoryDTO(Integer id, String name) {
        IngredientCategoryDTO ingredientCategoryDTO = new IngredientCategoryDTO();
        ingredientCategoryDTO.setId(id);
        ingredientCategoryDTO.setName(name);
        return ingredientCategoryDTO;
    }

    @Test
    public void saveIngredientCategoryTest() throws Exception {
        IngredientCategoryDTO ingredientCategoryDTO = createIngredientCategoryDTO(1, "Milk");
        when(ingredientCategoryService.save(any(IngredientCategoryDTO.class))).thenReturn(ingredientCategoryDTO);
        ObjectMapper objectMapper = new ObjectMapper();
        mockMvc.perform(post(UrlConsts.PATH_INGREDIENT_CATEGORIES)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(ingredientCategoryDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Milk")));
    }

    @Test
    public void findAllIngredientCategoriesTest() throws Exception {
        List ingredientCategoryDTOs = new ArrayList();
        ingredientCategoryDTOs.add(createIngredientCategoryDTO(1, "Milk"));
        ingredientCategoryDTOs.add(createIngredientCategoryDTO(2, "Bread"));
        when(ingredientCategoryService.findAll()).thenReturn(ingredientCategoryDTOs);
        mockMvc.perform(get(UrlConsts.PATH_INGREDIENT_CATEGORIES)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("Milk")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].name", is("Bread")))
                .andDo(print());
    }

    @Test
    public void updateIngredientCategoryTest() throws Exception {
        IngredientCategoryDTO ingredientCategoryDTO = createIngredientCategoryDTO(1, "Milk");
        when(ingredientCategoryService.findById(ingredientCategoryDTO.getId())).thenReturn(ingredientCategoryDTO);
        ingredientCategoryDTO.setName("Bread");
        when(ingredientCategoryService.save(any(IngredientCategoryDTO.class))).thenReturn(ingredientCategoryDTO);
        ObjectMapper objectMapper = new ObjectMapper();
        mockMvc.perform(put(UrlConsts.PATH_INGREDIENT_CATEGORIES + "/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(ingredientCategoryDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Bread")));
    }

    @Test
    public void deleteIngredientCategoryTest() throws Exception {
        IngredientCategoryDTO ingredientCategoryDTO = createIngredientCategoryDTO(1, "Milk");
        when(ingredientCategoryService.findById(ingredientCategoryDTO.getId())).thenReturn(ingredientCategoryDTO);
        mockMvc.perform(delete(UrlConsts.PATH_INGREDIENT_CATEGORIES + "/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    public void checkUniqueNameIngredientCategoryTest() throws Exception {
        IngredientCategory ingredientCategory = new IngredientCategory(1,"Milk", true);
        String name = "Milk";
        when(ingredientCategoryService.findByName(name)).thenReturn(ingredientCategory);
        mockMvc.perform(get(UrlConsts.PATH_INGREDIENT_CATEGORIES + "/checkIngredientCategoryUniqueName?name=" + name)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(String.valueOf(-1)));
    }

    @Test
    public void checkConnectedElementsTest() throws Exception {
        List<String> result = Collections.singletonList("ингредиенты: 1");
        when(ingredientCategoryService.checkConnectedElements(1)).thenReturn(result);
        mockMvc.perform(get(UrlConsts.PATH_INGREDIENT_CATEGORIES + "/checkConnectedElements/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }
}
