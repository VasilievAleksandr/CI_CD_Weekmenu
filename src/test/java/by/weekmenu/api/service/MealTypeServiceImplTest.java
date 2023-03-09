package by.weekmenu.api.service;

import by.weekmenu.api.dto.MealTypeDTO;
import by.weekmenu.api.entity.*;
import by.weekmenu.api.repository.MealTypeRepository;
import by.weekmenu.api.repository.MenuRecipeRepository;
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
public class MealTypeServiceImplTest {

    @MockBean
    private MealTypeRepository mealTypeRepository;

    @MockBean
    private RecycleBinRepository recycleBinRepository;

    @MockBean
    private MenuRecipeRepository menuRecipeRepository;

    @MockBean
    private ModelMapper modelMapper;

    private MealTypeServiceImpl mealTypeService;

    @Before
    public void setup() {
        mealTypeService = new MealTypeServiceImpl(mealTypeRepository, recycleBinRepository,
                menuRecipeRepository, modelMapper);
    }

    @Test
    public void getAllMealTypeTest() {
        List<MealType> mealTypes = new ArrayList<>();
        mealTypes.add(new MealType(new Short("1"), "Завтрак", 5, false));
        mealTypes.add(new MealType(new Short("1"), "Обед", 5, false));
        when(mealTypeRepository.findAllByIsArchivedIsFalse()).thenReturn(mealTypes);
        List<MealTypeDTO> result = mealTypeService.findAll();
        assertThat(mealTypes.size()).isEqualTo(result.size());
    }

    @Test
    public void saveMealTypeTest() {
        MealTypeDTO mealTypeDTO = new MealTypeDTO();
        mealTypeDTO.setId(new Short("1"));
        mealTypeDTO.setName("Завтрак");
        MealType mealType = new MealType(new Short("1"), "Завтрак", 5, false);
        when(modelMapper.map(mealTypeDTO, MealType.class)).thenReturn(mealType);
        when(mealTypeRepository.save(mealType)).thenReturn(mealType);
        when(modelMapper.map(mealType, MealTypeDTO.class)).thenReturn(mealTypeDTO);
        MealTypeDTO saved = mealTypeService.save(mealTypeDTO);
        assertThat(saved).isNotNull();
        assertThat(saved.getName()).isEqualTo("Завтрак");
    }

    @Test
    public void findMealTypeByIdTest() {
        MealTypeDTO mealTypeDTO = new MealTypeDTO();
        mealTypeDTO.setId(new Short("1"));
        mealTypeDTO.setName("Завтрак");
        MealType mealType = new MealType(new Short("1"), "Завтрак", 5,false);
        when(modelMapper.map(mealType, MealTypeDTO.class)).thenReturn(mealTypeDTO);
        when(mealTypeRepository.findById(mealTypeDTO.getId())).thenReturn(Optional.of(mealType));
        MealTypeDTO found = mealTypeService.findById(mealTypeDTO.getId());
        assertThat(found).isNotNull();
        assertThat(found.getName()).isEqualTo("Завтрак");
    }

    @Test
    public void checkUniqueMealTypeNameTest() {
        when(mealTypeRepository.findByNameIgnoreCase(anyString())).thenReturn(Optional.empty());
        MealType mealType = mealTypeService.findByName("Завтрак");
        assertThat(mealType).isNull();
    }

    @Test
    public void moveToRecycleBinTest() {
        MealTypeDTO mealTypeDTO = new MealTypeDTO();
        mealTypeDTO.setId(new Short("1"));
        mealTypeDTO.setName("Завтрак");
        when(recycleBinRepository.save(any(RecycleBin.class))).thenReturn(new RecycleBin());
        mealTypeService.moveToRecycleBin(mealTypeDTO);
        verify(mealTypeRepository, times(1)).softDelete(mealTypeDTO.getId());
    }

    @Test
    public void checkConnectedElementsTest() {
        MealType mealType = new MealType(new Short("1"), "Завтрак", 5,false);
        MenuRecipe menuRecipe = new MenuRecipe();
        menuRecipe.setMealType(mealType);
        List<MenuRecipe> menuRecipes = new ArrayList();
        menuRecipes.add(menuRecipe);
        when(mealTypeRepository.findById(mealType.getId())).thenReturn(Optional.of(mealType));
        when(menuRecipeRepository.findAllByMealType_Id(mealType.getId())).thenReturn(menuRecipes);
        List<String> list = mealTypeService.checkConnectedElements(mealType.getId());
        assertThat(list.size()).isEqualTo(1);
    }
}
