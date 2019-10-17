package by.weekmenu.api.controller;

import by.weekmenu.api.dto.MenuCategoryDTO;
import by.weekmenu.api.entity.MenuCategory;
import by.weekmenu.api.repository.MealTypeRepository;
import by.weekmenu.api.repository.OwnershipRepository;
import by.weekmenu.api.repository.UnitOfMeasureRepository;
import by.weekmenu.api.service.MenuCategoryService;
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
@WebMvcTest(MenuCategoryController.class)
public class MenuCategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    MenuCategoryService menuCategoryService;

    @MockBean
    private OwnershipRepository ownershipRepository;

    @MockBean
    private UnitOfMeasureRepository unitOfMeasureRepository;

    @MockBean
    private MealTypeRepository mealTypeRepository;

    private MenuCategoryDTO createMenuCategoryDTO(Integer id, String name) {
        MenuCategoryDTO menuCategoryDTO = new MenuCategoryDTO();
        menuCategoryDTO.setId(id);
        menuCategoryDTO.setName(name);
        return menuCategoryDTO;
    }

    @Test
    public void saveMenuCategoryTest() throws Exception {
        MenuCategoryDTO menuCategoryDTO = createMenuCategoryDTO(1, "Постное");
        when(menuCategoryService.save(any(MenuCategoryDTO.class))).thenReturn(menuCategoryDTO);
        ObjectMapper objectMapper = new ObjectMapper();
        mockMvc.perform(post(UrlConsts.PATH_MENU_CATEGORIES)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(menuCategoryDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Постное")));
    }

    @Test
    public void findAllMenuCategoriesTest() throws Exception {
        List menuCategoryDTOs = new ArrayList();
        menuCategoryDTOs.add(createMenuCategoryDTO(1, "Постное"));
        menuCategoryDTOs.add(createMenuCategoryDTO(2, "Диетическое"));
        when(menuCategoryService.findAll()).thenReturn(menuCategoryDTOs);
        mockMvc.perform(get(UrlConsts.PATH_MENU_CATEGORIES)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("Постное")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].name", is("Диетическое")))
                .andDo(print());
    }

    @Test
    public void updateMenuCategoryTest() throws Exception {
        MenuCategoryDTO menuCategoryDTO = createMenuCategoryDTO(1, "Постное");
        when(menuCategoryService.findById(menuCategoryDTO.getId())).thenReturn(menuCategoryDTO);
        menuCategoryDTO.setName("Диетическое");
        when(menuCategoryService.save(any(MenuCategoryDTO.class))).thenReturn(menuCategoryDTO);
        ObjectMapper objectMapper = new ObjectMapper();
        mockMvc.perform(put(UrlConsts.PATH_MENU_CATEGORIES + "/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(menuCategoryDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Диетическое")));
    }

    @Test
    public void deleteMenuCategoryTest() throws Exception {
        MenuCategoryDTO menuCategoryDTO = createMenuCategoryDTO(1, "Постное");
        when(menuCategoryService.findById(menuCategoryDTO.getId())).thenReturn(menuCategoryDTO);
        mockMvc.perform(delete(UrlConsts.PATH_MENU_CATEGORIES + "/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    public void checkUniqueNameMenuCategoryTest() throws Exception {
        MenuCategory menuCategory = new MenuCategory(1,"Постное", true);
        String name = "Постное";
        when(menuCategoryService.findByName(name)).thenReturn(menuCategory);
        mockMvc.perform(get(UrlConsts.PATH_MENU_CATEGORIES + "/checkMenuCategoryUniqueName?name=" + name)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(String.valueOf(-1)));
    }

    @Test
    public void checkConnectedElementsTest() throws Exception {
        List<String> result = Collections.singletonList("меню: 1");
        when(menuCategoryService.checkConnectedElements(1)).thenReturn(result);
        mockMvc.perform(get(UrlConsts.PATH_MENU_CATEGORIES + "/checkConnectedElements/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }
}
