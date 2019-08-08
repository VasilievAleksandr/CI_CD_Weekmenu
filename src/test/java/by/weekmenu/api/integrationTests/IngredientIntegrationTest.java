package by.weekmenu.api.integrationTests;

import by.weekmenu.api.ApiApplication;
import by.weekmenu.api.dto.IngredientDto;
import by.weekmenu.api.dto.IngredientPriceDTO;
import by.weekmenu.api.dto.UnitOfMeasureDTO;
import by.weekmenu.api.entity.*;
import by.weekmenu.api.entity.Currency;
import by.weekmenu.api.repository.*;
import by.weekmenu.api.service.IngredientService;
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
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ApiApplication.class)
@AutoConfigureMockMvc
public class IngredientIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private IngredientRepository ingredientRepository;

    @Autowired
    private OwnershipRepository ownershipRepository;

    @Autowired
    private RegionRepository regionRepository;

    @Autowired
    private CountryRepository countryRepository;

    @Autowired
    private IngredientPriceRepository ingredientPriceRepository;

    @Autowired
    private IngredientUnitOfMeasureRepository ingredientUnitOfMeasureRepository;

    @Autowired
    private UnitOfMeasureRepository unitOfMeasureRepository;

    @Autowired
    private CurrencyRepository currencyRepository;

    @Autowired
    private IngredientService ingredientService;

    @Autowired
    private UnitOfMeasureService unitOfMeasureService;

    @Autowired
    private RecycleBinRepository recycleBinRepository;

    @Autowired
    private CookingMethodRepository cookingMethodRepository;

    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private RecipeIngredientRepository recipeIngredientRepository;

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
    }

    @After
    public void cleanDB() {
        ingredientPriceRepository.deleteAll();
        regionRepository.deleteAll();
        countryRepository.deleteAll();
        currencyRepository.deleteAll();
        ingredientUnitOfMeasureRepository.deleteAll();
        ingredientRepository.deleteAll();
        recycleBinRepository.deleteAll();
    }

    private IngredientDto createIngredientDto(String name) {
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
        return ingredientService.save(ingredientDto);
    }

    private Set<IngredientPriceDTO> getIngredientPriceDTOS() {
        Set<IngredientPriceDTO> ingredientPrices = new HashSet<>();
        IngredientPriceDTO ingredientPriceDTO = new IngredientPriceDTO();
        ingredientPriceDTO.setRegionName("Минск");
        ingredientPriceDTO.setCurrencyCode("BYN");
        ingredientPriceDTO.setQuantity(new BigDecimal("1"));
        ingredientPriceDTO.setUnitOfMeasureName("Ложка");
        ingredientPriceDTO.setPriceValue(new BigDecimal("12.1"));
        ingredientPrices.add(ingredientPriceDTO);
        return ingredientPrices;
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

    private Ingredient createIngredient(String name) {
        Ingredient ingredient = new Ingredient();
        ingredient.setName(name);
        ingredient.setCalories(new BigDecimal("100"));
        ingredient.setCarbs(new BigDecimal("100"));
        ingredient.setFats(new BigDecimal("100"));
        ingredient.setProteins(new BigDecimal("100"));
        ingredient.setOwnership(ownershipRepository.findByName(OwnershipName.ADMIN.name()).orElse(null));
        return ingredientRepository.save(ingredient);
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

    @Test
    @Transactional
    public void saveIngredient() throws Exception {
        IngredientDto ingredientDto = new IngredientDto();
        ingredientDto.setName("Курица");
        ingredientDto.setCalories(new BigDecimal("100"));
        ingredientDto.setCarbs(new BigDecimal("100"));
        ingredientDto.setFats(new BigDecimal("100"));
        ingredientDto.setProteins(new BigDecimal("100"));
        Map<String, BigDecimal> map = new HashMap<>();
        map.put("Стакан", new BigDecimal(100));
        map.put("Ложка", new BigDecimal(20));
        ingredientDto.setUnitOfMeasureEquivalent(map);
        ingredientDto.setIngredientPrices(getIngredientPriceDTOS());

        ObjectMapper objectMapper = new ObjectMapper();
        mockMvc.perform(post("/ingredients")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(ingredientDto)))
        .andDo(print());

        Iterable<Ingredient> ingredients = ingredientRepository.findAll();
        assertThat(ingredients).extracting(Ingredient::getName).containsOnly("Курица");
        assertThat(ingredients).extracting(Ingredient::getCalories).containsOnly(new BigDecimal("100"));
        assertThat(ingredients).extracting(Ingredient::getCarbs).containsOnly(new BigDecimal("100"));
        assertThat(ingredients).extracting(Ingredient::getFats).containsOnly(new BigDecimal("100"));
        assertThat(ingredients).extracting(Ingredient::getProteins).containsOnly(new BigDecimal("100"));

        List<IngredientUnitOfMeasure> allByIngredient = ingredientUnitOfMeasureRepository.findAllById_IngredientId(ingredients.iterator().next().getId());
        Ingredient fromDB = ingredientRepository.findById(allByIngredient.iterator().next().getId().getIngredientId()).orElse(null);
        assertThat(fromDB).isEqualTo(ingredients.iterator().next());
        List<Long> uomIds = allByIngredient.stream().map(ingredientUnitOfMeasure -> ingredientUnitOfMeasure.getId().getUnitOfMeasureId())
                .collect(Collectors.toList());
        Iterable<UnitOfMeasure> uomsFromDB = unitOfMeasureRepository.findAllById(uomIds);
        assertThat(uomsFromDB).extracting(UnitOfMeasure::getFullName).contains("Стакан", "Ложка");
        assertThat(allByIngredient).extracting(IngredientUnitOfMeasure::getEquivalent).containsAnyOf(new BigDecimal("20"), new BigDecimal("100"));
        Set<IngredientPrice> ingredientPrices = ingredientPriceRepository.findAllById_IngredientId(ingredients.iterator().next().getId());
        assertThat(ingredientPrices).extracting(IngredientPrice::getPriceValue).contains(new BigDecimal("12.1"));
        assertThat(ingredientPrices).extracting(IngredientPrice::getQuantity).contains(new BigDecimal("1"));
    }

    @Test
    public void getAllIngredients() throws Exception {
        createIngredientDto("Курица");
        createIngredientDto("Ананас");

        mockMvc.perform(get("/ingredients").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", is("Курица")))
                .andExpect(jsonPath("$[0].calories", is(100.0)))
                .andExpect(jsonPath("$[0].carbs", is(100.0)))
                .andExpect(jsonPath("$[0].fats", is(100.0)))
                .andExpect(jsonPath("$[0].proteins", is(100.0)))
                .andExpect(jsonPath("$[1].name", is("Ананас")))
                .andExpect(jsonPath("$[1].calories", is(100.0)))
                .andExpect(jsonPath("$[1].carbs", is(100.0)))
                .andExpect(jsonPath("$[1].fats", is(100.0)))
                .andExpect(jsonPath("$[1].proteins", is(100.0)))
                .andDo(print());
    }

    @Test
    public void findIngredientByIdTest() throws Exception {
        IngredientDto ingredientDto = createIngredientDto("Курица");

        mockMvc.perform(get("/ingredients/" + ingredientDto.getId().toString())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(ingredientDto.getId().intValue())))
                .andExpect(jsonPath("$.name", is("Курица")))
                .andExpect(jsonPath("$.calories", is(100.0)))
                .andExpect(jsonPath("$.carbs", is(100.0)))
                .andExpect(jsonPath("$.fats", is(100.0)))
                .andExpect(jsonPath("$.proteins", is(100.0)));
    }

    @Test
    public void deleteIngredientTest() throws Exception {
        IngredientDto ingredientDto = createIngredientDto("Курица");
        mockMvc.perform(delete("/ingredients/" + ingredientDto.getId().toString())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        Iterable<RecycleBin> recycleBins = recycleBinRepository.findAll();
        assertThat(recycleBins).extracting(RecycleBin::getElementName).containsOnly("Курица");
        assertThat(recycleBins).extracting(RecycleBin::getEntityName).containsOnly("Ингредиент");
        assertThat(recycleBins).extracting(RecycleBin::getDeleteDate).isNotNull();

        Optional<Ingredient> ingredient = ingredientRepository.findById(ingredientDto.getId());
        assertThat(ingredient.get().isArchived()).isEqualTo(true);
    }

    @Test
    public void updateIngredient() throws Exception {
        Ingredient ingredient = createIngredient("Курица");
        IngredientDto ingredientDto = new IngredientDto();
        ingredientDto.setName("Ананас");
        ingredientDto.setCalories(new BigDecimal("100"));
        ingredientDto.setCarbs(new BigDecimal("100"));
        ingredientDto.setFats(new BigDecimal("50"));
        ingredientDto.setProteins(new BigDecimal("100"));
        UnitOfMeasureDTO unitOfMeasureDto = new UnitOfMeasureDTO();
        unitOfMeasureDto.setShortName("Гр");
        unitOfMeasureDto.setFullName("Грамм");
        ObjectMapper objectMapper = new ObjectMapper();

        mockMvc.perform(put("/ingredients/" + ingredient.getId().toString())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(ingredientDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Ананас")))
                .andExpect(jsonPath("$.calories", is(100)))
                .andExpect(jsonPath("$.carbs", is(100)))
                .andExpect(jsonPath("$.fats", is(50)))
                .andExpect(jsonPath("$.proteins", is(100)));
    }

    @Test
    public void checkUniqueNameTest() throws Exception {
        Ingredient ingredient = createIngredient("Курица");
        mockMvc.perform(get("/ingredients/checkUniqueName?name=" + ingredient.getName())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(String.valueOf(-1)));
    }

    @Test
    @Transactional
    public void checkConnectedElementsTest() throws Exception {
        Ingredient ingredient = createIngredient("Курица");
        CookingMethod cookingMethod = cookingMethodRepository.save(new CookingMethod("жарка"));
        Ownership ownership = ownershipRepository.findByName("USER").orElse(null);
        UnitOfMeasure unitOfMeasure = unitOfMeasureRepository.save(new UnitOfMeasure("Кг", "Килограмм"));
        Recipe recipe = recipeRepository.save(new Recipe("рецепт", true, cookingMethod, ownership));
        RecipeIngredient recipeIngredient = new RecipeIngredient(new BigDecimal("111"),
                ingredient, recipe);
        recipeIngredient.setId(new RecipeIngredient.Id(ingredient.getId(), recipe.getId()));
        recipeIngredient.setUnitOfMeasure(unitOfMeasure);
        recipeIngredientRepository.save(recipeIngredient);
        mockMvc.perform(get("/ingredients/checkConnectedElements/" + ingredient.getId().toString())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }
}
