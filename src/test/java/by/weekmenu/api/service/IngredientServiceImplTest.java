package by.weekmenu.api.service;

import by.weekmenu.api.dto.IngredientDto;
import by.weekmenu.api.dto.UnitOfMeasureDTO;
import by.weekmenu.api.entity.Ingredient;
import by.weekmenu.api.repository.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

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
    private ModelMapper modelMapper;

    private IngredientService ingredientService;

    @Before
    public void setup() {
        ingredientService = new IngredientServiceImpl(ingredientRepository, ownershipRepository,
                unitOfMeasureRepository, ingredientUnitOfMeasureRepository,
                regionRepository, ingredientPriceRepository, recipeService, modelMapper);
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

    private IngredientDto createIngredientDto(String name) {
        IngredientDto ingredientDto = new IngredientDto();
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
        IngredientDto ingredientDto = createIngredientDto("Курица");
        when(modelMapper.map(ingredientDto, Ingredient.class)).thenReturn(ingredient);
        when(ingredientRepository.save(ingredient)).thenReturn(ingredient);
        when(modelMapper.map(ingredient, IngredientDto.class)).thenReturn(ingredientDto);

        IngredientDto saved = ingredientService.save(ingredientDto);
        assertThat(saved).isNotNull();
        assertThat(saved.getName()).isEqualTo("Курица");
        assertThat(saved.getCalories()).isEqualTo(new BigDecimal("100"));
        assertThat(saved.getCarbs()).isEqualTo(new BigDecimal("100"));
        assertThat(saved.getFats()).isEqualTo(new BigDecimal("100"));
        assertThat(saved.getProteins()).isEqualTo(new BigDecimal("100"));
    }

    @Test
    public void findById() {
        IngredientDto ingredientDto = createIngredientDto("Курица");
        Ingredient ingredient = createIngredient("Курица");
        when(modelMapper.map(ingredient, IngredientDto.class)).thenReturn(ingredientDto);
        when(ingredientRepository.findById(ingredientDto.getId())).thenReturn(Optional.of(ingredient));
        IngredientDto found = ingredientService.findById(ingredientDto.getId());
        assertThat(found).isNotNull();
        assertThat(found.getName()).isEqualTo("Курица");
        assertThat(found.getCalories()).isEqualTo(new BigDecimal("100"));
        assertThat(found.getCarbs()).isEqualTo(new BigDecimal("100"));
        assertThat(found.getFats()).isEqualTo(new BigDecimal("100"));
        assertThat(found.getProteins()).isEqualTo(new BigDecimal("100"));
    }

    @Test
    public void delete() {
        ingredientService.delete(1L);
        verify(ingredientRepository, times(1)).deleteById(1L);
    }

    @Test
    public void findAll() {
        Ingredient ingredient1 = createIngredient("Курица");
        Ingredient ingredient2 = createIngredient("Ананас");
        List<Ingredient> ingredients = Arrays.asList(ingredient1, ingredient2);
        IngredientDto ingredientDto1 = createIngredientDto("Курица");
        IngredientDto ingredientDto2 = createIngredientDto("Ананас");
        when(ingredientRepository.findAll()).thenReturn(ingredients);
        when(modelMapper.map(ingredient1, IngredientDto.class)).thenReturn(ingredientDto1);
        when(modelMapper.map(ingredient2, IngredientDto.class)).thenReturn(ingredientDto2);
        List<IngredientDto> result = ingredientService.findAll();
        assertThat(result.size()).isEqualTo(ingredients.size());
    }

    @Test
    public void findByName() {
        Ingredient ingredient = createIngredient("Курица");
        when(ingredientRepository.findByNameIgnoreCase("Курица")).thenReturn(Optional.of(ingredient));
        Ingredient ingredient1 = ingredientService.findByName("Курица");
        assertThat(ingredient1).isNotNull();
        assertThat(ingredient1.getName()).isEqualTo("Курица");
    }
}