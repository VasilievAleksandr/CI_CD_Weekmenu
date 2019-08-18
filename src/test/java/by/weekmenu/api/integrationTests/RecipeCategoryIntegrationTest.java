package by.weekmenu.api.integrationTests;

import by.weekmenu.api.ApiApplication;
import by.weekmenu.api.dto.RecipeCategoryDTO;
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
public class RecipeCategoryIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RecipeCategoryRepository recipeCategoryRepository;

    @Autowired
    private RecycleBinRepository recycleBinRepository;

    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private CookingMethodRepository cookingMethodRepository;

    @Autowired
    private OwnershipRepository ownershipRepository;

    @After
    public void cleanDB() {
        recipeCategoryRepository.deleteAll();
        recycleBinRepository.deleteAll();
        recipeRepository.deleteAll();
        cookingMethodRepository.deleteAll();
    }

    @Test
    @Transactional
    public void saveRecipeCategoryIntegrationTest() throws Exception {
        RecipeCategoryDTO recipeCategoryDTO = new RecipeCategoryDTO();
        recipeCategoryDTO.setName("Обед");
        ObjectMapper objectMapper = new ObjectMapper();
        mockMvc.perform(post("/recipecategories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(recipeCategoryDTO)));
        Iterable<RecipeCategory> recipeCategories = recipeCategoryRepository.findAll();
        assertThat(recipeCategories).extracting(RecipeCategory::getName).containsOnly("Обед");
    }

    @Test
    public void getAllRecipeCategoryIntegrationTest() throws Exception {
        RecipeCategory recipeCategory1 = new RecipeCategory("Обед");
        RecipeCategory recipeCategory2 = new RecipeCategory("Ужин");
        recipeCategoryRepository.save(recipeCategory1);
        recipeCategoryRepository.save(recipeCategory2);
        mockMvc.perform(get("/recipecategories")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", is("Обед")))
                .andExpect(jsonPath("$[1].name", is("Ужин")));
    }

    @Test
    public void updateRecipeCategoryIntegrationTest() throws Exception {
        RecipeCategory recipeCategory = new RecipeCategory("Обед");
        recipeCategoryRepository.save(recipeCategory);
        RecipeCategoryDTO recipeCategoryDTO = new RecipeCategoryDTO();
        recipeCategoryDTO.setName("Ужин");
        ObjectMapper objectMapper = new ObjectMapper();
        mockMvc.perform(put("/recipecategories/" + recipeCategory.getId().toString())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(recipeCategoryDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Ужин")));
    }

    @Test
    public void deleteRecipeCategoryIntegrationTest() throws Exception {
        RecipeCategory recipeCategory = recipeCategoryRepository.save(new RecipeCategory("Обед"));
        mockMvc.perform(delete("/recipecategories/" + recipeCategory.getId().toString())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        Iterable<RecycleBin> recycleBins = recycleBinRepository.findAll();
        assertThat(recycleBins).extracting(RecycleBin::getElementName).containsOnly("Обед");
        assertThat(recycleBins).extracting(RecycleBin::getEntityName).containsOnly("Категория рецепта");
        assertThat(recycleBins).extracting(RecycleBin::getDeleteDate).isNotNull();

        Optional<RecipeCategory> recipeCategoryAfterSoftDelete = recipeCategoryRepository.findById(recipeCategory.getId());
        assertThat(recipeCategoryAfterSoftDelete.get().isArchived()).isTrue();
    }

    @Test
    public void checkUniqueNameRecipeCategoryIntegrationTest() throws Exception {
        RecipeCategory recipeCategory = new RecipeCategory("Обед");
        recipeCategoryRepository.save(recipeCategory);
        mockMvc.perform(get("/recipecategories/checkRecipeCategoryUniqueName?name=" + recipeCategory.getName())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(String.valueOf(-1)));
    }

    @Test
    @Transactional
    public void checkConnectedElementsTest() throws Exception {
        RecipeCategory recipeCategory = new RecipeCategory("Обед");
        recipeCategoryRepository.save(recipeCategory);
        Recipe recipe = new Recipe();
        recipe.setName("Гречневая каша");
        recipe.setCookingTime(new Short("30"));
        recipe.setPreparingTime(new Short("15"));
        recipe.setPortions((short)2);
        recipe.setImageLink("images/image.png");
        recipe.setSource("http://bestrecipes.com/best-recipe");
        recipe.setCookingMethod(cookingMethodRepository.save(new CookingMethod("Варка")));
        recipe.setOwnership(ownershipRepository.findByName(OwnershipName.ADMIN.name()).orElse(null));
        recipe.addRecipeCategory(recipeCategory);
        recipeRepository.save(recipe);

        mockMvc.perform(get("/recipecategories/checkConnectedElements/" + recipeCategory.getId().toString())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }
}
