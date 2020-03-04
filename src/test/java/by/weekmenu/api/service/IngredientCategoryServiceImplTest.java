package by.weekmenu.api.service;

import by.weekmenu.api.dto.IngredientCategoryDTO;
import by.weekmenu.api.entity.*;
import by.weekmenu.api.repository.IngredientCategoryRepository;
import by.weekmenu.api.repository.IngredientRepository;
import by.weekmenu.api.repository.RecycleBinRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
public class IngredientCategoryServiceImplTest {

    @MockBean
    private IngredientCategoryRepository ingredientCategoryRepository;

    @MockBean
    private RecycleBinRepository recycleBinRepository;

    @MockBean
    private IngredientRepository ingredientRepository;

    @MockBean
    private ModelMapper modelMapper;

    private IngredientCategoryServiceImpl ingredientCategoryService;

    @Before
    public void setup() {
        ingredientCategoryService = new IngredientCategoryServiceImpl(ingredientCategoryRepository, recycleBinRepository,
                ingredientRepository, modelMapper);
    }

    @Test
    public void getAllIngredientCategoryTest() {
        List<IngredientCategory> ingredientCategories = new ArrayList<>();
        ingredientCategories.add(new IngredientCategory(1, "Milk", false));
        ingredientCategories.add(new IngredientCategory(2, "Bread", false));
        when(ingredientCategoryRepository.findAllByIsArchivedIsFalse()).thenReturn(ingredientCategories);
        List<IngredientCategoryDTO> result = ingredientCategoryService.findAll();
        assertThat(ingredientCategories.size()).isEqualTo(result.size());
    }

    @Test
    public void saveIngredientCategoryTest() {
        IngredientCategoryDTO ingredientCategoryDTO = new IngredientCategoryDTO();
        ingredientCategoryDTO.setId(1);
        ingredientCategoryDTO.setName("Milk");
        IngredientCategory ingredientCategory = new IngredientCategory(1, "Milk", true);
        when(modelMapper.map(ingredientCategoryDTO, IngredientCategory.class)).thenReturn(ingredientCategory);
        when(ingredientCategoryRepository.save(ingredientCategory)).thenReturn(ingredientCategory);
        when(modelMapper.map(ingredientCategory, IngredientCategoryDTO.class)).thenReturn(ingredientCategoryDTO);
        IngredientCategoryDTO saved = ingredientCategoryService.save(ingredientCategoryDTO);
        assertThat(saved).isNotNull();
        assertThat(saved.getName()).isEqualTo("Milk");
    }

    @Test
    public void findIngredientCategoryByIdTest() {
        IngredientCategoryDTO ingredientCategoryDTO = new IngredientCategoryDTO();
        ingredientCategoryDTO.setId(1);
        ingredientCategoryDTO.setName("Milk");
        IngredientCategory ingredientCategory = new IngredientCategory(1, "Milk", true);
        when(modelMapper.map(ingredientCategory, IngredientCategoryDTO.class)).thenReturn(ingredientCategoryDTO);
        when(ingredientCategoryRepository.findById(ingredientCategoryDTO.getId())).thenReturn(Optional.of(ingredientCategory));
        IngredientCategoryDTO found = ingredientCategoryService.findById(ingredientCategoryDTO.getId());
        assertThat(found).isNotNull();
        assertThat(found.getName()).isEqualTo("Milk");
    }

    @Test
    public void checkUniqueIngredientCategoryNameTest() {
        when(ingredientCategoryRepository.findByNameIgnoreCase(anyString())).thenReturn(Optional.empty());
        IngredientCategory ingredientCategory = ingredientCategoryService.findByName("Milk");
        assertThat(ingredientCategory).isNull();
    }

    @Test
    public void moveToRecycleBinTest() {
        IngredientCategoryDTO ingredientCategoryDTO = new IngredientCategoryDTO();
        ingredientCategoryDTO.setId(1);
        ingredientCategoryDTO.setName("Milk");
        when(recycleBinRepository.save(any(RecycleBin.class))).thenReturn(new RecycleBin());
        ingredientCategoryService.moveToRecycleBin(ingredientCategoryDTO);
        verify(ingredientCategoryRepository, times(1)).softDelete(ingredientCategoryDTO.getId());
    }

    @Test
    public void checkConnectedElementsTest() {
        IngredientCategory ingredientCategory = new IngredientCategory(1, "Молочные продукты", true);
        Ingredient ingredient = new Ingredient();
        ingredient.setName("Молоко");
        ingredient.setCalories(new BigDecimal("100"));
        ingredient.setCarbs(new BigDecimal("100"));
        ingredient.setFats(new BigDecimal("100"));
        ingredient.setProteins(new BigDecimal("100"));
        ingredient.setIngredientCategory(ingredientCategory);
        List<Ingredient> ingredients = new ArrayList();
        ingredients.add(ingredient);
        when(ingredientCategoryRepository.findById(ingredientCategory.getId())).thenReturn(Optional.of(ingredientCategory));
        when(ingredientRepository.findAllByIngredientCategory_Id(ingredientCategory.getId())).thenReturn(ingredients);
        List<String> list = ingredientCategoryService.checkConnectedElements(ingredientCategory.getId());
        assertThat(list.size()).isEqualTo(1);
    }
}
