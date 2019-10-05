package by.weekmenu.api.integrationTests;

import by.weekmenu.api.ApiApplication;
import by.weekmenu.api.dto.MealTypeDTO;
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
public class MealTypeIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MealTypeRepository mealTypeRepository;

    @Autowired
    private RecycleBinRepository recycleBinRepository;

    @Autowired
    private OwnershipRepository ownershipRepository;

    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private MenuRecipeRepository menuRecipeRepository;

    @Autowired
    private CookingMethodRepository cookingMethodRepository;

    @After
    public void cleanDB() {
        mealTypeRepository.deleteAll();
        recycleBinRepository.deleteAll();
        recipeRepository.deleteAll();
        menuRecipeRepository.deleteAll();
        cookingMethodRepository.deleteAll();
    }

    private MealTypeDTO createMealTypeDTO(Short id, String name, Integer priority) {
        MealTypeDTO mealTypeDTO = new MealTypeDTO();
        mealTypeDTO.setId(id);
        mealTypeDTO.setName(name);
        mealTypeDTO.setPriority(priority);
        mealTypeDTO.setArchived(false);
        return mealTypeDTO;
    }

    @Test
    @Transactional
    public void saveMealTypeIntegrationTest() throws Exception {
        MealTypeDTO mealTypeDTO = createMealTypeDTO(new Short("1"), "Завтрак", 5);
        ObjectMapper objectMapper = new ObjectMapper();
        mockMvc.perform(post(UrlConsts.PATH_MEAL_TYPES)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(mealTypeDTO)));
        Iterable<MealType> mealTypes = mealTypeRepository.findAll();
        assertThat(mealTypes).extracting(MealType::getName).containsOnly("Завтрак");
    }

    @Test
    public void getAllMealTypeIntegrationTest() throws Exception {
        MealType mealType1 = new MealType( "Завтрак",  false);
        MealType mealType2 = new MealType( "Обед",  false);
        mealTypeRepository.save(mealType1);
        mealTypeRepository.save(mealType2);
        mockMvc.perform(get(UrlConsts.PATH_MEAL_TYPES)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", is("Завтрак")))
                .andExpect(jsonPath("$[1].name", is("Обед")));
    }

    @Test
    public void updateMealTypeIntegrationTest() throws Exception {
        MealType mealType = new MealType( "Завтрак", false);
        mealTypeRepository.save(mealType);
        MealTypeDTO mealTypeDTO = createMealTypeDTO(mealType.getId(), "Обед", 3);
        ObjectMapper objectMapper = new ObjectMapper();
        mockMvc.perform(put(UrlConsts.PATH_MEAL_TYPES + "/" + mealType.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(mealTypeDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Обед")));
    }

    @Test
    public void deleteMealTypeIntegrationTest() throws Exception {
        MealType mealType = new MealType( "Завтрак", false);
        mealTypeRepository.save(mealType);
        mockMvc.perform(delete(UrlConsts.PATH_MEAL_TYPES + "/" + mealType.getId().toString())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        Iterable<RecycleBin> recycleBins = recycleBinRepository.findAll();
        assertThat(recycleBins).extracting(RecycleBin::getElementName).containsOnly("Завтрак");
        assertThat(recycleBins).extracting(RecycleBin::getEntityName).containsOnly("Прием пищи");
        assertThat(recycleBins).extracting(RecycleBin::getDeleteDate).isNotNull();

        Optional<MealType> mealTypeAfterSoftDelete = mealTypeRepository.findById(mealType.getId());
        assertThat(mealTypeAfterSoftDelete.get().isArchived()).isTrue();
    }

    @Test
    public void checkUniqueNameMealTypeIntegrationTest() throws Exception {
        MealType mealType = new MealType(new Short("1"), "Завтрак", 5, false);
        mealTypeRepository.save(mealType);
        mockMvc.perform(get(UrlConsts.PATH_MEAL_TYPES + "/checkMealTypeUniqueName?name=" + mealType.getName())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(String.valueOf(-1)));
    }

    @Test
    @Transactional
    public void checkConnectedElementsTest() throws Exception {
        MealType mealType = new MealType("Завтрак",  false);
        mealTypeRepository.save(mealType);

        CookingMethod cookingMethod = new CookingMethod("Жарка");
        cookingMethodRepository.save(cookingMethod);

        Recipe recipe = new Recipe();
        recipe.setName("Рецепт");
        recipe.setCookingTime(new Short("30"));
        recipe.setPreparingTime(new Short("15"));
        recipe.setPortions((short) 2);
        recipe.setImageLink("images/image.png");
        recipe.setSource("http://bestrecipes.com/best-recipe");
        recipe.setCookingMethod(cookingMethod);
        recipe.setOwnership(ownershipRepository.findByName(OwnershipName.ADMIN.name()).orElse(null));
        recipeRepository.save(recipe);

        Menu menu = new Menu("Бюджетное", true, new Ownership(OwnershipName.USER));
//        TODO
//        menuRepository.save(menu);

//        MenuRecipe menuRecipe = new MenuRecipe(menu, recipe, mealType, new DayOfWeek("Понедельник", "ПН"));
//        TODO
//        menuRecipeRepository.save(menuRecipe);

        mockMvc.perform(get(UrlConsts.PATH_MEAL_TYPES + "/checkConnectedElements/" + mealType.getId().toString())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }
}
