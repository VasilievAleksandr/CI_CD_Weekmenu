//package by.weekmenu.api.integrationTests;
//
//import by.weekmenu.api.ApiApplication;
//import by.weekmenu.api.dto.RegionDTO;
//import by.weekmenu.api.entity.*;
//import by.weekmenu.api.repository.*;
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
//import java.util.Objects;
//import java.util.Optional;
//
//import static org.assertj.core.api.Assertions.*;
//import static org.hamcrest.Matchers.*;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@RunWith(SpringRunner.class)
//@SpringBootTest(classes = ApiApplication.class)
//@AutoConfigureMockMvc
//public class RegionIntegrationTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Autowired
//    private RegionRepository regionRepository;
//
//    @Autowired
//    private CountryRepository countryRepository;
//
//    @Autowired
//    private CurrencyRepository currencyRepository;
//
//    @Autowired
//    private IngredientPriceRepository ingredientPriceRepository;
//
//    @Autowired
//    private RecipePriceRepository recipePriceRepository;
//
//    @Autowired
//    private IngredientRepository ingredientRepository;
//
//    @Autowired
//    private UnitOfMeasureRepository unitOfMeasureRepository;
//
//    @Autowired
//    private OwnershipRepository ownershipRepository;
//
//    @Autowired
//    private RecipeRepository recipeRepository;
//
//    @Autowired
//    private RecycleBinRepository recycleBinRepository;
//
//    @Autowired
//    private CookingMethodRepository cookingMethodRepository;
//
//    @Autowired
//    IngredientCategoryRepository ingredientCategoryRepository;
//
//    @Before
//    public void createCountry() {
//        Currency currency = new Currency("??????. ??????.", "BYN", false);
//        Country country = new Country();
//        country.setName("????????????????");
//        country.setAlphaCode2("BY");
//        country.setCurrency(currency);
//        countryRepository.save(country);
//    }
//
//    @After
//    public void cleanDB() {
//        regionRepository.deleteAll();
//        countryRepository.deleteAll();
//        currencyRepository.deleteAll();
//        recycleBinRepository.deleteAll();
//    }
//
//    private Region createRegion(String name) {
//        Region region = new Region();
//        region.setName(name);
//        region.setCountry(countryRepository.findByNameIgnoreCase("????????????????").orElse(null));
//        return regionRepository.save(region);
//    }
//
//    @Test
//    @Transactional
//    public void saveRegion() throws Exception {
//        RegionDTO regionDto = new RegionDTO();
//        regionDto.setName("????????????");
//        regionDto.setCountryName("????????????????");
//
//        ObjectMapper objectMapper = new ObjectMapper();
//        mockMvc.perform(post(UrlConsts.PATH_REGIONS)
//        .contentType(MediaType.APPLICATION_JSON)
//        .content(objectMapper.writeValueAsBytes(regionDto)));
//
//        Iterable<Region> regions = regionRepository.findAll();
//        assertThat(regions).extracting(Region::getName).containsOnly("????????????");
//        assertThat(regions).extracting(region -> region.getCountry().getName()).containsOnly("????????????????");
//        assertThat(regions).extracting(Region::isArchived).contains(false);
//    }
//
//    @Test
//    public void getAllRegions() throws Exception {
//        createRegion("??????????");
//        createRegion("????????????");
//
//        mockMvc.perform(get(UrlConsts.PATH_REGIONS).contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$", hasSize(2)))
//                .andExpect(jsonPath("$[0].name", is("??????????")))
//                .andExpect(jsonPath("$[0].countryName", is("????????????????")))
//                .andExpect(jsonPath("$[1].name", is("????????????")))
//                .andExpect(jsonPath("$[1].countryName", is("????????????????")));
//    }
//
//    @Test
//    public void updateRegion() throws Exception {
//        Region region = createRegion("??????????");
//        RegionDTO regionDto = new RegionDTO();
//        regionDto.setName("????????????");
//        regionDto.setCountryName("????????????????");
//        ObjectMapper objectMapper = new ObjectMapper();
//
//        mockMvc.perform(put(UrlConsts.PATH_REGIONS + "/"  + region.getId().toString())
//        .contentType(MediaType.APPLICATION_JSON)
//                .content(objectMapper.writeValueAsBytes(regionDto)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.name", is("????????????")))
//                .andExpect(jsonPath("$.countryName", is("????????????????")));
//    }
//
//    @Test
//    public void findRegionByIdTest() throws Exception {
//        Region region = createRegion("??????????");
//
//        mockMvc.perform(get(UrlConsts.PATH_REGIONS + "/"  + region.getId().toString())
//                .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id", is(region.getId().intValue())))
//                .andExpect(jsonPath("$.name", is("??????????")))
//                .andExpect(jsonPath("$.countryName", is("????????????????")));
//    }
//
//    @Test
//    public void deleteRegionTest() throws Exception {
//        Region region = createRegion("??????????");
//        mockMvc.perform(delete(UrlConsts.PATH_REGIONS + "/"  + region.getId().toString())
//                .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isNoContent());
//
//        Iterable<RecycleBin> recycleBins = recycleBinRepository.findAll();
//        assertThat(recycleBins).extracting(RecycleBin::getElementName).containsOnly("??????????");
//        assertThat(recycleBins).extracting(RecycleBin::getEntityName).containsOnly("????????????");
//        assertThat(recycleBins).extracting(RecycleBin::getDeleteDate).isNotNull();
//
//        Optional<Region> regionAfterDelete = regionRepository.findById(region.getId());
//        assertThat(regionAfterDelete.get().isArchived()).isTrue();
//    }
//
//    @Test
//    public void checkUniqueNameTest() throws Exception {
//        Region region = createRegion("??????????");
//        mockMvc.perform(get(UrlConsts.PATH_REGIONS + "/checkUniqueName?name=" + region.getName())
//                .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(content().string(String.valueOf(-1)));
//    }
//
//    @Test
//    @Transactional
//    public void checkConnectedElementsTest() throws Exception {
//        Region region = createRegion("??????????");
//        UnitOfMeasure unitOfMeasure = unitOfMeasureRepository.save(new UnitOfMeasure("????", "??????????????????"));
//        Ownership ownership = ownershipRepository.findByName("USER").orElse(null);
//        Ingredient ingredient = new Ingredient("????????????", ownership);
//        IngredientCategory ingredientCategory = new IngredientCategory("Milk", false);
//        ingredientCategoryRepository.save(ingredientCategory);
//        ingredient.setIngredientCategory(ingredientCategory);
//        ingredientRepository.save(ingredient);
//        IngredientPrice ingredientPrice = new IngredientPrice();
//        ingredientPrice.setId(new IngredientPrice.Id(ingredient.getId(), Objects.requireNonNull(region).getId()));
//        ingredientPrice.setUnitOfMeasure(unitOfMeasure);
//        ingredientPrice.setIngredient(ingredient);
//        ingredientPrice.setRegion(region);
//        ingredientPrice.setQuantity(new BigDecimal("1"));
//        ingredientPrice.setPriceValue(new BigDecimal("123.12"));
//        ingredientPriceRepository.save(ingredientPrice);
//        CookingMethod cookingMethod = cookingMethodRepository.save(new CookingMethod("??????????"));
//        Recipe recipe = recipeRepository.save(new Recipe("????????????", true, cookingMethod, ownership));
//        RecipePrice recipePrice = new RecipePrice();
//        recipePrice.setId(new RecipePrice.Id(recipe.getId(), region.getId()));
//        recipePrice.setRegion(region);
//        recipePrice.setRecipe(recipe);
//        recipePrice.setPriceValue(new BigDecimal("111"));
//        recipePriceRepository.save(recipePrice);
//        mockMvc.perform(get(UrlConsts.PATH_REGIONS + "/checkConnectedElements/" + region.getId().toString())
//                .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$", hasSize(2)));
//    }
//}
