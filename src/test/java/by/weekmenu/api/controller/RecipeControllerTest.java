package by.weekmenu.api.controller;

import by.weekmenu.api.dto.RecipeDTO;
import by.weekmenu.api.entity.Recipe;
import by.weekmenu.api.repository.OwnershipRepository;
import by.weekmenu.api.repository.UnitOfMeasureRepository;
import by.weekmenu.api.service.RecipeService;
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
@WebMvcTest(RecipeController.class)
public class RecipeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RecipeService recipeService;

    @MockBean
    private OwnershipRepository ownershipRepository;

    @MockBean
    private UnitOfMeasureRepository unitOfMeasureRepository;

    @MockBean
    private ModelMapper modelMapper;

    private RecipeDTO createRecipeDto(Long id, String name) {
        RecipeDTO recipeDto = new RecipeDTO();
        recipeDto.setId(id);
        recipeDto.setName(name);
        recipeDto.setCookingTime("30");
        recipeDto.setPreparingTime("15");
        recipeDto.setPortions((short)2);
        recipeDto.setImageLink("images/image.png");
        return recipeDto;
    }

    private Recipe createRecipe(Long id, String name) {
        Recipe recipe = new Recipe();
        recipe.setId(id);
        recipe.setName(name);
        recipe.setName(name);
        recipe.setCookingTime((short)30);
        recipe.setPreparingTime((short)15);
        recipe.setPortions((short)2);
        recipe.setImageLink("images/image.png");
        return recipe;
    }

    @Test
    public void findAllRecipes() throws Exception {
        List<RecipeDTO> list = new ArrayList<>();
        list.add(createRecipeDto(1L, "Жареная курица"));
        list.add(createRecipeDto(2L, "Батон"));
        when(recipeService.findAll()).thenReturn(list);

        mockMvc.perform(get("/recipes")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("Жареная курица")))
                .andExpect(jsonPath("$[0].cookingTime", is("30")))
                .andExpect(jsonPath("$[0].preparingTime", is("15")))
                .andExpect(jsonPath("$[0].portions", is(2)))
                .andExpect(jsonPath("$[0].imageLink", is("images/image.png")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].name", is("Батон")))
                .andExpect(jsonPath("$[1].cookingTime", is("30")))
                .andExpect(jsonPath("$[1].preparingTime", is("15")))
                .andExpect(jsonPath("$[1].portions", is(2)))
                .andExpect(jsonPath("$[1].imageLink", is("images/image.png")))
                .andDo(print());
    }

    @Test
    public void addRecipeTest() throws Exception{
        RecipeDTO recipeDto = createRecipeDto(1L, "Шашлык");
        when(recipeService.save(any(RecipeDTO.class))).thenReturn(recipeDto);
        ObjectMapper objectMapper = new ObjectMapper();
        mockMvc.perform(post("/recipes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(recipeDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Шашлык")))
                .andExpect(jsonPath("$.cookingTime", is("30")))
                .andExpect(jsonPath("$.preparingTime", is("15")))
                .andExpect(jsonPath("$.portions", is(2)))
                .andExpect(jsonPath("$.imageLink", is("images/image.png")));
    }

    @Test
    public void updateRecipeTest() throws Exception {
        RecipeDTO recipeDto = createRecipeDto(1L, "Шашлык");
        when(recipeService.findById(recipeDto.getId())).thenReturn(recipeDto);
        recipeDto.setName("Шашлык из курицы");
        recipeDto.setPortions((short)3);
        when(modelMapper.map(any(), any())).thenReturn(recipeDto);
        when(recipeService.save(any(RecipeDTO.class))).thenReturn(recipeDto);
        ObjectMapper objectMapper = new ObjectMapper();
        mockMvc.perform(put("/recipes/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(recipeDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Шашлык из курицы")))
                .andExpect(jsonPath("$.cookingTime", is("30")))
                .andExpect(jsonPath("$.preparingTime", is("15")))
                .andExpect(jsonPath("$.portions", is(3)))
                .andExpect(jsonPath("$.imageLink", is("images/image.png")));
    }

    @Test
    public void deleteRecipe() throws Exception{
        RecipeDTO recipeDto = createRecipeDto(1L, "Шашлык");
        when(recipeService.findById(recipeDto.getId())).thenReturn(recipeDto);
        mockMvc.perform(delete("/recipes/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    public void checkUniqueName() throws Exception{
        Recipe recipe = createRecipe(1L, "Шашлык");
        String name = "Шашлык";
        when(recipeService.findByName(name)).thenReturn(recipe);
        mockMvc.perform(get("/recipes/checkUniqueName?name=" + name)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(String.valueOf(-1)));
    }

    @Test
    public void checkConnectedElementsTest() throws Exception {
        List<String> result = Collections.singletonList("меню: 1");
        when(recipeService.checkConnectedElements(1L)).thenReturn(result);
        mockMvc.perform(get("/recipes/checkConnectedElements/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }
}