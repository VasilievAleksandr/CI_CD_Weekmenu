package by.weekmenu.api.service;

import by.weekmenu.api.dto.IngredientDTO;
import by.weekmenu.api.dto.UnitOfMeasureDTO;
import by.weekmenu.api.entity.*;
import by.weekmenu.api.repository.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
public class IngredientServiceImplTest {

    @MockBean
    private IngredientRepository ingredientRepository;

    @MockBean
    private OwnershipRepository ownershipRepository;

    @MockBean
    private UnitOfMeasureRepository unitOfMeasureRepository;

    @MockBean
    private RegionRepository regionRepository;

    @MockBean
    private IngredientPriceRepository ingredientPriceRepository;

    @MockBean
    private IngredientUnitOfMeasureRepository ingredientUnitOfMeasureRepository;

    @MockBean
    private RecipeService recipeService;

    @MockBean
    private RecycleBinRepository recycleBinRepository;

    @MockBean
    private RecipeIngredientRepository recipeIngredientRepository;

    @MockBean
    private IngredientCategoryRepository ingredientCategoryRepository;

    @MockBean
    private ModelMapper modelMapper;

    private IngredientService ingredientService;

    @Before
    public void setup() {
        ingredientService = new IngredientServiceImpl(ingredientRepository, ownershipRepository,
                unitOfMeasureRepository, ingredientUnitOfMeasureRepository,
                regionRepository, ingredientPriceRepository, recipeService,
                recycleBinRepository, recipeIngredientRepository, ingredientCategoryRepository, modelMapper);
    }

    private Ingredient createIngredient(String name) {
        Ingredient ingredient = new Ingredient();
        ingredient.setName(name);
        ingredient.setCalories(new BigDecimal("100"));
        ingredient.setCarbs(new BigDecimal("100"));
        ingredient.setFats(new BigDecimal("100"));
        ingredient.setProteins(new BigDecimal("100"));
        return ingredient;
    }

    private IngredientDTO createIngredientDto(String name) {
        IngredientDTO ingredientDto = new IngredientDTO();
        ingredientDto.setName(name);
        ingredientDto.setCalories(new BigDecimal("100"));
        ingredientDto.setCarbs(new BigDecimal("100"));
        ingredientDto.setFats(new BigDecimal("100"));
        ingredientDto.setProteins(new BigDecimal("100"));
        UnitOfMeasureDTO unitOfMeasureDto = new UnitOfMeasureDTO();
        unitOfMeasureDto.setShortName("Гр");
        unitOfMeasureDto.setFullName("Грамм");
        return ingredientDto;
    }

    @Test
    public void saveIngredientTest() {
        Ingredient ingredient = createIngredient("Курица");
        IngredientDTO ingredientDto = createIngredientDto("Курица");
        when(modelMapper.map(ingredientDto, Ingredient.class)).thenReturn(ingredient);
        when(ingredientRepository.save(ingredient)).thenReturn(ingredient);
        when(modelMapper.map(ingredient, IngredientDTO.class)).thenReturn(ingredientDto);

        IngredientDTO saved = ingredientService.save(ingredientDto);
        assertThat(saved).isNotNull();
        assertThat(saved.getName()).isEqualTo("Курица");
        assertThat(saved.getCalories()).isEqualTo(new BigDecimal("100"));
        assertThat(saved.getCarbs()).isEqualTo(new BigDecimal("100"));
        assertThat(saved.getFats()).isEqualTo(new BigDecimal("100"));
        assertThat(saved.getProteins()).isEqualTo(new BigDecimal("100"));
    }

    @Test
    public void findByIdTest() {
        IngredientDTO ingredientDto = createIngredientDto("Курица");
        Ingredient ingredient = createIngredient("Курица");
        when(modelMapper.map(ingredient, IngredientDTO.class)).thenReturn(ingredientDto);
        when(ingredientRepository.findById(ingredientDto.getId())).thenReturn(Optional.of(ingredient));
        IngredientDTO found = ingredientService.findById(ingredientDto.getId());
        assertThat(found).isNotNull();
        assertThat(found.getName()).isEqualTo("Курица");
        assertThat(found.getCalories()).isEqualTo(new BigDecimal("100"));
        assertThat(found.getCarbs()).isEqualTo(new BigDecimal("100"));
        assertThat(found.getFats()).isEqualTo(new BigDecimal("100"));
        assertThat(found.getProteins()).isEqualTo(new BigDecimal("100"));
    }

    @Test
    public void deleteTest() {
        ingredientService.delete(1L);
        verify(ingredientRepository, times(1)).deleteById(1L);
        verify(ingredientUnitOfMeasureRepository, times(1)).deleteIngredientUnitOfMeasuresById_IngredientId(1L);
        verify(ingredientPriceRepository, times(1)).deleteIngredientPricesById_IngredientId(1L);
    }

    @Test
    public void findAllIngredientsTest() {
        Ingredient ingredient1 = createIngredient("Курица");
        Ingredient ingredient2 = createIngredient("Ананас");
        List<Ingredient> ingredients = Arrays.asList(ingredient1, ingredient2);
        IngredientDTO ingredientDTO1 = createIngredientDto("Курица");
        IngredientDTO ingredientDTO2 = createIngredientDto("Ананас");
        when(ingredientRepository.findAllByIsArchivedIsFalse()).thenReturn(ingredients);
        when(modelMapper.map(ingredient1, IngredientDTO.class)).thenReturn(ingredientDTO1);
        when(modelMapper.map(ingredient2, IngredientDTO.class)).thenReturn(ingredientDTO2);
        List<IngredientDTO> result = ingredientService.findAll();
        assertThat(result.size()).isEqualTo(ingredients.size());
    }

    @Test
    public void findByNameTest() {
        Ingredient ingredient = createIngredient("Курица");
        when(ingredientRepository.findByNameIgnoreCase("Курица")).thenReturn(Optional.of(ingredient));
        Ingredient ingredient1 = ingredientService.findByName("Курица");
        assertThat(ingredient1).isNotNull();
        assertThat(ingredient1.getName()).isEqualTo("Курица");
    }

    @Test
    public void moveToRecycleBinTest() {
        IngredientDTO ingredientDto = createIngredientDto("Курица");
        when(recycleBinRepository.save(any(RecycleBin.class))).thenReturn(new RecycleBin());
        ingredientService.moveToRecycleBin(ingredientDto);
        verify(ingredientRepository, times(1)).softDelete(ingredientDto.getId());
    }

    @Test
    public void checkConnectedElementsTest() {
        Ingredient ingredient = createIngredient("Курица");
        Set<RecipeIngredient> recipeIngredients = new HashSet<>();
        RecipeIngredient recipeIngredient = new RecipeIngredient(new BigDecimal("111"),
                ingredient, new Recipe("рецепт", true,
                new CookingMethod("жарка"), new Ownership(OwnershipName.USER)));
        recipeIngredient.setUnitOfMeasure(new UnitOfMeasure("Гр", "Грамм"));
        recipeIngredients.add(recipeIngredient);
        when(recipeIngredientRepository.findAllById_IngredientId(ingredient.getId())).thenReturn(recipeIngredients);
        List<String> list = ingredientService.checkConnectedElements(ingredient.getId());
        assertThat(list.size()).isEqualTo(1);
    }

}