package by.weekmenu.api.service;

import by.weekmenu.api.dto.RecipeCategoryDTO;
import by.weekmenu.api.entity.*;
import by.weekmenu.api.repository.RecipeCategoryRepository;
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
public class RecipeCategoryServiceImplTest {

    @MockBean
    private RecipeCategoryRepository recipeCategoryRepository;

    @MockBean
    private RecycleBinRepository recycleBinRepository;

    @MockBean
    private ModelMapper modelMapper;

    private RecipeCategoryService recipeCategoryService;

    @Before
    public void setup() {
        recipeCategoryService = new RecipeCategoryServiceImpl(recipeCategoryRepository, recycleBinRepository, modelMapper);
    }

    @Test
    public void getAllRecipeCategoryTest() {
        List<RecipeCategory> recipeCategories = new ArrayList<>();
        recipeCategories.add(new RecipeCategory(1L, "Обед"));
        recipeCategories.add(new RecipeCategory(2L, "Ужин"));
        when(recipeCategoryRepository.findAllByIsArchivedIsFalse()).thenReturn(recipeCategories);
        List<RecipeCategoryDTO> result = recipeCategoryService.findAll();
        assertThat(recipeCategories.size()).isEqualTo(result.size());
    }

    @Test
    public void saveRecipeCategoryTest() {
        RecipeCategoryDTO recipeCategoryDTO = new RecipeCategoryDTO();
        recipeCategoryDTO.setId(1L);
        recipeCategoryDTO.setName("Обед");
        RecipeCategory recipeCategory = new RecipeCategory(1L, "Обед");
        when(modelMapper.map(recipeCategoryDTO, RecipeCategory.class)).thenReturn(recipeCategory);
        when(recipeCategoryRepository.save(recipeCategory)).thenReturn(recipeCategory);
        when(modelMapper.map(recipeCategory, RecipeCategoryDTO.class)).thenReturn(recipeCategoryDTO);
        RecipeCategoryDTO saved = recipeCategoryService.save(recipeCategoryDTO);
        assertThat(saved).isNotNull();
        assertThat(saved.getName()).isEqualTo("Обед");
    }

    @Test
    public void findRecipeCategoryByIdTest() {
        RecipeCategoryDTO recipeCategoryDTO = new RecipeCategoryDTO();
        recipeCategoryDTO.setId(1L);
        recipeCategoryDTO.setName("Обед");
        RecipeCategory recipeCategory = new RecipeCategory(1L, "Обед");
        when(modelMapper.map(recipeCategory, RecipeCategoryDTO.class)).thenReturn(recipeCategoryDTO);
        when(recipeCategoryRepository.findById(recipeCategoryDTO.getId())).thenReturn(Optional.of(recipeCategory));
        RecipeCategoryDTO found = recipeCategoryService.findById(recipeCategoryDTO.getId());
        assertThat(found).isNotNull();
        assertThat(found.getName()).isEqualTo("Обед");
    }

    @Test
    public void deleteRecipeCategoryTest() {
        recipeCategoryService.delete(1L);
        verify(recipeCategoryRepository, times(1)).deleteById(1L);
    }

    @Test
    public void checkUniqueRecipeCategoryNameTest() {
        when(recipeCategoryRepository.findByNameIgnoreCase(anyString())).thenReturn(Optional.empty());
        RecipeCategory recipeCategory = recipeCategoryService.findByName("Обед");
        assertThat(recipeCategory).isNull();
    }

    @Test
    public void moveToRecycleBinTest() {
        RecipeCategoryDTO recipeCategoryDTO = new RecipeCategoryDTO();
        recipeCategoryDTO.setId(1L);
        recipeCategoryDTO.setName("Обед");
        when(recycleBinRepository.save(any(RecycleBin.class))).thenReturn(new RecycleBin());
        recipeCategoryService.moveToRecycleBin(recipeCategoryDTO);
        verify(recipeCategoryRepository, times(1)).softDelete(recipeCategoryDTO.getId());
    }

    @Test
    public void checkConnectedElementsTest() {
        RecipeCategory recipeCategory = new RecipeCategory(1L, "Обед");
//        TODO check connected recipes
        List<String> list = recipeCategoryService.checkConnectedElements(recipeCategory.getId());
        assertThat(list.size()).isEqualTo(0);
    }
}
