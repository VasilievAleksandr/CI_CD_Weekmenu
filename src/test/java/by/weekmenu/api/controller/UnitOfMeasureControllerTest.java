package by.weekmenu.api.controller;

import by.weekmenu.api.dto.UnitOfMeasureDTO;
import by.weekmenu.api.entity.UnitOfMeasure;
import by.weekmenu.api.repository.OwnershipRepository;
import by.weekmenu.api.repository.UnitOfMeasureRepository;
import by.weekmenu.api.service.UnitOfMeasureService;
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
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(UnitOfMeasureController.class)
public class UnitOfMeasureControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    UnitOfMeasureService unitOfMeasureService;

    @MockBean
    private OwnershipRepository ownershipRepository;

    @MockBean
    private UnitOfMeasureRepository unitOfMeasureRepository;

    private UnitOfMeasureDTO createUnitOfMeasureDTO(Long id, String shortName, String fullName) {
        UnitOfMeasureDTO unitOfMeasureDTO = new UnitOfMeasureDTO();
        unitOfMeasureDTO.setId(id);
        unitOfMeasureDTO.setShortName(shortName);
        unitOfMeasureDTO.setFullName(fullName);
        return unitOfMeasureDTO;
    }

    @Test
    public void saveUnitOfMeasureTest() throws Exception {
        UnitOfMeasureDTO unitOfMeasureDTO = createUnitOfMeasureDTO(1L, "л", "Литр");
        when(unitOfMeasureService.save(any(UnitOfMeasureDTO.class))).thenReturn(unitOfMeasureDTO);
        ObjectMapper objectMapper = new ObjectMapper();
        mockMvc.perform(post(UrlConsts.PATH_UOM)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(unitOfMeasureDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.shortName", is("л")))
                .andExpect(jsonPath("$.fullName", is("Литр")));
    }

    @Test
    public void findAllUnitOfMeasuresTest() throws Exception {
        List unitOfMeasureDTOs = new ArrayList();
        unitOfMeasureDTOs.add(createUnitOfMeasureDTO(1L, "л", "Литр"));
        unitOfMeasureDTOs.add(createUnitOfMeasureDTO(2L, "кг", "Килограмм"));
        when(unitOfMeasureService.findAll()).thenReturn(unitOfMeasureDTOs);
        mockMvc.perform(get(UrlConsts.PATH_UOM)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].shortName", is("л")))
                .andExpect(jsonPath("$[0].fullName", is("Литр")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].shortName", is("кг")))
                .andExpect(jsonPath("$[1].fullName", is("Килограмм")))
                .andDo(print());
    }

    @Test
    public void updateUnitOfMeasureTest() throws Exception {
        UnitOfMeasureDTO unitOfMeasureDTO = createUnitOfMeasureDTO(1L, "л", "Литр");
        when(unitOfMeasureService.findById(unitOfMeasureDTO.getId())).thenReturn(unitOfMeasureDTO);
        unitOfMeasureDTO.setShortName("Л");
        when(unitOfMeasureService.save(any(UnitOfMeasureDTO.class))).thenReturn(unitOfMeasureDTO);
        ObjectMapper objectMapper = new ObjectMapper();
        mockMvc.perform(put(UrlConsts.PATH_UOM + "/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(unitOfMeasureDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.shortName", is("Л")));
    }

    @Test
    public void deleteUnitOfMeasureTest() throws Exception {
        UnitOfMeasureDTO unitOfMeasureDTO = createUnitOfMeasureDTO(1L, "л", "Литр");
        when(unitOfMeasureService.findById(unitOfMeasureDTO.getId())).thenReturn(unitOfMeasureDTO);
        mockMvc.perform(delete(UrlConsts.PATH_UOM + "/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    public void checkUniqueFullNameUnitOfMeasureTest() throws Exception {
        UnitOfMeasure unitOfMeasure = new UnitOfMeasure(1L, "л", "Литр");
        String name = "Литр";
        when(unitOfMeasureService.findByFullName(name)).thenReturn(unitOfMeasure);
        mockMvc.perform(get(UrlConsts.PATH_UOM + "/checkUniqueFullName?fullName=" + name)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(String.valueOf(-1)));
    }

    @Test
    public void checkUniqueShortNameUnitOfMeasureTest() throws Exception {
        UnitOfMeasure unitOfMeasure = new UnitOfMeasure(1L, "л", "Литр");
        String name = "л";
        when(unitOfMeasureService.findByShortName(name)).thenReturn(unitOfMeasure);
        mockMvc.perform(get(UrlConsts.PATH_UOM + "/checkUniqueShortName?shortName=" + name)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(String.valueOf(-1)));
    }

    @Test
    public void checkConnectedElementsTest() throws Exception {
        List<String> result = Collections.singletonList("ингредиенты: 1");
        when(unitOfMeasureService.checkConnectedElements(1L)).thenReturn(result);
        mockMvc.perform(get(UrlConsts.PATH_UOM + "/checkConnectedElements/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }
}
