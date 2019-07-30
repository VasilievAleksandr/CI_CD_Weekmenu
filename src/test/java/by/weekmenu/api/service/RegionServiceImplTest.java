package by.weekmenu.api.service;

import by.weekmenu.api.dto.RegionDTO;
import by.weekmenu.api.entity.Country;
import by.weekmenu.api.entity.Currency;
import by.weekmenu.api.entity.Region;
import by.weekmenu.api.repository.CountryRepository;
import by.weekmenu.api.repository.RegionRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

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
    private ModelMapper modelMapper;

    private RegionService regionService;

    @Mock
    private Region regionOne;

    @Mock
    private Region regionTwo;

    @Before
    public void setup() {
        regionService = new RegionServiceImpl(regionRepository, countryRepository, modelMapper);
    }

    private Region createRegion() {
        Region region = new Region();
        region.setId(1L);
        region.setName("Минск");
        region.setCountry(new Country("Беларусь", "BY",
                new Currency("Бел. руб.", "BYN", true)));
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
        when(regionRepository.findAll()).thenReturn(regions);

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
        regionService.delete(1L);
        verify(regionRepository, times(1)).deleteById(1L);
    }

    @Test
    public void checkUniqueNameTest() {
        when(regionRepository.findByNameIgnoreCase(anyString())).thenReturn(Optional.empty());
        RegionDTO regionDto = regionService.findByName("Минск");
        assertThat(regionDto).isNull();
    }
}
