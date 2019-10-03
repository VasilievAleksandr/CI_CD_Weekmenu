package by.weekmenu.api.service;

import by.weekmenu.api.dto.RecycleBinDTO;
import by.weekmenu.api.entity.*;
import by.weekmenu.api.repository.*;
import by.weekmenu.api.utils.EntityNamesConsts;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
public class RecycleBinServiceImplTest {

    @MockBean
    private RecycleBinRepository recycleBinRepository;
    @MockBean
    private UnitOfMeasureRepository unitOfMeasureRepository;
    @MockBean
    private CurrencyRepository currencyRepository;
    @MockBean
    private CountryRepository countryRepository;
    @MockBean
    private RegionRepository regionRepository;
    @MockBean
    private IngredientRepository ingredientRepository;
    @MockBean
    private IngredientService ingredientService;
    @MockBean
    private RecipeRepository recipeRepository;
    @MockBean
    private RecipeService recipeService;
    @MockBean
    private RecipeCategoryRepository recipeCategoryRepository;
    @MockBean
    private CookingMethodRepository cookingMethodRepository;
    @MockBean
    private RecipeSubcategoryRepository recipeSubcategoryRepository;
    @MockBean
    private MenuCategoryRepository menuCategoryRepository;
    @MockBean
    private MealTypeRepository mealTypeRepository;
    @MockBean
    private ModelMapper modelMapper;

    private RecycleBinService recycleBinService;

    @Before
    public void setup() {
        recycleBinService = new RecycleBinServiceImpl(recycleBinRepository, unitOfMeasureRepository, currencyRepository,
                countryRepository, regionRepository,ingredientRepository, ingredientService, recipeRepository, recipeService,
                recipeCategoryRepository, cookingMethodRepository, recipeSubcategoryRepository,menuCategoryRepository,
                mealTypeRepository, modelMapper);
    }

    private RecycleBin createRecycleBin(String elementName, String entityName, LocalDateTime deleteDate) {
        RecycleBin recycleBin = new RecycleBin();
        recycleBin.setElementName(elementName);
        recycleBin.setEntityName(entityName);
        recycleBin.setDeleteDate(deleteDate);
        return recycleBin;
    }

    @Test
    public void restoreElement_UnitOfMeasure() {
        RecycleBin recycleBin = createRecycleBin("Литр", EntityNamesConsts.UNIT_OF_MEASURE, LocalDateTime.now());
        when(recycleBinRepository.findById(anyLong())).thenReturn(Optional.of(recycleBin));
        UnitOfMeasure unitOfMeasure = mock(UnitOfMeasure.class);
        when(unitOfMeasureRepository.findByFullNameIgnoreCase(anyString())).thenReturn(Optional.of(unitOfMeasure));
        recycleBinService.restoreElement(1L);
        verify(unitOfMeasureRepository, times(1)).restore(0L);
    }

    @Test
    public void restoreElement_Currency() {
        RecycleBin recycleBin = createRecycleBin("Бел.руб.", EntityNamesConsts.CURRENCY, LocalDateTime.now());
        when(recycleBinRepository.findById(anyLong())).thenReturn(Optional.of(recycleBin));
        Currency currency = mock(Currency.class);
        when(currencyRepository.findByNameIgnoreCase(anyString())).thenReturn(Optional.of(currency));
        recycleBinService.restoreElement(1L);
        verify(currencyRepository, times(1)).restore(0);
    }

    @Test
    public void restoreElement_Country() {
        RecycleBin recycleBin = createRecycleBin("Беларусь", EntityNamesConsts.COUNTRY, LocalDateTime.now());
        when(recycleBinRepository.findById(anyLong())).thenReturn(Optional.of(recycleBin));
        Country country = mock(Country.class);
        when(countryRepository.findByNameIgnoreCase(anyString())).thenReturn(Optional.of(country));
        recycleBinService.restoreElement(1L);
        verify(countryRepository, times(1)).restore(0L);
    }

    @Test
    public void restoreElement_Region() {
        RecycleBin recycleBin = createRecycleBin("Минск",EntityNamesConsts.REGION, LocalDateTime.now());
        when(recycleBinRepository.findById(anyLong())).thenReturn(Optional.of(recycleBin));
        Region region = mock(Region.class);
        when(regionRepository.findByNameIgnoreCase(anyString())).thenReturn(Optional.of(region));
        recycleBinService.restoreElement(1L);
        verify(regionRepository, times(1)).restore(0L);
    }

    @Test
    public void restoreElement_Ingredient() {
        RecycleBin recycleBin = createRecycleBin("Гречка", EntityNamesConsts.INGREDIENT, LocalDateTime.now());
        when(recycleBinRepository.findById(anyLong())).thenReturn(Optional.of(recycleBin));
        Ingredient ingredient = mock(Ingredient.class);
        when(ingredientRepository.findByNameIgnoreCase(anyString())).thenReturn(Optional.of(ingredient));
        recycleBinService.restoreElement(1L);
        verify(ingredientRepository, times(1)).restore(0L);
    }

    @Test
    public void restoreElement_Recipe() {
        RecycleBin recycleBin = createRecycleBin("Гречневая каша", EntityNamesConsts.RECIPE, LocalDateTime.now());
        when(recycleBinRepository.findById(anyLong())).thenReturn(Optional.of(recycleBin));
        Recipe recipe = mock(Recipe.class);
        when(recipeRepository.findByNameIgnoreCase(anyString())).thenReturn(Optional.of(recipe));
        recycleBinService.restoreElement(1L);
        verify(recipeRepository, times(1)).restore(0L);
    }

    @Test
    public void restoreElement_RecipeCategory() {
        RecycleBin recycleBin = createRecycleBin("Гречневая каша", EntityNamesConsts.RECIPE_CATEGORY, LocalDateTime.now());
        when(recycleBinRepository.findById(anyLong())).thenReturn(Optional.of(recycleBin));
        RecipeCategory recipeCategory = mock(RecipeCategory.class);
        when(recipeCategoryRepository.findByNameIgnoreCase(anyString())).thenReturn(Optional.of(recipeCategory));
        recycleBinService.restoreElement(1L);
        verify(recipeCategoryRepository, times(1)).restore(0L);
    }

    @Test
    public void restoreElement_RecipeSubcategory() {
        RecycleBin recycleBin = createRecycleBin("Гречневая каша", EntityNamesConsts.RECIPE_SUBCATEGORY, LocalDateTime.now());
        when(recycleBinRepository.findById(anyLong())).thenReturn(Optional.of(recycleBin));
        RecipeSubcategory recipeSubcategory = mock(RecipeSubcategory.class);
        when(recipeSubcategoryRepository.findByNameIgnoreCase(anyString())).thenReturn(Optional.of(recipeSubcategory));
        recycleBinService.restoreElement(1L);
        verify(recipeSubcategoryRepository, times(1)).restore(0L);
    }

    @Test
    public void restoreElement_CookingMethod() {
        RecycleBin recycleBin = createRecycleBin("Варка", EntityNamesConsts.COOKING_METHOD, LocalDateTime.now());
        when(recycleBinRepository.findById(anyLong())).thenReturn(Optional.of(recycleBin));
        CookingMethod cookingMethod = mock(CookingMethod.class);
        when(cookingMethodRepository.findByNameIgnoreCase(anyString())).thenReturn(Optional.of(cookingMethod));
        recycleBinService.restoreElement(1L);
        verify(cookingMethodRepository, times(1)).restore(0);
    }

    @Test
    public void restoreElement_MenuCategory() {
        RecycleBin recycleBin = createRecycleBin("Диетическое", EntityNamesConsts.MENU_CATEGORY, LocalDateTime.now());
        when(recycleBinRepository.findById(anyLong())).thenReturn(Optional.of(recycleBin));
        MenuCategory menuCategory = mock(MenuCategory.class);
        when(menuCategoryRepository.findByNameIgnoreCase(anyString())).thenReturn(Optional.of(menuCategory));
        recycleBinService.restoreElement(1L);
        verify(menuCategoryRepository, times(1)).restore(0);
    }

    @Test
    public void restoreElement_MealType() {
        RecycleBin recycleBin = createRecycleBin("Завтрак", EntityNamesConsts.MEAL_TYPE, LocalDateTime.now());
        when(recycleBinRepository.findById(anyLong())).thenReturn(Optional.of(recycleBin));
        MealType mealType = mock(MealType.class);
        when(mealTypeRepository.findByNameIgnoreCase(anyString())).thenReturn(Optional.of(mealType));
        recycleBinService.restoreElement(1L);
        verify(mealTypeRepository, times(1)).restore(new Short("0"));
    }

    @Test
    public void findAll() {
        List<RecycleBin> list = new ArrayList<>();
        list.add(createRecycleBin("Гречневая каша", "Рецепт", LocalDateTime.now()));
        list.add(createRecycleBin("Гречка", "Ингредиент", LocalDateTime.now()));
        when(recycleBinRepository.findAll()).thenReturn(list);
        List<RecycleBinDTO> result = recycleBinService.findAll();
        assertThat(result.size()).isEqualTo(list.size());
    }

    @Test
    public void deleteElement_UnitOfMeasure() {
        RecycleBin recycleBin = createRecycleBin("Литр", EntityNamesConsts.UNIT_OF_MEASURE, LocalDateTime.now());
        when(recycleBinRepository.findById(anyLong())).thenReturn(Optional.of(recycleBin));
        UnitOfMeasure unitOfMeasure = mock(UnitOfMeasure.class);
        when(unitOfMeasureRepository.findByFullNameIgnoreCase(anyString())).thenReturn(Optional.of(unitOfMeasure));
        recycleBinService.deleteElement(1L);
        verify(unitOfMeasureRepository, times(1)).deleteById(0L);
    }

    @Test
    public void deleteElement_Currency() {
        RecycleBin recycleBin = createRecycleBin("Бел.руб.", EntityNamesConsts.CURRENCY, LocalDateTime.now());
        when(recycleBinRepository.findById(anyLong())).thenReturn(Optional.of(recycleBin));
        Currency currency = mock(Currency.class);
        when(currencyRepository.findByNameIgnoreCase(anyString())).thenReturn(Optional.of(currency));
        recycleBinService.deleteElement(1L);
        verify(currencyRepository, times(1)).deleteById(0);
    }

    @Test
    public void deleteElement_Country() {
        RecycleBin recycleBin = createRecycleBin("Беларусь", EntityNamesConsts.COUNTRY, LocalDateTime.now());
        when(recycleBinRepository.findById(anyLong())).thenReturn(Optional.of(recycleBin));
        Country country = mock(Country.class);
        when(countryRepository.findByNameIgnoreCase(anyString())).thenReturn(Optional.of(country));
        recycleBinService.deleteElement(1L);
        verify(countryRepository, times(1)).deleteById(0L);
    }

    @Test
    public void deleteElement_Region() {
        RecycleBin recycleBin = createRecycleBin("Минск",EntityNamesConsts.REGION, LocalDateTime.now());
        when(recycleBinRepository.findById(anyLong())).thenReturn(Optional.of(recycleBin));
        Region region = mock(Region.class);
        when(regionRepository.findByNameIgnoreCase(anyString())).thenReturn(Optional.of(region));
        recycleBinService.deleteElement(1L);
        verify(regionRepository, times(1)).deleteById(0L);
    }

    @Test
    public void deleteElement_Ingredient() {
        RecycleBin recycleBin = createRecycleBin("Гречка", EntityNamesConsts.INGREDIENT, LocalDateTime.now());
        when(recycleBinRepository.findById(anyLong())).thenReturn(Optional.of(recycleBin));
        Ingredient ingredient = mock(Ingredient.class);
        when(ingredientRepository.findByNameIgnoreCase(anyString())).thenReturn(Optional.of(ingredient));
        recycleBinService.deleteElement(1L);
        verify(ingredientService, times(1)).delete(0L);
    }

    @Test
    public void deleteElement_Recipe() {
        RecycleBin recycleBin = createRecycleBin("Гречневая каша", EntityNamesConsts.RECIPE, LocalDateTime.now());
        when(recycleBinRepository.findById(anyLong())).thenReturn(Optional.of(recycleBin));
        Recipe recipe = mock(Recipe.class);
        when(recipeRepository.findByNameIgnoreCase(anyString())).thenReturn(Optional.of(recipe));
        recycleBinService.deleteElement(1L);
        verify(recipeService, times(1)).delete(0L);
    }

    @Test
    public void deleteElement_RecipeCategory() {
        RecycleBin recycleBin = createRecycleBin("Гречневая каша", EntityNamesConsts.RECIPE_CATEGORY, LocalDateTime.now());
        when(recycleBinRepository.findById(anyLong())).thenReturn(Optional.of(recycleBin));
        RecipeCategory recipeCategory = mock(RecipeCategory.class);
        when(recipeCategoryRepository.findByNameIgnoreCase(anyString())).thenReturn(Optional.of(recipeCategory));
        recycleBinService.deleteElement(1L);
        verify(recipeCategoryRepository, times(1)).deleteById(0L);
    }

    @Test
    public void deleteElement_RecipeSubcategory() {
        RecycleBin recycleBin = createRecycleBin("Гречневая каша", EntityNamesConsts.RECIPE_SUBCATEGORY, LocalDateTime.now());
        when(recycleBinRepository.findById(anyLong())).thenReturn(Optional.of(recycleBin));
        RecipeSubcategory recipeSubcategory = mock(RecipeSubcategory.class);
        when(recipeSubcategoryRepository.findByNameIgnoreCase(anyString())).thenReturn(Optional.of(recipeSubcategory));
        recycleBinService.deleteElement(1L);
        verify(recipeSubcategoryRepository, times(1)).deleteById(0L);
    }

    @Test
    public void deleteElement_CookingMethod() {
        RecycleBin recycleBin = createRecycleBin("Варка", EntityNamesConsts.COOKING_METHOD, LocalDateTime.now());
        when(recycleBinRepository.findById(anyLong())).thenReturn(Optional.of(recycleBin));
        CookingMethod cookingMethod = mock(CookingMethod.class);
        when(cookingMethodRepository.findByNameIgnoreCase(anyString())).thenReturn(Optional.of(cookingMethod));
        recycleBinService.deleteElement(1L);
        verify(cookingMethodRepository, times(1)).deleteById(0);
    }

    @Test
    public void deleteElement_MenuCategory() {
        RecycleBin recycleBin = createRecycleBin("Диетическое", EntityNamesConsts.MENU_CATEGORY, LocalDateTime.now());
        when(recycleBinRepository.findById(anyLong())).thenReturn(Optional.of(recycleBin));
        MenuCategory menuCategory = mock(MenuCategory.class);
        when(menuCategoryRepository.findByNameIgnoreCase(anyString())).thenReturn(Optional.of(menuCategory));
        recycleBinService.deleteElement(1L);
        verify(menuCategoryRepository, times(1)).deleteById(0);
    }

    @Test
    public void deleteElement_MealType() {
        RecycleBin recycleBin = createRecycleBin("Завтрак", EntityNamesConsts.MEAL_TYPE, LocalDateTime.now());
        when(recycleBinRepository.findById(anyLong())).thenReturn(Optional.of(recycleBin));
        MealType mealType = mock(MealType.class);
        when(mealTypeRepository.findByNameIgnoreCase(anyString())).thenReturn(Optional.of(mealType));
        recycleBinService.deleteElement(1L);
        verify(mealTypeRepository, times(1)).deleteById(new Short("0"));
    }

    @Test
    public void findById() {
        LocalDateTime date = LocalDateTime.now();
        RecycleBin recycleBin = createRecycleBin("Гречневая каша", "Рецепт", date);
        when(recycleBinRepository.findById(anyLong())).thenReturn(Optional.of(recycleBin));
        RecycleBin found = recycleBinService.findById(1L);
        assertThat(found).isNotNull();
        assertThat(found.getElementName()).isEqualTo("Гречневая каша");
        assertThat(found.getEntityName()).isEqualTo("Рецепт");
        assertThat(found.getDeleteDate()).isEqualTo(date);
    }
}