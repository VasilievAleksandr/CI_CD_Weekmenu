package by.weekmenu.api.service;

import by.weekmenu.api.dto.CookingMethodDTO;
import by.weekmenu.api.entity.*;
import by.weekmenu.api.repository.CookingMethodRepository;
import by.weekmenu.api.repository.RecipeRepository;
import by.weekmenu.api.repository.RecycleBinRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
public class CookingMethodServiceImplTest {

    @MockBean
    private CookingMethodRepository cookingMethodRepository;

    @MockBean
    private RecipeRepository recipeRepository;

    @MockBean
    private RecycleBinRepository recycleBinRepository;

    @MockBean
    private ModelMapper modelMapper;

    private CookingMethodService cookingMethodService;

    @Before
    public void setup() {
        cookingMethodService = new CookingMethodServiceImpl(cookingMethodRepository, recipeRepository, recycleBinRepository, modelMapper);
    }

    @Test
    public void getAllCookingMethodTest() {
        List<CookingMethod> cookingMethods = new ArrayList<>();
        cookingMethods.add(new CookingMethod(1, "Жарка"));
        cookingMethods.add(new CookingMethod(2, "Варка"));
        when(cookingMethodRepository.findAllByIsArchivedIsFalse()).thenReturn(cookingMethods);
        List<CookingMethodDTO> result = cookingMethodService.findAll();
        assertThat(cookingMethods.size()).isEqualTo(result.size());
    }

    @Test
    public void saveCookingMethodTest() {
        CookingMethodDTO cookingMethodDTO = new CookingMethodDTO();
        cookingMethodDTO.setId(1);
        cookingMethodDTO.setName("Жарка");
        CookingMethod сookingMethod = new CookingMethod(1, "Жарка");
        when(modelMapper.map(cookingMethodDTO, CookingMethod.class)).thenReturn(сookingMethod);
        when(cookingMethodRepository.save(сookingMethod)).thenReturn(сookingMethod);
        when(modelMapper.map(сookingMethod, CookingMethodDTO.class)).thenReturn(cookingMethodDTO);
        CookingMethodDTO saved = cookingMethodService.save(cookingMethodDTO);
        assertThat(saved).isNotNull();
        assertThat(saved.getName()).isEqualTo("Жарка");
    }

    @Test
    public void findCookingMethodByIdTest() {
        CookingMethodDTO cookingMethodDTO = new CookingMethodDTO();
        cookingMethodDTO.setId(1);
        cookingMethodDTO.setName("Жарка");
        CookingMethod cookingMethod = new CookingMethod(1, "Жарка");
        when(modelMapper.map(cookingMethod, CookingMethodDTO.class)).thenReturn(cookingMethodDTO);
        when(cookingMethodRepository.findById(cookingMethodDTO.getId())).thenReturn(Optional.of(cookingMethod));
        CookingMethodDTO found = cookingMethodService.findById(cookingMethodDTO.getId());
        assertThat(found).isNotNull();
        assertThat(found.getName()).isEqualTo("Жарка");
    }

    @Test
    public void checkUniqueCookingMethodNameTest() {
        when(cookingMethodRepository.findByNameIgnoreCase(anyString())).thenReturn(Optional.empty());
        CookingMethod cookingMethod = cookingMethodService.findByName("Жарка");
        assertThat(cookingMethod).isNull();
    }

    @Test
    public void moveToRecycleBinTest() {
        CookingMethodDTO cookingMethodDTO = new CookingMethodDTO();
        cookingMethodDTO.setId(1);
        cookingMethodDTO.setName("Жарка");
        when(recycleBinRepository.save(any(RecycleBin.class))).thenReturn(new RecycleBin());
        cookingMethodService.moveToRecycleBin(cookingMethodDTO);
        verify(cookingMethodRepository, times(1)).softDelete(cookingMethodDTO.getId());
        assertThat(cookingMethodDTO.getName()).isEqualTo("Жарка");
    }

    @Test
    public void checkConnectedElementsTest() {
        CookingMethod cookingMethod = new CookingMethod(1,"Жарка");
        Recipe recipe = new Recipe();
        recipe.setName("Рецепт");
        recipe.setCookingTime((short) 30);
        recipe.setPreparingTime((short) 15);
        recipe.setPortions((short) 2);
        recipe.setImageLink("images/image.png");
        recipe.setSource("http://bestrecipes.com/best-recipe");
        recipe.setOwnership(new Ownership(OwnershipName.ADMIN));
        recipe.setCookingMethod(cookingMethod);
        cookingMethodRepository.save(cookingMethod);
        recipeRepository.save(recipe);
        List<Recipe> recipes = new ArrayList<>();
        recipes.add(recipe);
        when(recipeRepository.findAllByCookingMethod(cookingMethod)).thenReturn(recipes);
        when(cookingMethodRepository.findById(cookingMethod.getId())).thenReturn(Optional.of(cookingMethod));
        List<String> list = cookingMethodService.checkConnectedElements(cookingMethod.getId());
        assertThat(list.size()).isEqualTo(1);
    }
}
