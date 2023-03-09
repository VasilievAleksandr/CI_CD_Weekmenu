package by.weekmenu.api.integrationTests;

import by.weekmenu.api.ApiApplication;
import by.weekmenu.api.dto.CookingMethodDTO;
import by.weekmenu.api.entity.*;
import by.weekmenu.api.repository.*;
import by.weekmenu.api.utils.UrlConsts;
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
public class CookingMethodIntegrationTest {

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
        cookingMethodRepository.deleteAll();
        recycleBinRepository.deleteAll();
        recipeCategoryRepository.deleteAll();
        recipeRepository.deleteAll();
    }

    @Test
    @Transactional
    public void saveCookingMethodIntegrationTest() throws Exception {
        CookingMethodDTO cookingMethodDTO = new CookingMethodDTO();
        cookingMethodDTO.setName("Жарка");
        ObjectMapper objectMapper = new ObjectMapper();
        mockMvc.perform(post(UrlConsts.PATH_COOKINGMETHODS)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(cookingMethodDTO)));
        Iterable<CookingMethod> cookingMethods = cookingMethodRepository.findAll();
        assertThat(cookingMethods).extracting(CookingMethod::getName).containsOnly("Жарка");
    }

    @Test
    public void getAllCookingMethodIntegrationTest() throws Exception {
        CookingMethod cookingMethod1 = new CookingMethod("Жарка");
        CookingMethod cookingMethod2 = new CookingMethod("Варка");
        cookingMethodRepository.save(cookingMethod1);
        cookingMethodRepository.save(cookingMethod2);
        mockMvc.perform(get(UrlConsts.PATH_COOKINGMETHODS)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", is("Жарка")))
                .andExpect(jsonPath("$[1].name", is("Варка")));
    }

    @Test
    public void updateCookingMethodIntegrationTest() throws Exception {
        CookingMethod cookingMethod = new CookingMethod("Жарка");
        cookingMethodRepository.save(cookingMethod);
        CookingMethodDTO cookingMethodDTO = new CookingMethodDTO();
        cookingMethodDTO.setName("Жарка");
        ObjectMapper objectMapper = new ObjectMapper();
        mockMvc.perform(put(UrlConsts.PATH_COOKINGMETHODS + "/" + cookingMethod.getId().toString())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(cookingMethodDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Жарка")));
    }

    @Test
    public void deleteCookingMethodIntegrationTest() throws Exception {
        CookingMethod cookingMethod = cookingMethodRepository.save(new CookingMethod("Жарка"));
        mockMvc.perform(delete(UrlConsts.PATH_COOKINGMETHODS + "/" + cookingMethod.getId().toString())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        Iterable<RecycleBin> recycleBins = recycleBinRepository.findAll();
        assertThat(recycleBins).extracting(RecycleBin::getElementName).containsOnly("Жарка");
        assertThat(recycleBins).extracting(RecycleBin::getEntityName).containsOnly("Способ приготовления блюда");
        assertThat(recycleBins).extracting(RecycleBin::getDeleteDate).isNotNull();

        Optional<CookingMethod> cookingMethodAfterSoftDelete = cookingMethodRepository.findById(cookingMethod.getId());
        assertThat(cookingMethodAfterSoftDelete.get().isArchived()).isTrue();
    }

    @Test
    @Transactional
    public void checkConnectedElementsTest() throws Exception {
        CookingMethod cookingMethod = new CookingMethod("Жарка");
        Recipe recipe = new Recipe();
        recipe.setName("Гречневая каша");
        recipe.setCookingTime(new Short("30"));
        recipe.setActiveTime(new Short("15"));
        recipe.setPortions((short) 2);
        recipe.setImageLink("images/image.png");
        recipe.setSource("http://bestrecipes.com/best-recipe");
        recipe.setCookingMethod(cookingMethod);
        recipe.setOwnership(ownershipRepository.findByName(OwnershipName.ADMIN.name()).orElse(null));
        cookingMethodRepository.save(cookingMethod);
        recipeRepository.save(recipe);
        mockMvc.perform(get(UrlConsts.PATH_COOKINGMETHODS + "/checkConnectedElements/" + cookingMethod.getId().toString())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }
}
