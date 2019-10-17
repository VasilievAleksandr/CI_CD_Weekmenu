package by.weekmenu.api.controller;

import by.weekmenu.api.dto.RecipeCategoryDTO;
import by.weekmenu.api.entity.RecipeCategory;
import by.weekmenu.api.repository.MealTypeRepository;
import by.weekmenu.api.repository.OwnershipRepository;
import by.weekmenu.api.repository.UnitOfMeasureRepository;
import by.weekmenu.api.service.RecipeCategoryService;
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
@WebMvcTest(RecipeCategoryController.class)
public class RecipeCategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    RecipeCategoryService recipeCategorySevice;

    @MockBean
    private OwnershipRepository ownershipRepository;

    @MockBean
    private UnitOfMeasureRepository unitOfMeasureRepository;

    @MockBean
    private MealTypeRepository mealTypeRepository;

    private RecipeCategoryDTO createRecipeCategoryDTO(Long id, String name) {
        RecipeCategoryDTO recipeCategoryDTO = new RecipeCategoryDTO();
        recipeCategoryDTO.setId(id);
        recipeCategoryDTO.setName(name);
        return recipeCategoryDTO;
    }

    @Test
    public void saveRecipeCategoryTest() throws Exception {
        RecipeCategoryDTO recipeCategoryDTO = createRecipeCategoryDTO(1L, "Обед");
        when(recipeCategorySevice.save(any(RecipeCategoryDTO.class))).thenReturn(recipeCategoryDTO);
        ObjectMapper objectMapper = new ObjectMapper();
        mockMvc.perform(post(UrlConsts.PATH_RECIPECATEGORIES)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(recipeCategoryDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Обед")));
    }

    @Test
    public void findAllRecipeCategoriesTest() throws Exception {
        List recipeCategoryDTOs = new ArrayList();
        recipeCategoryDTOs.add(createRecipeCategoryDTO(1L, "Обед"));
        recipeCategoryDTOs.add(createRecipeCategoryDTO(2L, "Ужин"));
        when(recipeCategorySevice.findAll()).thenReturn(recipeCategoryDTOs);
        mockMvc.perform(get(UrlConsts.PATH_RECIPECATEGORIES)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("Обед")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].name", is("Ужин")))
                .andDo(print());
    }

    @Test
    public void updateRecipeCategoryTest() throws Exception {
        RecipeCategoryDTO recipeCategoryDTO = createRecipeCategoryDTO(1L, "Обед");
        when(recipeCategorySevice.findById(recipeCategoryDTO.getId())).thenReturn(recipeCategoryDTO);
        recipeCategoryDTO.setName("Ужин");
        when(recipeCategorySevice.save(any(RecipeCategoryDTO.class))).thenReturn(recipeCategoryDTO);
        ObjectMapper objectMapper = new ObjectMapper();
        mockMvc.perform(put(UrlConsts.PATH_RECIPECATEGORIES + "/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(recipeCategoryDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Ужин")));
    }

    @Test
    public void deleteRecipeCategoryTest() throws Exception {
        RecipeCategoryDTO recipeCategoryDTO = createRecipeCategoryDTO(1L, "Обед");
        when(recipeCategorySevice.findById(recipeCategoryDTO.getId())).thenReturn(recipeCategoryDTO);
        mockMvc.perform(delete(UrlConsts.PATH_RECIPECATEGORIES + "/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    public void checkUniqueNameRecipeCategoryTest() throws Exception {
        RecipeCategory recipeCategory = new RecipeCategory(1L, "Ужин");
        String name = "Ужин";
        when(recipeCategorySevice.findByName(name)).thenReturn(recipeCategory);
        mockMvc.perform(get(UrlConsts.PATH_RECIPECATEGORIES + "/checkRecipeCategoryUniqueName?name=" + name)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(String.valueOf(-1)));
    }

    @Test
    public void checkConnectedElementsTest() throws Exception {
        List<String> result = Collections.singletonList("рецепты: 1");
        when(recipeCategorySevice.checkConnectedElements(1L)).thenReturn(result);
        mockMvc.perform(get(UrlConsts.PATH_RECIPECATEGORIES + "/checkConnectedElements/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }
}
