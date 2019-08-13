package by.weekmenu.api.service;

import by.weekmenu.api.dto.RecipeSubcategoryDTO;
import by.weekmenu.api.entity.RecipeSubcategory;
import by.weekmenu.api.entity.RecycleBin;
import by.weekmenu.api.repository.RecipeSubcategoryRepository;
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
public class RecipeSubcategoryServiceImplTest {

    @MockBean
    private RecipeSubcategoryRepository recipeSubcategoryRepository;

    @MockBean
    private RecycleBinRepository recycleBinRepository;

    @MockBean
    private ModelMapper modelMapper;

    private RecipeSubcategoryService recipeSubcategoryService;

    @Before
    public void setup() {
        recipeSubcategoryService = new RecipeSubcategoryServiceImpl(recipeSubcategoryRepository,
                recycleBinRepository, modelMapper);
    }

    @Test
    public void getAllRecipeSubcategoryTest() {
        List<RecipeSubcategory> recipeSubcategories = new ArrayList<>();
        recipeSubcategories.add(new RecipeSubcategory(1L, "Курица"));
        recipeSubcategories.add(new RecipeSubcategory(2L, "Рыба"));
        when(recipeSubcategoryRepository.findAllByIsArchivedIsFalse()).thenReturn(recipeSubcategories);
        List<RecipeSubcategoryDTO> result = recipeSubcategoryService.findAll();
        assertThat(recipeSubcategories.size()).isEqualTo(result.size());
    }

    @Test
    public void saveRecipeSubcategoryTest() {
        RecipeSubcategoryDTO recipeSubcategoryDTO = new RecipeSubcategoryDTO();
        recipeSubcategoryDTO.setId(1L);
        recipeSubcategoryDTO.setName("Курица");
        RecipeSubcategory recipeSubcategory = new RecipeSubcategory(1L, "Курица");
        when(modelMapper.map(recipeSubcategoryDTO, RecipeSubcategory.class)).thenReturn(recipeSubcategory);
        when(recipeSubcategoryRepository.save(recipeSubcategory)).thenReturn(recipeSubcategory);
        when(modelMapper.map(recipeSubcategory, RecipeSubcategoryDTO.class)).thenReturn(recipeSubcategoryDTO);
        RecipeSubcategoryDTO saved = recipeSubcategoryService.save(recipeSubcategoryDTO);
        assertThat(saved).isNotNull();
        assertThat(saved.getName()).isEqualTo("Курица");
    }

    @Test
    public void findRecipeSubcategoryByIdTest() {
        RecipeSubcategoryDTO recipeSubcategoryDTO = new RecipeSubcategoryDTO();
        recipeSubcategoryDTO.setId(1L);
        recipeSubcategoryDTO.setName("Курица");
        RecipeSubcategory recipeSubcategory = new RecipeSubcategory(1L, "Курица");
        when(modelMapper.map(recipeSubcategory, RecipeSubcategoryDTO.class)).thenReturn(recipeSubcategoryDTO);
        when(recipeSubcategoryRepository.findById(recipeSubcategoryDTO.getId())).thenReturn(Optional.of(recipeSubcategory));
        RecipeSubcategoryDTO found = recipeSubcategoryService.findById(recipeSubcategoryDTO.getId());
        assertThat(found).isNotNull();
        assertThat(found.getName()).isEqualTo("Курица");
    }

    @Test
    public void deleteRecipeSubcategoryTest() {
        recipeSubcategoryService.delete(1L);
        verify(recipeSubcategoryRepository, times(1)).deleteById(1L);
    }

    @Test
    public void checkUniqueRecipeSubcategoryNameTest() {
        when(recipeSubcategoryRepository.findByNameIgnoreCase(anyString())).thenReturn(Optional.empty());
        RecipeSubcategory recipeSubcategory = recipeSubcategoryService.findByName("Курица");
        assertThat(recipeSubcategory).isNull();
    }

    @Test
    public void moveToRecycleBinTest() {
        RecipeSubcategoryDTO recipeSubcategoryDTO = new RecipeSubcategoryDTO();
        recipeSubcategoryDTO.setId(1L);
        recipeSubcategoryDTO.setName("Курица");
        when(recycleBinRepository.save(any(RecycleBin.class))).thenReturn(new RecycleBin());
        recipeSubcategoryService.moveToRecycleBin(recipeSubcategoryDTO);
        verify(recipeSubcategoryRepository, times(1)).softDelete(recipeSubcategoryDTO.getId());
    }

    @Test
    public void checkConnectedElementsTest() {
        RecipeSubcategory recipeSubcategory = new RecipeSubcategory(1L, "Курица");
//        TODO check connected recipes
        List<String> list = recipeSubcategoryService.checkConnectedElements(recipeSubcategory.getId());
        assertThat(list.size()).isEqualTo(0);
    }
}
