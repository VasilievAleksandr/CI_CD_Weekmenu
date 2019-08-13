package by.weekmenu.api.integrationTests;

import by.weekmenu.api.ApiApplication;
import by.weekmenu.api.dto.RecipeSubcategoryDTO;
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
public class RecipeSubcategoryIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RecipeSubcategoryRepository recipeSubcategoryRepository;

    @Autowired
    private RecycleBinRepository recycleBinRepository;

    @Autowired
    private CookingMethodRepository cookingMethodRepository;

    @Autowired
    private OwnershipRepository ownershipRepository;

    @Autowired
    private RecipeRepository recipeRepository;

    @After
    public void cleanDB() {
        recipeSubcategoryRepository.deleteAll();
        recycleBinRepository.deleteAll();
    }

    @Test
    @Transactional
    public void saveRecipeSubcategoryIntegrationTest() throws Exception {
        RecipeSubcategoryDTO recipeSubcategoryDTO = new RecipeSubcategoryDTO();
        recipeSubcategoryDTO.setName("Курица");
        ObjectMapper objectMapper = new ObjectMapper();
        mockMvc.perform(post("/recipesubcategories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(recipeSubcategoryDTO)));
        Iterable<RecipeSubcategory> recipeSubcategories = recipeSubcategoryRepository.findAll();
        assertThat(recipeSubcategories).extracting(RecipeSubcategory::getName).containsOnly("Курица");
    }

    @Test
    public void getAllRecipeSubcategoryIntegrationTest() throws Exception {
        RecipeSubcategory recipeSubcategory1 = new RecipeSubcategory("Курица");
        RecipeSubcategory recipeSubcategory2 = new RecipeSubcategory("Рыба");
        recipeSubcategoryRepository.save(recipeSubcategory1);
        recipeSubcategoryRepository.save(recipeSubcategory2);
        mockMvc.perform(get("/recipesubcategories")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", is("Курица")))
                .andExpect(jsonPath("$[1].name", is("Рыба")));
    }

    @Test
    public void updateRecipeSubcategoryIntegrationTest() throws Exception {
        RecipeSubcategory recipeSubcategory = new RecipeSubcategory("Курица");
        recipeSubcategoryRepository.save(recipeSubcategory);
        RecipeSubcategoryDTO recipeSubcategoryDTO = new RecipeSubcategoryDTO();
        recipeSubcategoryDTO.setName("Рыба");
        ObjectMapper objectMapper = new ObjectMapper();
        mockMvc.perform(put("/recipesubcategories/" + recipeSubcategory.getId().toString())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(recipeSubcategoryDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Рыба")));
    }

    @Test
    public void deleteRecipeSubcategoryIntegrationTest() throws Exception {
        RecipeSubcategory recipeSubcategory = recipeSubcategoryRepository.save(new RecipeSubcategory("Рыба"));
        mockMvc.perform(delete("/recipesubcategories/" + recipeSubcategory.getId().toString())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        Iterable<RecycleBin> recycleBins = recycleBinRepository.findAll();
        assertThat(recycleBins).extracting(RecycleBin::getElementName).containsOnly("Рыба");
        assertThat(recycleBins).extracting(RecycleBin::getEntityName).containsOnly("Подкатегория рецепта");
        assertThat(recycleBins).extracting(RecycleBin::getDeleteDate).isNotNull();

        Optional<RecipeSubcategory> recipeSubcategoryAfterSoftDelete = recipeSubcategoryRepository.findById(recipeSubcategory.getId());
        assertThat(recipeSubcategoryAfterSoftDelete.get().isArchived()).isTrue();
    }

    @Test
    public void checkUniqueNameRecipeSubcategoryIntegrationTest() throws Exception {
        RecipeSubcategory recipeSubcategory = new RecipeSubcategory("Рыба");
        recipeSubcategoryRepository.save(recipeSubcategory);
        mockMvc.perform(get("/recipesubcategories/checkRecipeSubcategoryUniqueName?name=" + recipeSubcategory.getName())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(String.valueOf(-1)));
    }

    @Test
    @Transactional
    public void checkConnectedElementsTest() throws Exception {
        RecipeSubcategory recipeSubcategory = recipeSubcategoryRepository.save(new RecipeSubcategory("Рыба"));
        Recipe recipe = new Recipe();
        recipe.setName("Гречневая каша");
        recipe.setCookingTime(new Short("30"));
        recipe.setPreparingTime(new Short("15"));
        recipe.setPortions((short)2);
        recipe.setImageLink("images/image.png");
        recipe.setSource("http://bestrecipes.com/best-recipe");
        recipe.setCookingMethod(cookingMethodRepository.save(new CookingMethod("Варка")));
        recipe.setOwnership(ownershipRepository.findByName(OwnershipName.ADMIN.name()).orElse(null));
        //TODO recipe set recipeCategory
        recipeRepository.save(recipe);

        mockMvc.perform(get("/recipesubcategories/checkConnectedElements/" + recipeSubcategory.getId().toString())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }
}
