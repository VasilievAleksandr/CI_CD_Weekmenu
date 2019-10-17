package by.weekmenu.api.controller;

import by.weekmenu.api.dto.RecipeSubcategoryDTO;
import by.weekmenu.api.entity.RecipeSubcategory;
import by.weekmenu.api.repository.MealTypeRepository;
import by.weekmenu.api.repository.OwnershipRepository;
import by.weekmenu.api.repository.UnitOfMeasureRepository;
import by.weekmenu.api.service.RecipeSubcategoryService;
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
@WebMvcTest(RecipeSubcategoryController.class)
public class RecipeSubcategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    RecipeSubcategoryService recipeSubcategoryService;

    @MockBean
    private OwnershipRepository ownershipRepository;

    @MockBean
    private UnitOfMeasureRepository unitOfMeasureRepository;

    @MockBean
    private MealTypeRepository mealTypeRepository;

    private RecipeSubcategoryDTO createRecipeSubcategoryDTO(Long id, String name) {
        RecipeSubcategoryDTO recipeSubcategoryDTO = new RecipeSubcategoryDTO();
        recipeSubcategoryDTO.setId(id);
        recipeSubcategoryDTO.setName(name);
        return recipeSubcategoryDTO;
    }

    @Test
    public void saveRecipeSubcategoryTest() throws Exception {
        RecipeSubcategoryDTO recipeSubcategoryDTO = createRecipeSubcategoryDTO(1L, "Курица");
        when(recipeSubcategoryService.save(any(RecipeSubcategoryDTO.class))).thenReturn(recipeSubcategoryDTO);
        ObjectMapper objectMapper = new ObjectMapper();
        mockMvc.perform(post(UrlConsts.PATH_RECIPESUBCATEGORIES)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(recipeSubcategoryDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Курица")));
    }

    @Test
    public void findAllRecipeSubcategoriesTest() throws Exception {
        List recipeSubcategoryDTOs = new ArrayList();
        recipeSubcategoryDTOs.add(createRecipeSubcategoryDTO(1L, "Курица"));
        recipeSubcategoryDTOs.add(createRecipeSubcategoryDTO(2L, "Рыба"));
        when(recipeSubcategoryService.findAll()).thenReturn(recipeSubcategoryDTOs);
        mockMvc.perform(get(UrlConsts.PATH_RECIPESUBCATEGORIES)
                .contentType(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("Курица")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].name", is("Рыба")))
                .andDo(print());
    }

    @Test
    public void updateRecipeSubcategoryTest() throws Exception {
        RecipeSubcategoryDTO recipeSubcategoryDTO = createRecipeSubcategoryDTO(1L, "Курица");
        when(recipeSubcategoryService.findById(recipeSubcategoryDTO.getId())).thenReturn(recipeSubcategoryDTO);
        recipeSubcategoryDTO.setName("Рыба");
        when(recipeSubcategoryService.save(any(RecipeSubcategoryDTO.class))).thenReturn(recipeSubcategoryDTO);
        ObjectMapper objectMapper = new ObjectMapper();
        mockMvc.perform(put(UrlConsts.PATH_RECIPESUBCATEGORIES + "/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(recipeSubcategoryDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Рыба")));
    }

    @Test
    public void deleteRecipeSubcategoryTest() throws Exception{
        RecipeSubcategoryDTO recipeSubcategoryDTO = createRecipeSubcategoryDTO(1L, "Курица");
        when(recipeSubcategoryService.findById(recipeSubcategoryDTO.getId())).thenReturn(recipeSubcategoryDTO);
        mockMvc.perform(delete(UrlConsts.PATH_RECIPESUBCATEGORIES + "/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    public void checkUniqueNameRecipeSubcategoryTest() throws Exception{
        RecipeSubcategory recipeSubcategory = new RecipeSubcategory ( 1L, "Рыба");
        String name = "Рыба";
        when(recipeSubcategoryService.findByName(name)).thenReturn(recipeSubcategory);
        mockMvc.perform(get(UrlConsts.PATH_RECIPESUBCATEGORIES + "/checkRecipeSubcategoryUniqueName?name=" + name)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(String.valueOf(-1)));
    }

    @Test
    public void checkConnectedElementsTest() throws Exception {
        List<String> result = Collections.singletonList("рецепты: 1");
        when(recipeSubcategoryService.checkConnectedElements(1L)).thenReturn(result);
        mockMvc.perform(get(UrlConsts.PATH_RECIPESUBCATEGORIES + "/checkConnectedElements/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }
}
