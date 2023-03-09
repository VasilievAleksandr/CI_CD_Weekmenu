package by.weekmenu.api.controller;

import by.weekmenu.api.dto.MealTypeDTO;
import by.weekmenu.api.entity.MealType;
import by.weekmenu.api.repository.MealTypeRepository;
import by.weekmenu.api.repository.OwnershipRepository;
import by.weekmenu.api.repository.UnitOfMeasureRepository;
import by.weekmenu.api.service.MealTypeService;
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
@WebMvcTest(MealTypeController.class)
public class MealTypeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    MealTypeService mealTypeService;

    @MockBean
    private OwnershipRepository ownershipRepository;

    @MockBean
    private UnitOfMeasureRepository unitOfMeasureRepository;

    @MockBean
    private MealTypeRepository mealTypeRepository;

    private MealTypeDTO createMealTypeDTO(Short id, String name) {
        MealTypeDTO mealTypeDTO = new MealTypeDTO();
        mealTypeDTO.setId(id);
        mealTypeDTO.setName(name);
        return mealTypeDTO;
    }

    @Test
    public void saveMealTypeTest() throws Exception {
        MealTypeDTO mealTypeDTO = createMealTypeDTO(new Short ("1"), "Завтрак");
        when(mealTypeService.save(any(MealTypeDTO.class))).thenReturn(mealTypeDTO);
        ObjectMapper objectMapper = new ObjectMapper();
        mockMvc.perform(post(UrlConsts.PATH_MEAL_TYPES)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(mealTypeDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Завтрак")));
    }

    @Test
    public void findAllMealTypeTest() throws Exception {
        List mealTypeDTOs = new ArrayList();
        mealTypeDTOs.add(createMealTypeDTO(new Short ("1"), "Завтрак"));
        mealTypeDTOs.add(createMealTypeDTO(new Short ("2"), "Обед"));
        when(mealTypeService.findAll()).thenReturn(mealTypeDTOs);
        mockMvc.perform(get(UrlConsts.PATH_MEAL_TYPES)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("Завтрак")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].name", is("Обед")))
                .andDo(print());
    }

    @Test
    public void updateMealTypeTest() throws Exception {
        MealTypeDTO mealTypeDTO = createMealTypeDTO(new Short ("1"), "Завтрак");
        when(mealTypeService.findById(mealTypeDTO.getId())).thenReturn(mealTypeDTO);
        mealTypeDTO.setName("Обед");
        when(mealTypeService.save(any(MealTypeDTO.class))).thenReturn(mealTypeDTO);
        ObjectMapper objectMapper = new ObjectMapper();
        mockMvc.perform(put(UrlConsts.PATH_MEAL_TYPES + "/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(mealTypeDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Обед")));
    }

    @Test
    public void deleteMealTypeTest() throws Exception {
        MealTypeDTO mealTypeDTO = createMealTypeDTO(new Short ("1"), "Завтрак");
        when(mealTypeService.findById(mealTypeDTO.getId())).thenReturn(mealTypeDTO);
        mockMvc.perform(delete(UrlConsts.PATH_MEAL_TYPES + "/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    public void checkUniqueNameMealTypeTest() throws Exception {
        MealType mealType = new MealType(new Short("1"),"Завтрак", 5,false);
        String name = "Завтрак";
        when(mealTypeService.findByName(name)).thenReturn(mealType);
        mockMvc.perform(get(UrlConsts.PATH_MEAL_TYPES + "/checkMealTypeUniqueName?name=" + name)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(String.valueOf(-1)));
    }

    @Test
    public void checkConnectedElementsTest() throws Exception {
        List<String> result = Collections.singletonList("рецепты меню: 1");
        when(mealTypeService.checkConnectedElements(new Short("1"))).thenReturn(result);
        mockMvc.perform(get(UrlConsts.PATH_MEAL_TYPES + "/checkConnectedElements/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }
}
