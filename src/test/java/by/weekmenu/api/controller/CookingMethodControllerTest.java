package by.weekmenu.api.controller;

import by.weekmenu.api.dto.CookingMethodDTO;
import by.weekmenu.api.entity.CookingMethod;
import by.weekmenu.api.repository.MealTypeRepository;
import by.weekmenu.api.repository.OwnershipRepository;
import by.weekmenu.api.repository.UnitOfMeasureRepository;
import by.weekmenu.api.service.CookingMethodService;
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
@WebMvcTest(CookingMethodController.class)
public class CookingMethodControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CookingMethodService cookingMethodService;

    @MockBean
    private OwnershipRepository ownershipRepository;

    @MockBean
    private UnitOfMeasureRepository unitOfMeasureRepository;

    @MockBean
    private MealTypeRepository mealTypeRepository;

    private CookingMethodDTO createCookingMethodDTO(Integer id, String name) {
        CookingMethodDTO cookingMethodDTO = new CookingMethodDTO();
        cookingMethodDTO.setId(id);
        cookingMethodDTO.setName(name);
        return cookingMethodDTO;
    }

    @Test
    public void saveCookingMethodTest() throws Exception {
        CookingMethodDTO cookingMethodDTO = createCookingMethodDTO(1, "Жарка");
        when(cookingMethodService.save(any(CookingMethodDTO.class))).thenReturn(cookingMethodDTO);
        ObjectMapper objectMapper = new ObjectMapper();
        mockMvc.perform(post(UrlConsts.PATH_COOKINGMETHODS)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(cookingMethodDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Жарка")));
    }

    @Test
    public void findAllCookingMethodsTest() throws Exception {
        List cookingMethodDTOs = new ArrayList();
        cookingMethodDTOs.add(createCookingMethodDTO(1, "Жарка"));
        cookingMethodDTOs.add(createCookingMethodDTO(2, "Варка"));
        when(cookingMethodService.findAll()).thenReturn(cookingMethodDTOs);
        mockMvc.perform(get(UrlConsts.PATH_COOKINGMETHODS)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("Жарка")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].name", is("Варка")))
                .andDo(print());
    }

    @Test
    public void updateCookingMethodTest() throws Exception {
        CookingMethodDTO cookingMethodDTO = createCookingMethodDTO(1, "Жарка");
        when(cookingMethodService.findById(cookingMethodDTO.getId())).thenReturn(cookingMethodDTO);
        cookingMethodDTO.setName("Варка");
        when(cookingMethodService.save(any(CookingMethodDTO.class))).thenReturn(cookingMethodDTO);
        ObjectMapper objectMapper = new ObjectMapper();
        mockMvc.perform(put(UrlConsts.PATH_COOKINGMETHODS + "/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(cookingMethodDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Варка")));
    }

    @Test
    public void deleteCookingMethodTest() throws Exception {
        CookingMethodDTO cookingMethodDTO = createCookingMethodDTO(1, "Жарка");
        when(cookingMethodService.findById(cookingMethodDTO.getId())).thenReturn(cookingMethodDTO);
        mockMvc.perform(delete(UrlConsts.PATH_COOKINGMETHODS + "/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    public void checkConnectedElementsTest() throws Exception {
        List<String> result = Collections.singletonList("рецепты: 1");
        when(cookingMethodService.checkConnectedElements(1)).thenReturn(result);
        mockMvc.perform(get(UrlConsts.PATH_COOKINGMETHODS + "/checkConnectedElements/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }
}
