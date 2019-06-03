package by.weekmenu.api.controller;

import by.weekmenu.api.dto.RegionDto;
import by.weekmenu.api.service.CrudService;
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
    private CrudService<RegionDto, Long> regionService;

    @Test
    public void getAllRegionsTest() throws Exception {
        List<RegionDto> regions = new ArrayList<>();
        regions.add(new RegionDto(1L, "Минск", "Беларусь"));
        regions.add(new RegionDto(2L, "Гродно", "Беларусь"));
        when(regionService.findAll()).thenReturn(regions);

        mockMvc.perform(get("/regions")
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
        RegionDto regionDto = new RegionDto(1L, "Минск", "Беларусь");
        when(regionService.save(any(RegionDto.class))).thenReturn(regionDto);

        ObjectMapper objectMapper = new ObjectMapper();

        mockMvc.perform(post("/regions")
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
        RegionDto regionDto = new RegionDto(1L, "Минск", "Беларусь");
        when(regionService.findById(regionDto.getId())).thenReturn(regionDto);

        mockMvc.perform(get("/regions/1")
        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Минск")))
                .andExpect(jsonPath("$.countryName", is("Беларусь")))
                .andDo(print());
    }

    @Test
    public void updateRegionTest() throws Exception {
        RegionDto regionDto = new RegionDto(1L, "Минск", "Беларусь");
        when(regionService.findById(regionDto.getId())).thenReturn(regionDto);
        regionDto.setName("Гродно");
        when(regionService.save(any(RegionDto.class))).thenReturn(regionDto);
        ObjectMapper objectMapper = new ObjectMapper();
        mockMvc.perform(put("/regions/1")
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
        RegionDto regionDto = new RegionDto(1L, "Минск", "Беларусь");
        when(regionService.findById(regionDto.getId())).thenReturn(regionDto);
        mockMvc.perform(delete("/regions/1")
        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
