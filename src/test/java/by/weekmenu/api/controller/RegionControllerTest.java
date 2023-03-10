package by.weekmenu.api.controller;

import by.weekmenu.api.dto.RegionDTO;
import by.weekmenu.api.repository.MealTypeRepository;
import by.weekmenu.api.repository.OwnershipRepository;
import by.weekmenu.api.repository.UnitOfMeasureRepository;
import by.weekmenu.api.service.RegionService;
import by.weekmenu.api.utils.UrlConsts;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(RegionController.class)
public class RegionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RegionService regionService;

    @MockBean
    private OwnershipRepository ownershipRepository;

    @MockBean
    private UnitOfMeasureRepository unitOfMeasureRepository;

    @MockBean
    private MealTypeRepository mealTypeRepository;

    @Test
    public void getAllRegionsTest() throws Exception {
        List<RegionDTO> regions = new ArrayList<>();
        regions.add(new RegionDTO(1L, "Минск", "Беларусь", "BYN"));
        regions.add(new RegionDTO(2L, "Гродно", "Беларусь", "BYN"));
        when(regionService.findAll()).thenReturn(regions);

        mockMvc.perform(get(UrlConsts.PATH_REGIONS)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("Минск")))
                .andExpect(jsonPath("$[0].countryName", is("Беларусь")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].name", is("Гродно")))
                .andExpect(jsonPath("$[1].countryName", is("Беларусь")))
                .andDo(print());
    }

    @Test
    public void saveRegionTest() throws Exception {
        RegionDTO regionDto = new RegionDTO(1L, "Минск", "Беларусь", "BYN");
        when(regionService.save(any(RegionDTO.class))).thenReturn(regionDto);

        ObjectMapper objectMapper = new ObjectMapper();

        mockMvc.perform(post(UrlConsts.PATH_REGIONS)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(regionDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Минск")))
                .andExpect(jsonPath("$.countryName", is("Беларусь")))
                .andDo(print());
    }

    @Test
    public void findRegionByIdTest() throws Exception {
        RegionDTO regionDto = new RegionDTO(1L, "Минск", "Беларусь", "BYN");
        when(regionService.findById(regionDto.getId())).thenReturn(regionDto);

        mockMvc.perform(get(UrlConsts.PATH_REGIONS + "/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Минск")))
                .andExpect(jsonPath("$.countryName", is("Беларусь")))
                .andDo(print());
    }

    @Test
    public void updateRegionTest() throws Exception {
        RegionDTO regionDto = new RegionDTO(1L, "Минск", "Беларусь", "BYN");
        when(regionService.findById(regionDto.getId())).thenReturn(regionDto);
        regionDto.setName("Гродно");
        when(regionService.save(any(RegionDTO.class))).thenReturn(regionDto);
        ObjectMapper objectMapper = new ObjectMapper();
        mockMvc.perform(put(UrlConsts.PATH_REGIONS + "/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(regionDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Гродно")))
                .andExpect(jsonPath("$.countryName", is("Беларусь")))
                .andDo(print());
    }

    @Test
    public void deleteRegionTest() throws Exception {
        RegionDTO regionDTO = new RegionDTO(1L, "Минск", "Беларусь", "BYN");
        when(regionService.findById(regionDTO.getId())).thenReturn(regionDTO);
        mockMvc.perform(delete(UrlConsts.PATH_REGIONS + "/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    public void checkUniqueNameTest() throws Exception {
        String name = "Минск";
        when(regionService.findByName(name)).thenReturn(null);
        mockMvc.perform(get(UrlConsts.PATH_REGIONS + "/checkUniqueName?name=" + name)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(String.valueOf(0)));
    }

    @Test
    public void checkConnectedElementsTest() throws Exception {
        List<String> result = Arrays.asList("цены ингредиентов: 1", "цены рецептов: 2");
        when(regionService.checkConnectedElements(1L)).thenReturn(result);
        mockMvc.perform(get(UrlConsts.PATH_REGIONS + "/checkConnectedElements/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }
}
