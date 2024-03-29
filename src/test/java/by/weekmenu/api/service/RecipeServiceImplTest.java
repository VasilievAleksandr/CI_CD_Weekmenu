package by.weekmenu.api.service;

import by.weekmenu.api.dto.CookingStepDTO;
import by.weekmenu.api.dto.RecipeDTO;
import by.weekmenu.api.dto.RecipeIngredientDTO;
import by.weekmenu.api.entity.*;
import by.weekmenu.api.repository.*;
import by.weekmenu.api.utils.RecipeCalculation;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
public class RecipeServiceImplTest {

    @MockBean
    private RecipeRepository recipeRepository;
    @MockBean
    private OwnershipRepository ownershipRepository;
    @MockBean
    private CookingMethodRepository cookingMethodRepository;
    @MockBean
    private IngredientRepository ingredientRepository;
    @MockBean
    private UnitOfMeasureRepository unitOfMeasureRepository;
    @MockBean
    private RecipeIngredientRepository recipeIngredientRepository;
    @MockBean
    private CookingStepRepository cookingStepRepository;
    @MockBean
    private RecipePriceRepository recipePriceRepository;
    @MockBean
    private RecycleBinRepository recycleBinRepository;
    @MockBean
    private MenuRecipeRepository menuRecipeRepository;
    @MockBean
    private RecipeCategoryRepository recipeCategoryRepository;
    @MockBean
    private RecipeSubcategoryRepository recipeSubcategoryRepository;
    @MockBean
    private MenuService menuService;
    @MockBean
    private ModelMapper modelMapper;
    @MockBean
    private RecipeCalculation recipeCalculation;

    private RecipeService recipeService;

    @Before
    public void setup(){
        recipeService = new RecipeServiceImpl(recipeRepository, ownershipRepository,
                cookingMethodRepository, ingredientRepository,
                unitOfMeasureRepository, recipeIngredientRepository, cookingStepRepository,
                recipePriceRepository, recycleBinRepository, menuRecipeRepository,
                recipeCategoryRepository, recipeSubcategoryRepository, recipeCalculation,
                menuService, modelMapper);
    }

    private RecipeDTO createRecipeDto(String name) {
        RecipeDTO recipeDto = new RecipeDTO();
        recipeDto.setName(name);
        recipeDto.setCookingTime("30");
        recipeDto.setActiveTime("15");
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

    private Recipe createRecipe(String name) {
        Recipe recipe = new Recipe();
        recipe.setName(name);
        recipe.setCookingTime((short)30);
        recipe.setActiveTime((short)15);
        recipe.setPortions((short)2);
        recipe.setImageLink("images/image.png");
        recipe.setSource("http://bestrecipes.com/best-recipe");
        recipe.setOwnership(new Ownership(OwnershipName.ADMIN));
        recipe.setCookingMethod(new CookingMethod("Варка"));
        Set<RecipeCategory> recipeCategories = new HashSet<>();
        recipeCategories.add(new RecipeCategory("Обед"));
        recipeCategories.add(new RecipeCategory("Ужин"));
        recipe.setRecipeCategories(recipeCategories);
        Set<RecipeSubcategory> recipeSubcategories = new HashSet<>();
        recipeSubcategories.add(new RecipeSubcategory("Курица"));
        recipeSubcategories.add(new RecipeSubcategory("Мясо"));
        recipe.setRecipeSubcategories(recipeSubcategories);
        return recipe;
    }

    private CookingStep createCookingStep() {
        CookingStep cookingStep = new CookingStep();
        cookingStep.setDescription("Нарежьте овощи");
        cookingStep.setPriority(1);
        return cookingStep;
    }

    @Test
    public void saveRecipeTest() {
        RecipeDTO recipeDto = createRecipeDto("Гречневая каша");
        Recipe recipe = createRecipe("Гречневая каша");
        when(modelMapper.map(recipeDto, Recipe.class)).thenReturn(recipe);
        when(recipeRepository.save(recipe)).thenReturn(recipe);
        when(modelMapper.map(recipe, RecipeDTO.class)).thenReturn(recipeDto);
        when(ingredientRepository.findByNameIgnoreCase(anyString())).thenReturn(Optional.of(new Ingredient()));
        Set<CookingStepDTO> cookingSteps = recipeDto.getCookingSteps();
        CookingStepDTO cookingStepDto = cookingSteps.iterator().next();
        CookingStep cookingStep = createCookingStep();
        when(modelMapper.map(cookingStepDto, CookingStep.class)).thenReturn(cookingStep);
        RecipeDTO saved = recipeService.save(recipeDto);
        assertThat(saved).isNotNull();
        assertThat(saved.getName()).isEqualTo("Гречневая каша");
        assertThat(saved.getCookingMethodName()).isEqualTo("Варка");
        assertThat(saved.getOwnershipName()).isEqualTo("ADMIN");
        assertThat(saved.getImageLink()).isEqualTo("images/image.png");
        assertThat(saved.getSource()).isEqualTo("http://bestrecipes.com/best-recipe");
        assertThat(saved.getCookingTime()).isEqualTo("30");
        assertThat(saved.getActiveTime()).isEqualTo("15");
        assertThat(saved.getPortions()).isEqualTo((short)2);
        assertThat(saved.getCategoryNames().size()).isEqualTo(2);
        assertThat(saved.getSubcategoryNames().size()).isEqualTo(2);
    }

    @Test
    public void findRecipeByIdTest() {
        RecipeDTO recipeDto = createRecipeDto("Гречневая каша");
        Recipe recipe = createRecipe("Гречневая каша");
        when(modelMapper.map(recipe, RecipeDTO.class)).thenReturn(recipeDto);
        when(recipeRepository.findById(recipeDto.getId())).thenReturn(Optional.of(recipe));
        RecipeDTO found = recipeService.findById(recipeDto.getId());
        assertThat(found).isNotNull();
        assertThat(found.getName()).isEqualTo("Гречневая каша");
        assertThat(found.getCookingMethodName()).isEqualTo("Варка");
        assertThat(found.getOwnershipName()).isEqualTo("ADMIN");
        assertThat(found.getImageLink()).isEqualTo("images/image.png");
        assertThat(found.getSource()).isEqualTo("http://bestrecipes.com/best-recipe");
        assertThat(found.getCookingTime()).isEqualTo("30");
        assertThat(found.getActiveTime()).isEqualTo("15");
        assertThat(found.getPortions()).isEqualTo((short)2);
        assertThat(found.getCategoryNames().size()).isEqualTo(2);
        assertThat(found.getSubcategoryNames().size()).isEqualTo(2);
    }

    @Test
    public void deleteRecipeTest() {
        recipeService.delete(1L);
        verify(recipeRepository, times(1)).deleteById(1L);
        verify(cookingStepRepository, times(1)).deleteAllByRecipe_Id(1L);
        verify(recipeIngredientRepository, times(1)).deleteById_RecipeId(1L);
        verify(recipePriceRepository, times(1)).deleteById_RecipeId(1L);
    }

    @Test
    public void findAllRecipesTest() {
        Recipe recipe1 = createRecipe("Гречневая каша");
        Recipe recipe2 = createRecipe("Гречка с овощами");
        List<Recipe> recipes = Arrays.asList(recipe1, recipe2);
        RecipeDTO recipeDTO1 = createRecipeDto("Гречневая каша");
        RecipeDTO recipeDTO2 = createRecipeDto("Гречка с овощами");
        when(recipeRepository.findAllByIsArchivedIsFalse()).thenReturn(recipes);
        when(modelMapper.map(recipe1, RecipeDTO.class)).thenReturn(recipeDTO1);
        when(modelMapper.map(recipe2, RecipeDTO.class)).thenReturn(recipeDTO2);
        List<RecipeDTO> result = recipeService.findAll();
        assertThat(result.size()).isEqualTo(recipes.size());
    }

    @Test
    public void findRecipeByName() {
        Recipe recipe = createRecipe("Гречневая каша");
        when(recipeRepository.findByNameIgnoreCase("Гречневая каша")).thenReturn(Optional.of(recipe));
        Recipe found = recipeService.findByName("Гречневая каша");
        assertThat(found).isNotNull();
        assertThat(found.getName()).isEqualTo("Гречневая каша");
    }

    @Test
    public void moveToRecycleBinTest() {
        RecipeDTO recipeDto = createRecipeDto("Гречневая каша");
        when(recycleBinRepository.save(any(RecycleBin.class))).thenReturn(new RecycleBin());
        recipeService.moveToRecycleBin(recipeDto);
        verify(recipeRepository, times(1)).softDelete(recipeDto.getId());
    }

    @Test
    public void checkConnectedElementsTest() {
        Recipe recipe = createRecipe("Гречневая каша");
        List<MenuRecipe> menuRecipes = new ArrayList<>();
        menuRecipes.add(new MenuRecipe(new Menu("Бюджетное", true, new Ownership(OwnershipName.USER)),
                recipe, new MealType("Обед", 20),
                DayOfWeek.MONDAY));
        when(menuRecipeRepository.findAllByRecipe_Id(recipe.getId())).thenReturn(menuRecipes);
        List<String> list = recipeService.checkConnectedElements(recipe.getId());
        assertThat(list.size()).isEqualTo(1);
    }

    @Test
    public void updateRecipesTest() {
        Set<RecipeIngredient> recipeIngredients = new HashSet<>();
        RecipeIngredient recipeIngredient = mock(RecipeIngredient.class);
        recipeIngredients.add(recipeIngredient);
        when(recipeIngredientRepository.findAllById_IngredientId(anyLong())).thenReturn(recipeIngredients);
        Recipe recipe = createRecipe("Гречневая каша");
        RecipeDTO recipeDto = createRecipeDto("Гечневая каша");
        when(recipeIngredient.getRecipe()).thenReturn(recipe);
        when(modelMapper.map(recipe, RecipeDTO.class)).thenReturn(recipeDto);
        recipeService.updateRecipes(1L);
        verify(recipeRepository, times(1)).save(recipe);
    }
}
