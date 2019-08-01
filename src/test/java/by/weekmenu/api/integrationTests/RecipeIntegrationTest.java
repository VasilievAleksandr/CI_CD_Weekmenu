package by.weekmenu.api.integrationTests;

import by.weekmenu.api.ApiApplication;
import by.weekmenu.api.dto.*;
import by.weekmenu.api.entity.*;
import by.weekmenu.api.entity.Currency;
import by.weekmenu.api.repository.*;
import by.weekmenu.api.service.IngredientService;
import by.weekmenu.api.service.RecipeService;
import by.weekmenu.api.service.UnitOfMeasureService;
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
import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ApiApplication.class)
@AutoConfigureMockMvc
public class RecipeIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private OwnershipRepository ownershipRepository;

    @Autowired
    private UnitOfMeasureRepository unitOfMeasureRepository;

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
    private IngredientService ingredientService;

    @Autowired
    private RecipeService recipeService;

    @Before
    public void createBaseData() {
        if (ownershipRepository.findAll().spliterator().getExactSizeIfKnown()==0) {
            ownershipRepository.save(new Ownership(OwnershipName.ADMIN));
            ownershipRepository.save(new Ownership(OwnershipName.USER));
        }
        if (unitOfMeasureRepository.findAll().spliterator().getExactSizeIfKnown()==1) {
            createUnitOfMeasureDtos();
        }
        createRegion("Минск");
        createCookingMethod("Варка");
        createIngredient("Гречка");
    }

    @After
    public void cleanDB() {
        ingredientPriceRepository.deleteAll();
        recipePriceRepository.deleteAll();
        regionRepository.deleteAll();
        countryRepository.deleteAll();
        currencyRepository.deleteAll();
        ingredientUnitOfMeasureRepository.deleteAll();
        recipeIngredientRepository.deleteAll();
        ingredientRepository.deleteAll();
        cookingStepRepository.deleteAll();
        recipeRepository.deleteAll();
        cookingMethodRepository.deleteAll();
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
        Currency currency = new Currency("Бел. руб.", "BYN", true);
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
        IngredientDto ingredientDto = new IngredientDto();
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

    private RecipeDTO createRecipeDto(String name) {
        RecipeDTO recipeDto = new RecipeDTO();
        recipeDto.setName(name);
        recipeDto.setCookingTime("30");
        recipeDto.setPreparingTime("15");
        recipeDto.setPortions((short)2);
        recipeDto.setImageLink("images/image.png");
        recipeDto.setSource("http://bestrecipes.com/best-recipe");
        recipeDto.setCookingMethodName("Варка");
        recipeDto.setOwnershipName("ADMIN");

        Set<RecipeIngredientDTO> recipeIngredients = getRecipeIngredients();
        recipeDto.setRecipeIngredients(recipeIngredients);

        Set<CookingStepDTO> cookingSteps = getCookingSteps();
        recipeDto.setCookingSteps(cookingSteps);
        return recipeDto;
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

    @Test
    @Transactional
    public void saveRecipeTest() throws Exception {
        RecipeDTO recipeDto = createRecipeDto("Гречневая каша");
        ObjectMapper objectMapper = new ObjectMapper();
        mockMvc.perform(post("/recipes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(recipeDto)))
                .andDo(print());

        Iterable<Recipe> recipes = recipeRepository.findAll();
        assertThat(recipes).extracting(Recipe::getName).containsOnly("Гречневая каша");
        assertThat(recipes).extracting(Recipe::getPortions).containsOnly((short)2);
        assertThat(recipes).extracting(Recipe::getCookingTime).containsOnly((short)30);
        assertThat(recipes).extracting(Recipe::getPreparingTime).containsOnly((short)15);
        assertThat(recipes).extracting(Recipe::getSource).containsOnly("http://bestrecipes.com/best-recipe");
        assertThat(recipes).extracting(Recipe::getImageLink).containsOnly("images/image.png");
        //check calculations
        assertThat(recipes).extracting(Recipe::getCalories).containsOnly(new BigDecimal("50.0"));
        assertThat(recipes).extracting(Recipe::getProteins).containsOnly(new BigDecimal("50.0"));
        assertThat(recipes).extracting(Recipe::getFats).containsOnly(new BigDecimal("50.0"));
        assertThat(recipes).extracting(Recipe::getCarbs).containsOnly(new BigDecimal("50.0"));
        assertThat(recipes).extracting(Recipe::getCookingMethod)
                .containsOnly(cookingMethodRepository.findByNameIgnoreCase(recipes.iterator().next().getCookingMethod().getName()).get());
        assertThat(recipes).extracting(Recipe::getOwnership)
                .containsOnly(ownershipRepository.findByName(recipes.iterator().next().getOwnership().getName()).get());

        Set<RecipeIngredient> recipeIngredients = recipeIngredientRepository.findAllById_RecipeId(recipes.iterator().next().getId());
        assertThat(recipeIngredients).extracting(RecipeIngredient::getIngredient)
                .containsOnly(ingredientRepository.findByNameIgnoreCase(recipeIngredients.iterator().next().getIngredient().getName()).get());
        assertThat(recipeIngredients).extracting(RecipeIngredient::getUnitOfMeasure)
                .containsOnly(unitOfMeasureRepository.findByShortNameIgnoreCase(recipeIngredients.iterator().next().getUnitOfMeasure().getShortName()).get());
        assertThat(recipeIngredients).extracting(RecipeIngredient::getQty).containsOnly(new BigDecimal("1"));

        Set<CookingStep> cookingSteps = cookingStepRepository.findAllByRecipe_Id(recipes.iterator().next().getId());
        assertThat(cookingSteps).extracting(CookingStep::getPriority).containsOnly(1);
        assertThat(cookingSteps).extracting(CookingStep::getDescription).containsOnly("Нарежьте овощи");

        List<RecipePrice> recipePrices = recipePriceRepository.findAllById_RecipeId(recipes.iterator().next().getId());
        assertThat(recipePrices).extracting(RecipePrice::getRegion)
                .containsOnly(regionRepository.findByNameIgnoreCase(recipePrices.iterator().next().getRegion().getName()).get());
        //check calculations
        assertThat(recipePrices).extracting(RecipePrice::getPriceValue).containsOnly(new BigDecimal("6.00"));
    }

    @Test
    public void getAllRecipesTest() throws Exception {
        recipeService.save(createRecipeDto("Гречневая каша"));
        recipeService.save(createRecipeDto("Гречка с овощами"));

        mockMvc.perform(get("/recipes").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", is("Гречневая каша")))
                .andExpect(jsonPath("$[0].cookingTime", is("30")))
                .andExpect(jsonPath("$[0].preparingTime", is("15")))
                .andExpect(jsonPath("$[0].portions", is(2)))
                .andExpect(jsonPath("$[0].imageLink", is("images/image.png")))
                .andExpect(jsonPath("$[0].source", is("http://bestrecipes.com/best-recipe")))
                .andExpect(jsonPath("$[0].calories", is(50.0)))
                .andExpect(jsonPath("$[0].carbs", is(50.0)))
                .andExpect(jsonPath("$[0].proteins", is(50.0)))
                .andExpect(jsonPath("$[0].fats", is(50.0)))
                .andExpect(jsonPath("$[0].cookingMethodName", is("Варка")))
                .andExpect(jsonPath("$[0].ownershipName", is("ADMIN")))
                .andExpect(jsonPath("$[1].name", is("Гречка с овощами")))
                .andExpect(jsonPath("$[1].cookingTime", is("30")))
                .andExpect(jsonPath("$[1].preparingTime", is("15")))
                .andExpect(jsonPath("$[1].portions", is(2)))
                .andExpect(jsonPath("$[1].imageLink", is("images/image.png")))
                .andExpect(jsonPath("$[1].source", is("http://bestrecipes.com/best-recipe")))
                .andExpect(jsonPath("$[1].calories", is(50.0)))
                .andExpect(jsonPath("$[1].carbs", is(50.0)))
                .andExpect(jsonPath("$[1].proteins", is(50.0)))
                .andExpect(jsonPath("$[1].fats", is(50.0)))
                .andExpect(jsonPath("$[1].cookingMethodName", is("Варка")))
                .andExpect(jsonPath("$[1].ownershipName", is("ADMIN")))
                .andDo(print());
    }

    @Test
    public void updateRecipeTest() throws Exception {
        RecipeDTO recipeDto = recipeService.save(createRecipeDto("Гречневая каша"));
        recipeDto.setName("Гречка с овощами");
        recipeDto.setPortions((short)4);
        ObjectMapper objectMapper = new ObjectMapper();

        mockMvc.perform(put("/recipes/" + recipeDto.getId().toString())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(recipeDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Гречка с овощами")))
                .andExpect(jsonPath("$.cookingTime", is("30")))
                .andExpect(jsonPath("$.preparingTime", is("15")))
                .andExpect(jsonPath("$.portions", is(4)))
                .andExpect(jsonPath("$.imageLink", is("images/image.png")))
                .andExpect(jsonPath("$.source", is("http://bestrecipes.com/best-recipe")))
                .andExpect(jsonPath("$.calories", is(25.0)))
                .andExpect(jsonPath("$.carbs", is(25.0)))
                .andExpect(jsonPath("$.proteins", is(25.0)))
                .andExpect(jsonPath("$.fats", is(25.0)))
                .andExpect(jsonPath("$.cookingMethodName", is("Варка")))
                .andExpect(jsonPath("$.ownershipName", is("ADMIN")))
                .andDo(print());
    }

    @Test
    public void deleteRecipeTest() throws Exception{
        RecipeDTO recipeDto = recipeService.save(createRecipeDto("Гречневая каша"));
        mockMvc.perform(delete("/recipes/" + recipeDto.getId().toString())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    public void checkUniqueNameTest() throws Exception {
        RecipeDTO recipeDto = recipeService.save(createRecipeDto("Гречневая каша"));
        mockMvc.perform(get("/recipes/checkUniqueName?name=" + recipeDto.getName())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(String.valueOf(-1)));
        mockMvc.perform(get("/recipes/checkUniqueName?name=" + "Шашлык")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(String.valueOf(0)));
    }
}
