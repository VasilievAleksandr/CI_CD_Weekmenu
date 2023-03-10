//package by.weekmenu.api.integrationTests;
//
//import by.weekmenu.api.ApiApplication;
//import by.weekmenu.api.dto.*;
//import by.weekmenu.api.entity.*;
//import by.weekmenu.api.entity.Currency;
//import by.weekmenu.api.repository.*;
//import by.weekmenu.api.service.IngredientService;
//import by.weekmenu.api.service.RecipeService;
//import by.weekmenu.api.service.UnitOfMeasureService;
//import by.weekmenu.api.utils.UrlConsts;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.junit.After;
//import org.junit.Before;
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
//import java.math.BigDecimal;
//import java.time.DayOfWeek;
//import java.util.*;
//import java.util.stream.Collectors;
//import java.util.stream.Stream;
//
//import static org.assertj.core.api.Assertions.*;
//import static org.hamcrest.Matchers.*;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@RunWith(SpringRunner.class)
//@SpringBootTest(classes = ApiApplication.class)
//@AutoConfigureMockMvc
//public class RecipeIntegrationTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Autowired
//    private RecipeRepository recipeRepository;
//
//    @Autowired
//    private OwnershipRepository ownershipRepository;
//
//    @Autowired
//    private UnitOfMeasureRepository unitOfMeasureRepository;
//
//    @Autowired
//    private UnitOfMeasureService unitOfMeasureService;
//
//    @Autowired
//    private CountryRepository countryRepository;
//
//    @Autowired
//    private RegionRepository regionRepository;
//
//    @Autowired
//    private CurrencyRepository currencyRepository;
//
//    @Autowired
//    private IngredientRepository ingredientRepository;
//
//    @Autowired
//    private CookingMethodRepository cookingMethodRepository;
//
//    @Autowired
//    private CookingStepRepository cookingStepRepository;
//
//    @Autowired
//    private IngredientPriceRepository ingredientPriceRepository;
//
//    @Autowired
//    private IngredientUnitOfMeasureRepository ingredientUnitOfMeasureRepository;
//
//    @Autowired
//    private RecipePriceRepository recipePriceRepository;
//
//    @Autowired
//    private RecipeIngredientRepository recipeIngredientRepository;
//
//    @Autowired
//    private IngredientService ingredientService;
//
//    @Autowired
//    private RecycleBinRepository recycleBinRepository;
//
//    @Autowired
//    private MenuRecipeRepository menuRecipeRepository;
//
//    @Autowired
//    private MealTypeRepository mealTypeRepository;
//
//    @Autowired
//    private MenuRepository menuRepository;
//
//    @Autowired
//    private RecipeCategoryRepository recipeCategoryRepository;
//
//    @Autowired
//    private RecipeSubcategoryRepository recipeSubcategoryRepository;
//
//    @Autowired
//    private IngredientCategoryRepository ingredientCategoryRepository;
//
//    @Autowired
//    private RecipeService recipeService;
//
//    @Before
//    public void createBaseData() {
//        if (ownershipRepository.findAll().spliterator().getExactSizeIfKnown() == 0) {
//            ownershipRepository.save(new Ownership(OwnershipName.ADMIN));
//            ownershipRepository.save(new Ownership(OwnershipName.USER));
//        }
//        if (unitOfMeasureRepository.findAll().spliterator().getExactSizeIfKnown() == 1) {
//            createUnitOfMeasureDtos();
//        }
//        if (ingredientCategoryRepository.findAll().spliterator().getExactSizeIfKnown() == 0) {
//            ingredientCategoryRepository.save(new IngredientCategory("Milk", false));
//        }
//        recipeCategoryRepository.save(new RecipeCategory("????????"));
//        recipeCategoryRepository.save(new RecipeCategory("????????"));
//        recipeSubcategoryRepository.save(new RecipeSubcategory("????????????"));
//        recipeSubcategoryRepository.save(new RecipeSubcategory("????????"));
//        createRegion("??????????");
//        createCookingMethod("??????????");
//        createIngredient("????????????");
//    }
//
//    @After
//    public void cleanDB() {
//        ingredientPriceRepository.deleteAll();
//        recipePriceRepository.deleteAll();
//        regionRepository.deleteAll();
//        countryRepository.deleteAll();
//        currencyRepository.deleteAll();
//        ingredientUnitOfMeasureRepository.deleteAll();
//        recipeIngredientRepository.deleteAll();
//        ingredientRepository.deleteAll();
//        cookingStepRepository.deleteAll();
//        recipeRepository.deleteAll();
//        recipeCategoryRepository.deleteAll();
//        recipeSubcategoryRepository.deleteAll();
//        cookingMethodRepository.deleteAll();
//        recycleBinRepository.deleteAll();
//        ingredientCategoryRepository.deleteAll();
//    }
//
//    private void createUnitOfMeasureDtos() {
//        UnitOfMeasureDTO unitOfMeasureDTO1 = new UnitOfMeasureDTO();
//        unitOfMeasureDTO1.setFullName("????????????");
//        unitOfMeasureDTO1.setShortName("????");
//        unitOfMeasureService.save(unitOfMeasureDTO1);
//        UnitOfMeasureDTO unitOfMeasureDTO2 = new UnitOfMeasureDTO();
//        unitOfMeasureDTO2.setFullName("??????????");
//        unitOfMeasureDTO2.setShortName("????");
//        unitOfMeasureService.save(unitOfMeasureDTO2);
//    }
//
//    private void createRegion(String name) {
//        Currency currency = new Currency("??????. ??????.", "BYN", false);
//        Country country = new Country();
//        country.setName("????????????????");
//        country.setAlphaCode2("BY");
//        country.setCurrency(currency);
//        countryRepository.save(country);
//        Region region = new Region();
//        region.setName(name);
//        region.setCountry(countryRepository.findByNameIgnoreCase("????????????????").orElse(null));
//        regionRepository.save(region);
//    }
//
//    private void createCookingMethod(String name) {
//        CookingMethod cookingMethod = new CookingMethod(name);
//        cookingMethodRepository.save(cookingMethod);
//    }
//
//    private void createIngredient(String name) {
//        IngredientDTO ingredientDto = new IngredientDTO();
//        ingredientDto.setName(name);
//        ingredientDto.setCalories(new BigDecimal("100"));
//        ingredientDto.setCarbs(new BigDecimal("100"));
//        ingredientDto.setFats(new BigDecimal("100"));
//        ingredientDto.setProteins(new BigDecimal("100"));
//        ingredientDto.setIngredientCategoryName("Milk");
//        Map<String, BigDecimal> map = new HashMap<>();
//        map.put("????????????", new BigDecimal(100));
//        map.put("??????????", new BigDecimal(20));
//        ingredientDto.setUnitOfMeasureEquivalent(map);
//
//        Set<IngredientPriceDTO> ingredientPrices = getIngredientPriceDTOS();
//        ingredientDto.setIngredientPrices(ingredientPrices);
//        ingredientService.save(ingredientDto);
//    }
//
//    private Set<IngredientPriceDTO> getIngredientPriceDTOS() {
//        Set<IngredientPriceDTO> ingredientPrices = new HashSet<>();
//        IngredientPriceDTO ingredientPriceDTO = new IngredientPriceDTO();
//        ingredientPriceDTO.setRegionName("??????????");
//        ingredientPriceDTO.setCurrencyCode("BYN");
//        ingredientPriceDTO.setQuantity(new BigDecimal("1"));
//        ingredientPriceDTO.setUnitOfMeasureName("????????????");
//        ingredientPriceDTO.setPriceValue(new BigDecimal("12"));
//        ingredientPrices.add(ingredientPriceDTO);
//        return ingredientPrices;
//    }
//
//    private RecipeDTO createRecipeDto(String name) {
//        RecipeDTO recipeDto = new RecipeDTO();
//        recipeDto.setName(name);
//        recipeDto.setCookingTime("30");
//        recipeDto.setActiveTime("15");
//        recipeDto.setPortions((short) 2);
//        recipeDto.setImageLink("images/image.png");
//        recipeDto.setSource("http://bestrecipes.com/best-recipe");
//        recipeDto.setCookingMethodName("??????????");
//        recipeDto.setOwnershipName("ADMIN");
//        recipeDto.setCategoryNames(new HashSet<>(Arrays.asList("????????", "????????")));
//        recipeDto.setSubcategoryNames(new HashSet<>(Arrays.asList("????????????", "????????")));
//
//        Set<RecipeIngredientDTO> recipeIngredients = getRecipeIngredients();
//        recipeDto.setRecipeIngredients(recipeIngredients);
//
//        Set<CookingStepDTO> cookingSteps = getCookingSteps();
//        recipeDto.setCookingSteps(cookingSteps);
//        return recipeDto;
//    }
//
//    private Recipe createRecipe(String name) {
//        Recipe recipe = new Recipe();
//        recipe.setName(name);
//        recipe.setCookingTime(new Short("30"));
//        recipe.setActiveTime(new Short("15"));
//        recipe.setPortions((short) 2);
//        recipe.setImageLink("images/image.png");
//        recipe.setSource("http://bestrecipes.com/best-recipe");
//        recipe.setCookingMethod(cookingMethodRepository.findByNameIgnoreCase("??????????").orElse(null));
//        recipe.setOwnership(ownershipRepository.findByName(OwnershipName.ADMIN.name()).orElse(null));
//        recipe.setRecipeCategories(new HashSet<>(recipeCategoryRepository.findAllByIsArchivedIsFalse()));
//        recipe.setRecipeSubcategories(new HashSet<>(recipeSubcategoryRepository.findAllByIsArchivedIsFalse()));
//        return recipeRepository.save(recipe);
//    }
//
//    private Set<CookingStepDTO> getCookingSteps() {
//        Set<CookingStepDTO> cookingSteps = new HashSet<>();
//        CookingStepDTO cookingStepDto = new CookingStepDTO();
//        cookingStepDto.setPriority(1);
//        cookingStepDto.setDescription("???????????????? ??????????");
//        cookingSteps.add(cookingStepDto);
//        return cookingSteps;
//    }
//
//    private Set<RecipeIngredientDTO> getRecipeIngredients() {
//        Set<RecipeIngredientDTO> recipeIngredients = new HashSet<>();
//        RecipeIngredientDTO recipeIngredientDTO = new RecipeIngredientDTO();
//        recipeIngredientDTO.setIngredientName("????????????");
//        recipeIngredientDTO.setUnitOfMeasureShortName("????");
//        recipeIngredientDTO.setQuantity(new BigDecimal("1"));
//        recipeIngredients.add(recipeIngredientDTO);
//        return recipeIngredients;
//    }
//
//    @Test
//    @Transactional
//    public void saveRecipeTest() throws Exception {
//        RecipeDTO recipeDto = createRecipeDto("?????????????????? ????????");
//        ObjectMapper objectMapper = new ObjectMapper();
//        mockMvc.perform(post(UrlConsts.PATH_RECIPES)
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(objectMapper.writeValueAsBytes(recipeDto)))
//                .andDo(print());
//
//        Iterable<Recipe> recipes = recipeRepository.findAll();
//        assertThat(recipes).extracting(Recipe::getName).containsOnly("?????????????????? ????????");
//        assertThat(recipes).extracting(Recipe::getPortions).containsOnly((short) 2);
//        assertThat(recipes).extracting(Recipe::getCookingTime).containsOnly((short) 30);
//        assertThat(recipes).extracting(Recipe::getActiveTime).containsOnly((short) 15);
//        assertThat(recipes).extracting(Recipe::getSource).containsOnly("http://bestrecipes.com/best-recipe");
//        assertThat(recipes).extracting(Recipe::getImageLink).containsOnly("images/image.png");
//        assertThat(recipes).extracting(Recipe::getRecipeCategories)
//                .containsAnyOf(Stream.of(recipeCategoryRepository.findByNameIgnoreCase("????????").get(),
//                        recipeCategoryRepository.findByNameIgnoreCase("????????").get())
//                        .collect(Collectors.toSet()));
//        assertThat(recipes).extracting(Recipe::getRecipeSubcategories)
//                .containsAnyOf(Stream.of(recipeSubcategoryRepository.findByNameIgnoreCase("????????????").get(),
//                        recipeSubcategoryRepository.findByNameIgnoreCase("????????").get())
//                        .collect(Collectors.toSet()));
//        //check calculations
//        assertThat(recipes).extracting(Recipe::getCalories).containsOnly(new BigDecimal("50"));
//        assertThat(recipes).extracting(Recipe::getProteins).containsOnly(new BigDecimal("50.0"));
//        assertThat(recipes).extracting(Recipe::getFats).containsOnly(new BigDecimal("50.0"));
//        assertThat(recipes).extracting(Recipe::getCarbs).containsOnly(new BigDecimal("50.0"));
//        assertThat(recipes).extracting(Recipe::getCookingMethod)
//                .containsOnly(cookingMethodRepository.findByNameIgnoreCase(recipes.iterator().next().getCookingMethod().getName()).get());
//        assertThat(recipes).extracting(Recipe::getOwnership)
//                .containsOnly(ownershipRepository.findByName(recipes.iterator().next().getOwnership().getName()).get());
//        assertThat(recipes).extracting(Recipe::getGramsPerPortion).containsOnly(new BigDecimal("50.0"));
//
//        Set<RecipeIngredient> recipeIngredients = recipeIngredientRepository.findAllById_RecipeId(recipes.iterator().next().getId());
//        assertThat(recipeIngredients).extracting(RecipeIngredient::getIngredient)
//                .containsOnly(ingredientRepository.findByNameIgnoreCase(recipeIngredients.iterator().next().getIngredient().getName()).get());
//        assertThat(recipeIngredients).extracting(RecipeIngredient::getUnitOfMeasure)
//                .containsOnly(unitOfMeasureRepository.findByShortNameIgnoreCase(recipeIngredients.iterator().next().getUnitOfMeasure().getShortName()).get());
//        assertThat(recipeIngredients).extracting(RecipeIngredient::getQty).containsOnly(new BigDecimal("1"));
//
//        Set<CookingStep> cookingSteps = cookingStepRepository.findAllByRecipe_Id(recipes.iterator().next().getId());
//        assertThat(cookingSteps).extracting(CookingStep::getPriority).containsOnly(1);
//        assertThat(cookingSteps).extracting(CookingStep::getDescription).containsOnly("???????????????? ??????????");
//
//        List<RecipePrice> recipePrices = recipePriceRepository.findAllById_RecipeId(recipes.iterator().next().getId());
//        assertThat(recipePrices).extracting(RecipePrice::getRegion)
//                .containsOnly(regionRepository.findByNameIgnoreCase(recipePrices.iterator().next().getRegion().getName()).get());
//        //check calculations
//        assertThat(recipePrices).extracting(RecipePrice::getPriceValue).containsOnly(new BigDecimal("6.00"));
//    }
//
//    @Test
//    public void getAllRecipesTest() throws Exception {
//        recipeService.save(createRecipeDto("?????????????????? ????????"));
//        recipeService.save(createRecipeDto("???????????? ?? ??????????????"));
//
//        mockMvc.perform(get(UrlConsts.PATH_RECIPES).contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$", hasSize(2)))
//                .andExpect(jsonPath("$[0].name", is("?????????????????? ????????")))
//                .andExpect(jsonPath("$[0].cookingTime", is("30")))
//                .andExpect(jsonPath("$[0].activeTime", is("15")))
//                .andExpect(jsonPath("$[0].portions", is(2)))
//                .andExpect(jsonPath("$[0].imageLink", is("images/image.png")))
//                .andExpect(jsonPath("$[0].source", is("http://bestrecipes.com/best-recipe")))
//                .andExpect(jsonPath("$[0].calories", is(50)))
//                .andExpect(jsonPath("$[0].carbs", is(50.0)))
//                .andExpect(jsonPath("$[0].proteins", is(50.0)))
//                .andExpect(jsonPath("$[0].fats", is(50.0)))
//                .andExpect(jsonPath("$[0].cookingMethodName", is("??????????")))
//                .andExpect(jsonPath("$[0].ownershipName", is("ADMIN")))
//                .andExpect(jsonPath("$[0].categoryNames[0]", is("????????")))
//                .andExpect(jsonPath("$[0].categoryNames[1]", is("????????")))
//                .andExpect(jsonPath("$[0].subcategoryNames[0]", is("????????????")))
//                .andExpect(jsonPath("$[0].subcategoryNames[1]", is("????????")))
//
//                .andExpect(jsonPath("$[1].name", is("???????????? ?? ??????????????")))
//                .andExpect(jsonPath("$[1].cookingTime", is("30")))
//                .andExpect(jsonPath("$[1].activeTime", is("15")))
//                .andExpect(jsonPath("$[1].portions", is(2)))
//                .andExpect(jsonPath("$[1].imageLink", is("images/image.png")))
//                .andExpect(jsonPath("$[1].source", is("http://bestrecipes.com/best-recipe")))
//                .andExpect(jsonPath("$[1].calories", is(50)))
//                .andExpect(jsonPath("$[1].carbs", is(50.0)))
//                .andExpect(jsonPath("$[1].proteins", is(50.0)))
//                .andExpect(jsonPath("$[1].fats", is(50.0)))
//                .andExpect(jsonPath("$[1].cookingMethodName", is("??????????")))
//                .andExpect(jsonPath("$[1].ownershipName", is("ADMIN")))
//                .andExpect(jsonPath("$[1].categoryNames[0]", is("????????")))
//                .andExpect(jsonPath("$[1].categoryNames[1]", is("????????")))
//                .andExpect(jsonPath("$[1].subcategoryNames[0]", is("????????????")))
//                .andExpect(jsonPath("$[1].subcategoryNames[1]", is("????????")))
//                .andDo(print());
//    }
//
//    @Test
//    public void updateRecipeTest() throws Exception {
//        RecipeDTO recipeDto = recipeService.save(createRecipeDto("?????????????????? ????????"));
//        recipeDto.setName("???????????? ?? ??????????????");
//        recipeDto.setPortions((short) 4);
//        ObjectMapper objectMapper = new ObjectMapper();
//
//        mockMvc.perform(put(UrlConsts.PATH_RECIPES + "/" + recipeDto.getId().toString())
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(objectMapper.writeValueAsBytes(recipeDto)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.name", is("???????????? ?? ??????????????")))
//                .andExpect(jsonPath("$.cookingTime", is("30")))
//                .andExpect(jsonPath("$.activeTime", is("15")))
//                .andExpect(jsonPath("$.portions", is(4)))
//                .andExpect(jsonPath("$.imageLink", is("images/image.png")))
//                .andExpect(jsonPath("$.source", is("http://bestrecipes.com/best-recipe")))
//                .andExpect(jsonPath("$.calories", is(25)))
//                .andExpect(jsonPath("$.carbs", is(25.0)))
//                .andExpect(jsonPath("$.proteins", is(25.0)))
//                .andExpect(jsonPath("$.fats", is(25.0)))
//                .andExpect(jsonPath("$.cookingMethodName", is("??????????")))
//                .andExpect(jsonPath("$.ownershipName", is("ADMIN")))
//                .andExpect(jsonPath("$.categoryNames[0]", is("????????")))
//                .andExpect(jsonPath("$.categoryNames[1]", is("????????")))
//                .andExpect(jsonPath("$.subcategoryNames[0]", is("????????????")))
//                .andExpect(jsonPath("$.subcategoryNames[1]", is("????????")))
//                .andDo(print());
//    }
//
//    @Test
//    public void deleteRecipeTest() throws Exception {
//        RecipeDTO recipeDto = recipeService.save(createRecipeDto("?????????????????? ????????"));
//        mockMvc.perform(delete(UrlConsts.PATH_RECIPES + "/" + recipeDto.getId().toString())
//                .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isNoContent());
//
//        Iterable<RecycleBin> recycleBins = recycleBinRepository.findAll();
//        assertThat(recycleBins).extracting(RecycleBin::getElementName).containsOnly("?????????????????? ????????");
//        assertThat(recycleBins).extracting(RecycleBin::getEntityName).containsOnly("????????????");
//        assertThat(recycleBins).extracting(RecycleBin::getDeleteDate).isNotNull();
//
//        Optional<Recipe> recipe = recipeRepository.findById(recipeDto.getId());
//        assertThat(recipe.get().isArchived()).isTrue();
//    }
//
//    @Test
//    @Transactional
//    public void checkConnectedElementsTest() throws Exception {
//        Recipe recipe = createRecipe("?????????????????? ????????");
//        MealType mealType;
//        if (mealTypeRepository.findByNameIgnoreCase("??????????????").isPresent()) {
//            mealType = mealTypeRepository.findByNameIgnoreCase("??????????????").get();
//        } else {
//            mealType = mealTypeRepository.save(new MealType("??????????????", 50));
//        }
//
//        Menu menu = menuRepository.save(new Menu("??????????????????", true,
//                ownershipRepository.findByName(OwnershipName.ADMIN.name()).orElse(null)));
//        MenuRecipe menuRecipe = new MenuRecipe(menu, recipe, mealType, DayOfWeek.MONDAY);
//        menuRecipeRepository.save(menuRecipe);
//        mockMvc.perform(get(UrlConsts.PATH_RECIPES + "/checkConnectedElements/" + recipe.getId().toString())
//                .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$", hasSize(1)));
//    }
//
//    @Test
//    public void checkUniqueNameTest() throws Exception {
//        RecipeDTO recipeDto = recipeService.save(createRecipeDto("?????????????????? ????????"));
//        mockMvc.perform(get(UrlConsts.PATH_RECIPES + "/checkUniqueName?name=" + recipeDto.getName())
//                .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(content().string(String.valueOf(-1)));
//        mockMvc.perform(get(UrlConsts.PATH_RECIPES + "/checkUniqueName?name=" + "????????????")
//                .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(content().string(String.valueOf(0)));
//    }
//}
