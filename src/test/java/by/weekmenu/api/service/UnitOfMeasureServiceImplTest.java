package by.weekmenu.api.service;


import by.weekmenu.api.dto.UnitOfMeasureDTO;
import by.weekmenu.api.entity.*;
import by.weekmenu.api.repository.*;
import by.weekmenu.api.utils.EntityNamesConsts;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
public class UnitOfMeasureServiceImplTest {

    @MockBean
    private UnitOfMeasureRepository unitOfMeasureRepository;

    @MockBean
    private RecycleBinRepository recycleBinRepository;

    @MockBean
    private IngredientUnitOfMeasureRepository ingredientUnitOfMeasureRepository;

    @MockBean
    private IngredientPriceRepository ingredientPriceRepository;

    @MockBean
    private RecipeIngredientRepository recipeIngredientRepository;

    @MockBean
    private ModelMapper modelMapper;

    private UnitOfMeasureService unitOfMeasureService;

    @Before
    public void setup() {

        unitOfMeasureService = new UnitOfMeasureServiceImp(unitOfMeasureRepository, recycleBinRepository,
                ingredientUnitOfMeasureRepository, ingredientPriceRepository, recipeIngredientRepository, modelMapper);
    }

    @Test
    public void findAllUnitOfMeasureTest() {
        List<UnitOfMeasure> unitOfMeasures = new ArrayList<>();
        unitOfMeasures.add(new UnitOfMeasure("л", "Литр"));
        unitOfMeasures.add(new UnitOfMeasure("гр", "Грамм"));
        when(unitOfMeasureRepository.findAllByIsArchivedIsFalse()).thenReturn(unitOfMeasures);
        List<UnitOfMeasureDTO> result = unitOfMeasureService.findAll();
        assertThat(unitOfMeasures.size()).isEqualTo(result.size());
    }

    @Test
    public void saveUnitOfMeasureTest() {
        UnitOfMeasureDTO unitOfMeasureDTO = new UnitOfMeasureDTO();
        unitOfMeasureDTO.setId(1L);
        unitOfMeasureDTO.setFullName("Литр");
        unitOfMeasureDTO.setShortName("л");
        UnitOfMeasure unitOfMeasure = new UnitOfMeasure("л", "Литр");
        when(modelMapper.map(unitOfMeasureDTO, UnitOfMeasure.class)).thenReturn(unitOfMeasure);
        when(unitOfMeasureRepository.save(unitOfMeasure)).thenReturn(unitOfMeasure);
        when(modelMapper.map(unitOfMeasure, UnitOfMeasureDTO.class)).thenReturn(unitOfMeasureDTO);
        UnitOfMeasureDTO saved = unitOfMeasureService.save(unitOfMeasureDTO);
        assertThat(saved).isNotNull();
        assertThat(saved.getFullName()).isEqualTo("Литр");
    }

    @Test
    public void findUnitOfMeasureByIdTest() {
        UnitOfMeasureDTO unitOfMeasureDTO = new UnitOfMeasureDTO();
        unitOfMeasureDTO.setId(1L);
        unitOfMeasureDTO.setFullName("Литр");
        unitOfMeasureDTO.setShortName("л");
        UnitOfMeasure unitOfMeasure = new UnitOfMeasure("л", "Литр");
        when(modelMapper.map(unitOfMeasure, UnitOfMeasureDTO.class)).thenReturn(unitOfMeasureDTO);
        when(unitOfMeasureRepository.findById(unitOfMeasureDTO.getId())).thenReturn(Optional.of(unitOfMeasure));
        UnitOfMeasureDTO found = unitOfMeasureService.findById(unitOfMeasureDTO.getId());
        assertThat(found).isNotNull();
        assertThat(found.getFullName()).isEqualTo("Литр");
    }

    @Test
    public void deleteUnitOfMeasureTest() {
       UnitOfMeasureDTO unitOfMeasureDTO = new UnitOfMeasureDTO();
        unitOfMeasureDTO.setId(1L);
        unitOfMeasureDTO.setFullName("Литр");
        unitOfMeasureDTO.setShortName("л");
        RecycleBin recycleBin = new RecycleBin();
        recycleBin.setElementName(unitOfMeasureDTO.getFullName());
        recycleBin.setEntityName(EntityNamesConsts.UNIT_OF_MEASURE);
        recycleBin.setDeleteDate(LocalDateTime.now());
        when(recycleBinRepository.save(recycleBin)).thenReturn(recycleBin);
        unitOfMeasureService.moveToRecycleBin(unitOfMeasureDTO);
        verify(unitOfMeasureRepository, times(1)).softDelete(1L);
        assertThat(recycleBin.getElementName()).isEqualTo("Литр");
        assertThat(recycleBin.getEntityName()).isEqualTo("Единица измерения");
        assertThat(recycleBin.getDeleteDate()).isNotNull();
    }

    @Test
    public void checkUniqueUnitOfMeasureFullNameTest() {
        when(unitOfMeasureRepository.findByFullNameIgnoreCase(anyString())).thenReturn(Optional.empty());
        UnitOfMeasure unitOfMeasure = unitOfMeasureService.findByFullName("Литр");
        assertThat(unitOfMeasure).isNull();
    }

    @Test
    public void checkUniqueUnitOfMeasureShortNameTest() {
        when(unitOfMeasureRepository.findByShortNameIgnoreCase(anyString())).thenReturn(Optional.empty());
        UnitOfMeasure unitOfMeasure = unitOfMeasureService.findByShortName("л");
        assertThat(unitOfMeasure).isNull();
    }

    @Test
    public void moveToRecycleBinTest() {
        UnitOfMeasureDTO unitOfMeasureDTO = new UnitOfMeasureDTO();
        unitOfMeasureDTO.setId(1L);
        unitOfMeasureDTO.setFullName("Литр");
        unitOfMeasureDTO.setShortName("л");
        when(recycleBinRepository.save(any(RecycleBin.class))).thenReturn(new RecycleBin());
        unitOfMeasureService.moveToRecycleBin(unitOfMeasureDTO);
        verify(unitOfMeasureRepository, times(1)).softDelete(unitOfMeasureDTO.getId());
    }

    @Test
    public void checkConnectedElementsTest() {
        UnitOfMeasure unitOfMeasure = new UnitOfMeasure(1L, "л", "Литр");
        Ingredient ingredient = new Ingredient();
        ingredient.setName("Курица");
        ingredient.setCalories(new BigDecimal("100"));
        ingredient.setCarbs(new BigDecimal("100"));
        ingredient.setFats(new BigDecimal("100"));
        ingredient.setProteins(new BigDecimal("100"));
        ingredient.setOwnership(new Ownership());
        ingredient.setId(1L);
        IngredientUnitOfMeasure ingredientUnitOfMeasure = new IngredientUnitOfMeasure(new BigDecimal(3));
        ingredientUnitOfMeasure.setId(new IngredientUnitOfMeasure.Id(ingredient.getId(), unitOfMeasure.getId()));
        List<IngredientUnitOfMeasure> list = new ArrayList<>();
        list.add(ingredientUnitOfMeasure);
        when(ingredientUnitOfMeasureRepository.findAllById_UnitOfMeasureId(unitOfMeasure.getId())).thenReturn(list);
        List<String> result = unitOfMeasureService.checkConnectedElements(unitOfMeasure.getId());
        assertThat(result.size()).isEqualTo(1);
    }
}
