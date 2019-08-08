package by.weekmenu.api.controller;

import by.weekmenu.api.dto.RecipeSubcategoryDTO;
import by.weekmenu.api.entity.RecipeSubcategory;
import by.weekmenu.api.repository.OwnershipRepository;
import by.weekmenu.api.repository.UnitOfMeasureRepository;
import by.weekmenu.api.service.RecipeSubcategoryService;
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
    RecipeSubcategoryService recipeSubcategorySevice;

    @MockBean
    private OwnershipRepository ownershipRepository;

    @MockBean
    private UnitOfMeasureRepository unitOfMeasureRepository;

    private RecipeSubcategoryDTO createRecipeSubcategoryDTO(Long id, String name) {
        RecipeSubcategoryDTO recipeSubcategoryDTO = new RecipeSubcategoryDTO();
        recipeSubcategoryDTO.setId(id);
        recipeSubcategoryDTO.setName(name);
        return recipeSubcategoryDTO;
    }

    @Test
    public void saveRecipeSubcategoryTest() throws Exception {
        RecipeSubcategoryDTO recipeSubcategoryDTO = createRecipeSubcategoryDTO(1L, "Курица");
        when(recipeSubcategorySevice.save(any(RecipeSubcategoryDTO.class))).thenReturn(recipeSubcategoryDTO);
        ObjectMapper objectMapper = new ObjectMapper();
        mockMvc.perform(post("/recipesubcategories")
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
        when(recipeSubcategorySevice.findAll()).thenReturn(recipeSubcategoryDTOs);
        mockMvc.perform(get("/recipesubcategories")
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
        when(recipeSubcategorySevice.findById(recipeSubcategoryDTO.getId())).thenReturn(recipeSubcategoryDTO);
        recipeSubcategoryDTO.setName("Рыба");
        when(recipeSubcategorySevice.save(any(RecipeSubcategoryDTO.class))).thenReturn(recipeSubcategoryDTO);
        ObjectMapper objectMapper = new ObjectMapper();
        mockMvc.perform(put("/recipesubcategories/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(recipeSubcategoryDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Рыба")));
    }

    @Test
    public void deleteRecipeSubcategoryTest() throws Exception{
        RecipeSubcategoryDTO recipeSubcategoryDTO = createRecipeSubcategoryDTO(1L, "Курица");
        when(recipeSubcategorySevice.findById(recipeSubcategoryDTO.getId())).thenReturn(recipeSubcategoryDTO);
        mockMvc.perform(delete("/recipesubcategories/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    public void checkUniqueNameRecipeSubcategoryTest() throws Exception{
        RecipeSubcategory recipeSubcategory = new RecipeSubcategory ( 1L, "Рыба");
        String name = "Рыба";
        when(recipeSubcategorySevice.findByName(name)).thenReturn(recipeSubcategory);
        mockMvc.perform(get("/recipesubcategories/checkRecipeSubcategoryUniqueName?name=" + name)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(String.valueOf(-1)));
    }
}
