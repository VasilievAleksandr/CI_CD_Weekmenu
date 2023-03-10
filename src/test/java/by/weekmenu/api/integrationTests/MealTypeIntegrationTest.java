//package by.weekmenu.api.integrationTests;
//
//import by.weekmenu.api.ApiApplication;
//import by.weekmenu.api.dto.MealTypeDTO;
//import by.weekmenu.api.entity.*;
//import by.weekmenu.api.repository.*;
//import by.weekmenu.api.utils.UrlConsts;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.junit.After;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.MediaType;
//import org.springframework.test.context.junit4.SpringRunner;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.time.DayOfWeek;
//import java.util.Optional;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.hamcrest.Matchers.hasSize;
//import static org.hamcrest.Matchers.is;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@RunWith(SpringRunner.class)
//@SpringBootTest(classes = ApiApplication.class)
//@AutoConfigureMockMvc
//public class MealTypeIntegrationTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Autowired
//    private MealTypeRepository mealTypeRepository;
//
//    @Autowired
//    private RecycleBinRepository recycleBinRepository;
//
//    @Autowired
//    private OwnershipRepository ownershipRepository;
//
//    @Autowired
//    private RecipeRepository recipeRepository;
//
//    @Autowired
//    private MenuRecipeRepository menuRecipeRepository;
//
//    @Autowired
//    private CookingMethodRepository cookingMethodRepository;
//
//    @Autowired
//    private MenuRepository menuRepository;
//
//    @After
//    public void cleanDB() {
//        mealTypeRepository.deleteAll();
//        recycleBinRepository.deleteAll();
//        recipeRepository.deleteAll();
//        menuRecipeRepository.deleteAll();
//        cookingMethodRepository.deleteAll();
//    }
//
//    private MealTypeDTO createMealTypeDTO(String name, Integer priority) {
//        MealTypeDTO mealTypeDTO = new MealTypeDTO();
//        mealTypeDTO.setName(name);
//        mealTypeDTO.setPriority(priority);
//        mealTypeDTO.setArchived(false);
//        return mealTypeDTO;
//    }
//
//    @Test
//    @Transactional
//    public void saveMealTypeIntegrationTest() throws Exception {
//        MealTypeDTO mealTypeDTO = createMealTypeDTO("Завтракккк", 5);
//        ObjectMapper objectMapper = new ObjectMapper();
//        mockMvc.perform(post(UrlConsts.PATH_MEAL_TYPES)
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(objectMapper.writeValueAsBytes(mealTypeDTO)));
//        Iterable<MealType> mealTypes = mealTypeRepository.findAll();
//        assertThat(mealTypes).extracting(MealType::getName).contains("Завтракккк");
//    }
//
//    @Test
//    public void getAllMealTypeIntegrationTest() throws Exception {
//        MealType mealType1 = new MealType( "Завтракккк",  1);
//        MealType mealType2 = new MealType( "Обедддд",  2);
//        mealTypeRepository.save(mealType1);
//        mealTypeRepository.save(mealType2);
//        mockMvc.perform(get(UrlConsts.PATH_MEAL_TYPES)
//                .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$", hasSize(5)))
//                .andExpect(jsonPath("$[3].name", is("Завтракккк")))
//                .andExpect(jsonPath("$[4].name", is("Обедддд")));
//    }
//
//    @Test
//    public void updateMealTypeIntegrationTest() throws Exception {
//        MealType mealType = new MealType( "Завтрак", 1);
//        mealTypeRepository.save(mealType);
//        MealTypeDTO mealTypeDTO = createMealTypeDTO( "Обед", 3);
//        mealTypeDTO.setId(mealType.getId());
//        ObjectMapper objectMapper = new ObjectMapper();
//        mockMvc.perform(put(UrlConsts.PATH_MEAL_TYPES + "/" + mealType.getId())
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(objectMapper.writeValueAsBytes(mealTypeDTO)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.name", is("Обед")));
//    }
//
//    @Test
//    public void deleteMealTypeIntegrationTest() throws Exception {
//        MealType mealType = new MealType( "Завтрак", 1);
//        mealTypeRepository.save(mealType);
//        mockMvc.perform(delete(UrlConsts.PATH_MEAL_TYPES + "/" + mealType.getId().toString())
//                .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isNoContent());
//
//        Iterable<RecycleBin> recycleBins = recycleBinRepository.findAll();
//        assertThat(recycleBins).extracting(RecycleBin::getElementName).containsOnly("Завтрак");
//        assertThat(recycleBins).extracting(RecycleBin::getEntityName).containsOnly("Прием пищи");
//        assertThat(recycleBins).extracting(RecycleBin::getDeleteDate).isNotNull();
//
//        Optional<MealType> mealTypeAfterSoftDelete = mealTypeRepository.findById(mealType.getId());
//        assertThat(mealTypeAfterSoftDelete.get().isArchived()).isTrue();
//    }
//
//    @Test
//    public void checkUniqueNameMealTypeIntegrationTest() throws Exception {
//        MealType mealType = new MealType(Short.valueOf("1"), "Завтрак", 5, false);
//        mealTypeRepository.save(mealType);
//        mockMvc.perform(get(UrlConsts.PATH_MEAL_TYPES + "/checkMealTypeUniqueName?name=" + mealType.getName())
//                .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(content().string(String.valueOf(-1)));
//    }
//
//    @Test
//    @Transactional
//    public void checkConnectedElementsTest() throws Exception {
//        MealType mealType = new MealType("Завтрак",  1);
//        mealTypeRepository.save(mealType);
//
//        CookingMethod cookingMethod = new CookingMethod("Жарка");
//        cookingMethodRepository.save(cookingMethod);
//
//        Optional<Ownership> ownership = ownershipRepository.findByName(OwnershipName.ADMIN.name());
//
//        Recipe recipe = new Recipe();
//        recipe.setName("Рецепт");
//        recipe.setCookingTime(Short.valueOf("30"));
//        recipe.setActiveTime(Short.valueOf("15"));
//        recipe.setPortions((short) 2);
//        recipe.setImageLink("images/image.png");
//        recipe.setSource("http://bestrecipes.com/best-recipe");
//        recipe.setCookingMethod(cookingMethod);
//        ownership.ifPresent(recipe::setOwnership);
//        recipeRepository.save(recipe);
//
//        Menu menu = new Menu();
//        menu.setName("Бюджетное");
//        menu.setIsActive(true);
//        ownership.ifPresent(menu::setOwnership);
//        menuRepository.save(menu);
//        MenuRecipe menuRecipe = new MenuRecipe(menu, recipe, mealType, DayOfWeek.MONDAY);
//        menuRecipeRepository.save(menuRecipe);
//
//        mockMvc.perform(get(UrlConsts.PATH_MEAL_TYPES + "/checkConnectedElements/" + mealType.getId().toString())
//                .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$", hasSize(1)));
//    }
//}
