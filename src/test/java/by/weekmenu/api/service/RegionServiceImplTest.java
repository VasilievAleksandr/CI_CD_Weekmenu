package by.weekmenu.api.service;

import by.weekmenu.api.dto.RegionDTO;
import by.weekmenu.api.entity.*;
import by.weekmenu.api.repository.*;
import by.weekmenu.api.utils.EntityNamesConsts;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
public class RegionServiceImplTest {

    @MockBean
    private RegionRepository regionRepository;

    @MockBean
    private CountryRepository countryRepository;

    @MockBean
    private RecycleBinRepository recycleBinRepository;

    @MockBean
    private IngredientPriceRepository ingredientPriceRepository;

    @MockBean
    private RecipePriceRepository recipePriceRepository;

    @MockBean
    private ModelMapper modelMapper;

    private RegionService regionService;

    @Mock
    private Region regionOne;

    @Mock
    private Region regionTwo;

    @Before
    public void setup() {
        regionService = new RegionServiceImpl(regionRepository, countryRepository, recycleBinRepository,
                ingredientPriceRepository, recipePriceRepository, modelMapper);
    }

    private Region createRegion() {
        Region region = new Region();
        region.setId(1L);
        region.setName("Минск");
        region.setCountry(new Country("Беларусь", "BY",
                new Currency("Бел. руб.", "BYN", false)));
        region.setArchived(false);
        return region;
    }

    private RegionDTO createRegionDto() {
        RegionDTO regionDto = new RegionDTO();
        regionDto.setId(1L);
        regionDto.setName("Минск");
        regionDto.setCountryName("Беларусь");
        return regionDto;
    }

    @Test
    public void getAllRegionsTest() {
        List<Region> regions = new ArrayList<>();
        regions.add(regionOne);
        regions.add(regionTwo);
        when(regionRepository.findAllByIsArchivedIsFalse()).thenReturn(regions);
        List<RegionDTO> result = regionService.findAll();
        assertThat(regions.size()).isEqualTo(result.size());
    }

    @Test
    public void saveRegionTest() {
        RegionDTO regionDto = createRegionDto();
        Region region = createRegion();
        when(modelMapper.map(regionDto, Region.class)).thenReturn(region);
        when(regionRepository.save(region)).thenReturn(region);
        when(modelMapper.map(region, RegionDTO.class)).thenReturn(regionDto);
        RegionDTO saved = regionService.save(regionDto);
        assertThat(saved).isNotNull();
        assertThat(saved.getName()).isEqualTo("Минск");
        assertThat(saved.getCountryName()).isEqualTo("Беларусь");
    }

    @Test
    public void findRegionByIdTest() {
        RegionDTO regionDto = createRegionDto();
        Region region = createRegion();
        when(modelMapper.map(region, RegionDTO.class)).thenReturn(regionDto);
        when(regionRepository.findById(regionDto.getId())).thenReturn(Optional.of(region));
        RegionDTO found = regionService.findById(regionDto.getId());
        assertThat(found).isNotNull();
        assertThat(found.getName()).isEqualTo("Минск");
        assertThat(found.getCountryName()).isEqualTo("Беларусь");
    }

    @Test
    public void deleteRegionTest() {
        RegionDTO regionDTO = createRegionDto();
        RecycleBin recycleBin = new RecycleBin();
        recycleBin.setElementName(regionDTO.getName());
        recycleBin.setEntityName(EntityNamesConsts.REGION);
        recycleBin.setDeleteDate(LocalDateTime.now());
        when(recycleBinRepository.save(recycleBin)).thenReturn(recycleBin);
        regionService.moveToRecycleBin(regionDTO);
        verify(regionRepository, times(1)).softDelete(1L);
        assertThat(recycleBin.getElementName()).isEqualTo("Минск");
        assertThat(recycleBin.getEntityName()).isEqualTo("Регион");
        assertThat(recycleBin.getDeleteDate()).isNotNull();
    }

    @Test
    public void checkUniqueNameTest() {
        when(regionRepository.findByNameIgnoreCase(anyString())).thenReturn(Optional.empty());
        RegionDTO regionDto = regionService.findByName("Минск");
        assertThat(regionDto).isNull();
    }

    @Test
    public void checkConnectedElementsTest() {
        Region region = createRegion();
        List<IngredientPrice> ingredientPrices = new ArrayList<>();
        ingredientPrices.add(new IngredientPrice(new Ingredient("Курица", new Ownership(OwnershipName.USER)),
                region, new UnitOfMeasure("Кг", "Килограмм"),
                new BigDecimal("1"), new BigDecimal("123.12")));
        List<RecipePrice> recipePrices = new ArrayList<>();
        recipePrices.add(new RecipePrice(new BigDecimal("111"),
                new Recipe("рецепт", true, new CookingMethod("жарка"),
                        new Ownership(OwnershipName.USER)), region));
        when(ingredientPriceRepository.findAllById_RegionId(region.getId())).thenReturn(ingredientPrices);
        when(recipePriceRepository.findAllById_RegionId(region.getId())).thenReturn(recipePrices);
        List<String> list = regionService.checkConnectedElements(region.getId());
        assertThat(list.size()).isEqualTo(2);
    }
}
