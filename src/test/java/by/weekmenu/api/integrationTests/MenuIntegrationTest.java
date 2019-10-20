package by.weekmenu.api.integrationTests;

import by.weekmenu.api.ApiApplication;
import by.weekmenu.api.dto.*;
import by.weekmenu.api.entity.*;
import by.weekmenu.api.entity.Currency;
import by.weekmenu.api.repository.*;
import by.weekmenu.api.service.IngredientService;
import by.weekmenu.api.service.MenuService;
import by.weekmenu.api.service.RecipeService;
import by.weekmenu.api.service.UnitOfMeasureService;
import by.weekmenu.api.utils.UrlConsts;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ApiApplication.class)
@AutoConfigureMockMvc
public class MenuIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private OwnershipRepository ownershipRepository;

    @Autowired
    private UnitOfMeasureService unitOfMeasureService;

    @Autowired
    private CountryRepository countryRepository;

    @Autowired
    private RegionRepository regionRepository;

    @Autowired
    private CurrencyRepository currencyRepository;

    @Autowired
    private IngredientRepository ingredientRepository;

    @Autowired
    private CookingMethodRepository cookingMethodRepository;

    @Autowired
    private CookingStepRepository cookingStepRepository;

    @Autowired
    private IngredientPriceRepository ingredientPriceRepository;

    @Autowired
    private IngredientUnitOfMeasureRepository ingredientUnitOfMeasureRepository;

    @Autowired
    private RecipePriceRepository recipePriceRepository;

    @Autowired
    private RecipeIngredientRepository recipeIngredientRepository;

    @Autowired
    private MealTypeRepository mealTypeRepository;

    @Autowired
    private IngredientService ingredientService;

    @Autowired
    private RecycleBinRepository recycleBinRepository;

    @Autowired
    private MenuRecipeRepository menuRecipeRepository;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private RecipeCategoryRepository recipeCategoryRepository;

    @Autowired
    private RecipeSubcategoryRepository recipeSubcategoryRepository;

    @Autowired
    private MenuCategoryRepository menuCategoryRepository;

    @Autowired
    private MenuService menuService;

    @Autowired
    private UnitOfMeasureRepository unitOfMeasureRepository;

    @Autowired
    private RecipeService recipeService;

    @Autowired
    private MenuPriceRepository menuPriceRepository;

    @Before
    public void createBaseData() {
        if (unitOfMeasureRepository.findAll().spliterator().getExactSizeIfKnown()==1) {
            createUnitOfMeasureDtos();
        }
        if (mealTypeRepository.findAll().spliterator().getExactSizeIfKnown()==0) {
            mealTypeRepository.save(new MealType("Завтрак", 10));
            mealTypeRepository.save(new MealType("Обед", 20));
        }
        recipeCategoryRepository.save(new RecipeCategory("Обед"));
        recipeCategoryRepository.save(new RecipeCategory("Ужин"));
        recipeSubcategoryRepository.save(new RecipeSubcategory("Курица"));
        recipeSubcategoryRepository.save(new RecipeSubcategory("Мясо"));
        createRegion("Минск");
        createCookingMethod("Варка");
        createIngredient("Гречка");
        createRecipe("Гречневая каша");
        createRecipe("Гречка с мясом");
        menuCategoryRepository.save(new MenuCategory("Бюджетное", false));
    }

    @After
    public void cleanDB() {
        ingredientPriceRepository.deleteAll();
        recipePriceRepository.deleteAll();
        menuPriceRepository.deleteAll();
        regionRepository.deleteAll();
        countryRepository.deleteAll();
        currencyRepository.deleteAll();
        ingredientUnitOfMeasureRepository.deleteAll();
        menuRecipeRepository.deleteAll();
        recipeIngredientRepository.deleteAll();
        ingredientRepository.deleteAll();
        cookingStepRepository.deleteAll();
        recipeRepository.deleteAll();
        recipeCategoryRepository.deleteAll();
        recipeSubcategoryRepository.deleteAll();
        cookingMethodRepository.deleteAll();
        recycleBinRepository.deleteAll();
        menuRepository.deleteAll();
        menuCategoryRepository.deleteAll();
    }

    private void createUnitOfMeasureDtos() {
        UnitOfMeasureDTO unitOfMeasureDTO1 = new UnitOfMeasureDTO();
        unitOfMeasureDTO1.setFullName("Стакан");
        unitOfMeasureDTO1.setShortName("Ст");
        unitOfMeasureService.save(unitOfMeasureDTO1);
        UnitOfMeasureDTO unitOfMeasureDTO2 = new UnitOfMeasureDTO();
        unitOfMeasureDTO2.setFullName("Ложка");
        unitOfMeasureDTO2.setShortName("Лж");
        unitOfMeasureService.save(unitOfMeasureDTO2);
    }

    private void createRegion(String name) {
        Currency currency = new Currency("Бел. руб.", "BYN", false);
        Country country = new Country();
        country.setName("Беларусь");
        country.setAlphaCode2("BY");
        country.setCurrency(currency);
        countryRepository.save(country);
        Region region = new Region();
        region.setName(name);
        region.setCountry(countryRepository.findByNameIgnoreCase("Беларусь").orElse(null));
        regionRepository.save(region);
    }

    private void createCookingMethod(String name) {
        CookingMethod cookingMethod = new CookingMethod(name);
        cookingMethodRepository.save(cookingMethod);
    }

    private void createIngredient(String name) {
        IngredientDTO ingredientDto = new IngredientDTO();
        ingredientDto.setName(name);
        ingredientDto.setCalories(new BigDecimal("100"));
        ingredientDto.setCarbs(new BigDecimal("100"));
        ingredientDto.setFats(new BigDecimal("100"));
        ingredientDto.setProteins(new BigDecimal("100"));

        Map<String, BigDecimal> map = new HashMap<>();
        map.put("Стакан", new BigDecimal(100));
        map.put("Ложка", new BigDecimal(20));
        ingredientDto.setUnitOfMeasureEquivalent(map);

        Set<IngredientPriceDTO> ingredientPrices = getIngredientPriceDTOS();
        ingredientDto.setIngredientPrices(ingredientPrices);
        ingredientService.save(ingredientDto);
    }

    private Set<IngredientPriceDTO> getIngredientPriceDTOS() {
        Set<IngredientPriceDTO> ingredientPrices = new HashSet<>();
        IngredientPriceDTO ingredientPriceDTO = new IngredientPriceDTO();
        ingredientPriceDTO.setRegionName("Минск");
        ingredientPriceDTO.setCurrencyCode("BYN");
        ingredientPriceDTO.setQuantity(new BigDecimal("1"));
        ingredientPriceDTO.setUnitOfMeasureName("Стакан");
        ingredientPriceDTO.setPriceValue(new BigDecimal("12"));
        ingredientPrices.add(ingredientPriceDTO);
        return ingredientPrices;
    }

    private RecipeDTO createRecipe(String name) {
        RecipeDTO recipeDto = new RecipeDTO();
        recipeDto.setName(name);
        recipeDto.setCookingTime("30");
        recipeDto.setPreparingTime("15");
        recipeDto.setPortions((short)2);
        recipeDto.setImageLink("images/image.png");
        recipeDto.setSource("http://bestrecipes.com/best-recipe");
        recipeDto.setCookingMethodName("Варка");
        recipeDto.setOwnershipName("ADMIN");
        recipeDto.setCategoryNames(new HashSet<>(Arrays.asList("Обед", "Ужин")));
        recipeDto.setSubcategoryNames(new HashSet<>(Arrays.asList("Курица", "Мясо")));

        Set<RecipeIngredientDTO> recipeIngredients = getRecipeIngredients();
        recipeDto.setRecipeIngredients(recipeIngredients);

        Set<CookingStepDTO> cookingSteps = getCookingSteps();
        recipeDto.setCookingSteps(cookingSteps);
        return recipeService.save(recipeDto);
    }

    private Set<CookingStepDTO> getCookingSteps() {
        Set<CookingStepDTO> cookingSteps = new HashSet<>();
        CookingStepDTO cookingStepDto = new CookingStepDTO();
        cookingStepDto.setPriority(1);
        cookingStepDto.setDescription("Нарежьте овощи");
        cookingSteps.add(cookingStepDto);
        return cookingSteps;
    }

    private Set<RecipeIngredientDTO> getRecipeIngredients() {
        Set<RecipeIngredientDTO> recipeIngredients = new HashSet<>();
        RecipeIngredientDTO recipeIngredientDTO = new RecipeIngredientDTO();
        recipeIngredientDTO.setIngredientName("Гречка");
        recipeIngredientDTO.setUnitOfMeasureShortName("Ст");
        recipeIngredientDTO.setQuantity(new BigDecimal("1"));
        recipeIngredients.add(recipeIngredientDTO);
        return recipeIngredients;
    }

    private MenuDTO createMenuDTO(String name) {
        MenuDTO menuDTO = new MenuDTO();
        menuDTO.setName(name);
        menuDTO.setMenuCategoryName("Бюджетное");
        menuDTO.setAuthorName("Повар");
        menuDTO.setAuthorImageLink("/images/photo.jpg");
        menuDTO.setMenuDescription("Очень вкусно");
        menuDTO.setIsActive(true);
        menuDTO.setOwnershipName("ADMIN");
        menuDTO.setMenuRecipeDTOS(getMenuRecipeDTOS());
        menuDTO.setMenuPriceDTOS(getMenuPriceDTOS());
        return menuDTO;
    }

    private Set<MenuPriceDTO> getMenuPriceDTOS() {
        Set<MenuPriceDTO> menuPriceDTOS = new HashSet<>();
        MenuPriceDTO menuPriceDTO = new MenuPriceDTO();
        menuPriceDTO.setMenuName("Вкусное меню");
        menuPriceDTO.setCurrencyCode("BYN");
        menuPriceDTO.setPriceValue("100");
        menuPriceDTO.setRegionName("Минск");
        menuPriceDTOS.add(menuPriceDTO);
        return menuPriceDTOS;
    }

    private Set<MenuRecipeDTO> getMenuRecipeDTOS() {
        Set<MenuRecipeDTO> menuRecipeDTOS = new HashSet<>();
        MenuRecipeDTO menuRecipeDTO = new MenuRecipeDTO();
        menuRecipeDTO.setRecipeName("Гречневая каша");
        menuRecipeDTO.setMealTypeName("Завтрак");
        menuRecipeDTO.setDayOfWeek(DayOfWeek.MONDAY);
        menuRecipeDTOS.add(menuRecipeDTO);
        MenuRecipeDTO menuRecipeDTO1 = new MenuRecipeDTO();
        menuRecipeDTO1.setRecipeName("Гречка с мясом");
        menuRecipeDTO1.setMealTypeName("Обед");
        menuRecipeDTO1.setDayOfWeek(DayOfWeek.WEDNESDAY);
        menuRecipeDTOS.add(menuRecipeDTO1);
        return menuRecipeDTOS;
    }

    @Test
    @Transactional
    public void saveMenuTest() throws Exception {
        MenuDTO menuDTO = createMenuDTO("Вкусное меню");
        ObjectMapper objectMapper = new ObjectMapper();
        mockMvc.perform(post(UrlConsts.PATH_MENUS)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(menuDTO)))
                .andDo(print());

        Iterable<Menu> menus = menuRepository.findAll();
        assertThat(menus).extracting(Menu::getName).containsOnly("Вкусное меню");
        assertThat(menus).extracting(Menu::getAuthorName).containsOnly("Повар");
        assertThat(menus).extracting(Menu::getAuthorImageLink).containsOnly("/images/photo.jpg");
        assertThat(menus).extracting(Menu::getMenuDescription).containsOnly("Очень вкусно");
        assertThat(menus).extracting(Menu::getIsActive).contains(true);
        assertThat(menus).extracting(Menu::getMenuCategory)
                .containsOnly(menuCategoryRepository.findByNameIgnoreCase("Бюджетное").get());
        assertThat(menus).extracting(Menu::getOwnership)
                .containsOnly(ownershipRepository.findByName("ADMIN").get());
        //check calculations
        assertThat(menus).extracting(Menu::getCalories).containsOnly(new BigDecimal("50.0"));
        assertThat(menus).extracting(Menu::getProteins).containsOnly(new BigDecimal("50.0"));
        assertThat(menus).extracting(Menu::getFats).containsOnly(new BigDecimal("50.0"));
        assertThat(menus).extracting(Menu::getCarbs).containsOnly(new BigDecimal("50.0"));

        List<MenuPrice> menuPrices = menuPriceRepository.findAllById_MenuId(menus.iterator().next().getId());
        assertThat(menuPrices).extracting(MenuPrice::getRegion)
                .containsOnly(regionRepository.findByNameIgnoreCase("Минск").get());
        assertThat(menuPrices).extracting(MenuPrice::getPriceValue)
                .containsOnly(new BigDecimal("12.00"));

        List<MenuRecipe> menuRecipes = menuRecipeRepository.findAllByMenu_Id(menus.iterator().next().getId());
        assertThat(menuRecipes.size()).isEqualTo(2);
        assertThat(menuRecipes).extracting(MenuRecipe::getMenu)
                .contains(menuRepository.findByNameIgnoreCase("Вкусное меню").get());
        assertThat(menuRecipes).extracting(MenuRecipe::getRecipe)
                .contains(recipeRepository.findByNameIgnoreCase("Гречневая каша").get());
        assertThat(menuRecipes).extracting(MenuRecipe::getRecipe)
                .contains(recipeRepository.findByNameIgnoreCase("Гречка с мясом").get());
        assertThat(menuRecipes).extracting(MenuRecipe::getMealType)
                .contains(mealTypeRepository.findByNameIgnoreCase("Завтрак").get());
        assertThat(menuRecipes).extracting(MenuRecipe::getMealType)
                .contains(mealTypeRepository.findByNameIgnoreCase("Обед").get());
    }

    @Test
    public void getAllMenusTest() throws Exception {
        menuService.save(createMenuDTO("Вкусное меню"));
        menuService.save(createMenuDTO("Вегетарианское меню"));
        mockMvc.perform(get(UrlConsts.PATH_MENUS).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", is("Вкусное меню")))
                .andExpect(jsonPath("$[0].menuCategoryName", is("Бюджетное")))
                .andExpect(jsonPath("$[0].authorName", is("Повар")))
                .andExpect(jsonPath("$[0].authorImageLink", is("/images/photo.jpg")))
                .andExpect(jsonPath("$[0].menuDescription", is("Очень вкусно")))
                .andExpect(jsonPath("$[0].isActive", is (true)))
                .andExpect(jsonPath("$[0].ownershipName", is("ADMIN")))
                .andExpect(jsonPath("$[0].menuRecipeDTOS", iterableWithSize(2)))
                .andExpect(jsonPath("$[0].menuPriceDTOS", iterableWithSize(1)))
                .andExpect(jsonPath("$[0].calories", is(50.0)))
                .andExpect(jsonPath("$[0].proteins", is(50.0)))
                .andExpect(jsonPath("$[0].fats", is(50.0)))
                .andExpect(jsonPath("$[0].carbs", is(50.0)))
                .andExpect(jsonPath("$[1].name", is("Вегетарианское меню")))
                .andExpect(jsonPath("$[1].menuCategoryName", is("Бюджетное")))
                .andExpect(jsonPath("$[1].authorName", is("Повар")))
                .andExpect(jsonPath("$[1].authorImageLink", is("/images/photo.jpg")))
                .andExpect(jsonPath("$[1].menuDescription", is("Очень вкусно")))
                .andExpect(jsonPath("$[1].isActive", is (true)))
                .andExpect(jsonPath("$[1].ownershipName", is("ADMIN")))
                .andExpect(jsonPath("$[1].menuRecipeDTOS", iterableWithSize(2)))
                .andExpect(jsonPath("$[1].menuPriceDTOS", iterableWithSize(1)))
                .andExpect(jsonPath("$[1].calories", is(50.0)))
                .andExpect(jsonPath("$[1].proteins", is(50.0)))
                .andExpect(jsonPath("$[1].fats", is(50.0)))
                .andExpect(jsonPath("$[1].carbs", is(50.0)));
    }

    @Test
    public void updateMenuTest() throws Exception {
        MenuDTO menuDTO = menuService.save(createMenuDTO("Вкусное меню"));
        menuDTO.setName("Вкусняшка");
        menuDTO.setMenuDescription("Супер вкусно!");
        ObjectMapper objectMapper = new ObjectMapper();

        mockMvc.perform(put(UrlConsts.PATH_MENUS + "/" + menuDTO.getId().toString())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(menuDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Вкусняшка")))
                .andExpect(jsonPath("$.menuCategoryName", is("Бюджетное")))
                .andExpect(jsonPath("$.authorName", is("Повар")))
                .andExpect(jsonPath("$.authorImageLink", is("/images/photo.jpg")))
                .andExpect(jsonPath("$.menuDescription", is("Супер вкусно!")))
                .andExpect(jsonPath("$.isActive", is (true)))
                .andExpect(jsonPath("$.ownershipName", is("ADMIN")))
                .andExpect(jsonPath("$.menuRecipeDTOS", iterableWithSize(2)))
                .andExpect(jsonPath("$.menuPriceDTOS", iterableWithSize(1)))
                .andExpect(jsonPath("$.calories", is(50.0)))
                .andExpect(jsonPath("$.proteins", is(50.0)))
                .andExpect(jsonPath("$.fats", is(50.0)))
                .andExpect(jsonPath("$.carbs", is(50.0)));
    }

    @Test
    public void deleteMenuTest() throws Exception{
        MenuDTO menuDTO = menuService.save(createMenuDTO("Вкусное меню"));
        mockMvc.perform(delete(UrlConsts.PATH_MENUS + "/" + menuDTO.getId().toString())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        Iterable<RecycleBin> recycleBins = recycleBinRepository.findAll();
        assertThat(recycleBins).extracting(RecycleBin::getElementName).containsOnly("Вкусное меню");
        assertThat(recycleBins).extracting(RecycleBin::getEntityName).containsOnly("Меню");
        assertThat(recycleBins).extracting(RecycleBin::getDeleteDate).isNotNull();

        Optional<Menu> menu = menuRepository.findById(menuDTO.getId());
        assertThat(menu.get().isArchived()).isTrue();
    }

    @Test
    public void getMenuTest() throws Exception {
        MenuDTO menuDTO = menuService.save(createMenuDTO("Вкусное меню"));
        mockMvc.perform(get(UrlConsts.PATH_MENUS + "/" + menuDTO.getId().toString())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isFound());
    }

}
