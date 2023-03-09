package by.weekmenu.api.controller;

import by.weekmenu.api.dto.MenuDTO;
import by.weekmenu.api.dto.MenuPriceDTO;
import by.weekmenu.api.dto.MenuRecipeDTO;
import by.weekmenu.api.entity.Menu;
import by.weekmenu.api.entity.MenuCategory;
import by.weekmenu.api.entity.Ownership;
import by.weekmenu.api.entity.OwnershipName;
import by.weekmenu.api.repository.MealTypeRepository;
import by.weekmenu.api.repository.OwnershipRepository;
import by.weekmenu.api.repository.UnitOfMeasureRepository;
import by.weekmenu.api.service.MenuService;
import by.weekmenu.api.utils.UrlConsts;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(MenuController.class)
public class MenuControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MenuService menuService;

    @MockBean
    private OwnershipRepository ownershipRepository;

    @MockBean
    private UnitOfMeasureRepository unitOfMeasureRepository;

    @MockBean
    private MealTypeRepository mealTypeRepository;

    @MockBean
    private ModelMapper modelMapper;

    private MenuDTO createMenuDTO(Long id, String name) {
        MenuDTO menuDTO = new MenuDTO();
        menuDTO.setId(id);
        menuDTO.setName(name);
        menuDTO.setMenuCategoryName("Бюджетное");
        menuDTO.setAuthorName("Повар");
        menuDTO.setAuthorImageLink("/images/photo.jpg");
        menuDTO.setMenuDescription("Очень вкусно");
        menuDTO.setIsActive(true);
        menuDTO.setOwnershipName("ADMIN");
        menuDTO.setMenuRecipeDTOS(getMenuRecipeDTOS());
        menuDTO.setMenuPriceDTOS(getMenuPriceDTOS());
        return menuDTO;
    }

    private Set<MenuPriceDTO> getMenuPriceDTOS() {
        Set<MenuPriceDTO> menuPriceDTOS = new HashSet<>();
        MenuPriceDTO menuPriceDTO = new MenuPriceDTO();
        menuPriceDTO.setMenuName("Вкусное меню");
        menuPriceDTO.setCurrencyCode("BYN");
        menuPriceDTO.setPriceValue("100");
        menuPriceDTO.setRegionName("Минск");
        menuPriceDTOS.add(menuPriceDTO);
        return menuPriceDTOS;
    }

    private Set<MenuRecipeDTO> getMenuRecipeDTOS() {
        Set<MenuRecipeDTO> menuRecipeDTOS = new HashSet<>();
        MenuRecipeDTO menuRecipeDTO = new MenuRecipeDTO();
        menuRecipeDTO.setRecipeName("Гречневая каша");
        menuRecipeDTO.setMealTypeName("Завтрак");
        menuRecipeDTO.setDayOfWeek(DayOfWeek.MONDAY);
        menuRecipeDTOS.add(menuRecipeDTO);
        return menuRecipeDTOS;
    }

    private Menu createMenu (Long id, String name) {
        Menu menu = new Menu();
        menu.setId(id);
        menu.setName(name);
        menu.setMenuCategory(new MenuCategory("Бюджетное", false));
        menu.setAuthorName("Повар");
        menu.setAuthorImageLink("/images/photo.jpg");
        menu.setMenuDescription("Очень вкусно");
        menu.setIsActive(true);
        menu.setOwnership(new Ownership(OwnershipName.ADMIN));
        return menu;
    }

    @Test
    public void findAllMenusTest() throws Exception{
        List<MenuDTO> menus = new ArrayList<>();
        menus.add(createMenuDTO(1L, "Вкусное меню"));
        menus.add(createMenuDTO(2L, "Праздничное меню"));
        when(menuService.findAll()).thenReturn(menus);

        mockMvc.perform(get(UrlConsts.PATH_MENUS)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("Вкусное меню")))
                .andExpect(jsonPath("$[0].menuCategoryName", is("Бюджетное")))
                .andExpect(jsonPath("$[0].authorName", is("Повар")))
                .andExpect(jsonPath("$[0].authorImageLink", is("/images/photo.jpg")))
                .andExpect(jsonPath("$[0].menuDescription", is("Очень вкусно")))
                .andExpect(jsonPath("$[0].isActive", is (true)))
                .andExpect(jsonPath("$[0].ownershipName", is("ADMIN")))
                .andExpect(jsonPath("$[0].menuRecipeDTOS", iterableWithSize(1)))
                .andExpect(jsonPath("$[0].menuPriceDTOS", iterableWithSize(1)))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].name", is("Праздничное меню")))
                .andExpect(jsonPath("$[1].menuCategoryName", is("Бюджетное")))
                .andExpect(jsonPath("$[1].authorName", is("Повар")))
                .andExpect(jsonPath("$[1].authorImageLink", is("/images/photo.jpg")))
                .andExpect(jsonPath("$[1].menuDescription", is("Очень вкусно")))
                .andExpect(jsonPath("$[1].isActive", is (true)))
                .andExpect(jsonPath("$[1].ownershipName", is("ADMIN")))
                .andExpect(jsonPath("$[1].menuRecipeDTOS", iterableWithSize(1)))
                .andExpect(jsonPath("$[1].menuPriceDTOS", iterableWithSize(1)));
    }

    @Test
    public void addMenuTest() throws Exception{
        MenuDTO menuDTO = createMenuDTO(1L, "Вкусное меню");
        when(menuService.save(any(MenuDTO.class))).thenReturn(menuDTO);
        ObjectMapper objectMapper = new ObjectMapper();
        mockMvc.perform(post(UrlConsts.PATH_MENUS)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(menuDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Вкусное меню")))
                .andExpect(jsonPath("$.menuCategoryName", is("Бюджетное")))
                .andExpect(jsonPath("$.authorName", is("Повар")))
                .andExpect(jsonPath("$.authorImageLink", is("/images/photo.jpg")))
                .andExpect(jsonPath("$.menuDescription", is("Очень вкусно")))
                .andExpect(jsonPath("$.isActive", is (true)))
                .andExpect(jsonPath("$.ownershipName", is("ADMIN")))
                .andExpect(jsonPath("$.menuRecipeDTOS", iterableWithSize(1)))
                .andExpect(jsonPath("$.menuPriceDTOS", iterableWithSize(1)));
    }

    @Test
    public void getMenuTest() throws Exception{
        MenuDTO menuDTO = createMenuDTO(1L, "Вкусное меню");
        when(menuService.findById(1L)).thenReturn(menuDTO);
        when(menuService.findById(2L)).thenReturn(null);

        mockMvc.perform(get(UrlConsts.PATH_MENUS + "/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isFound())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Вкусное меню")))
                .andExpect(jsonPath("$.menuCategoryName", is("Бюджетное")))
                .andExpect(jsonPath("$.authorName", is("Повар")))
                .andExpect(jsonPath("$.authorImageLink", is("/images/photo.jpg")))
                .andExpect(jsonPath("$.menuDescription", is("Очень вкусно")))
                .andExpect(jsonPath("$.isActive", is (true)))
                .andExpect(jsonPath("$.ownershipName", is("ADMIN")))
                .andExpect(jsonPath("$.menuRecipeDTOS", iterableWithSize(1)))
                .andExpect(jsonPath("$.menuPriceDTOS", iterableWithSize(1)));

        mockMvc.perform(get(UrlConsts.PATH_MENUS + "/2")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void updateMenuTest() throws Exception{
        MenuDTO menuDTO = createMenuDTO(1L, "Вкусное меню");
        when(menuService.findById(menuDTO.getId())).thenReturn(menuDTO);
        menuDTO.setName("Вегетарианское меню");
        menuDTO.setMenuDescription("Без мяса");
        when(modelMapper.map(any(), any())).thenReturn(menuDTO);
        when(menuService.save(any(MenuDTO.class))).thenReturn(menuDTO);
        ObjectMapper objectMapper = new ObjectMapper();
        mockMvc.perform(put(UrlConsts.PATH_MENUS + "/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(menuDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Вегетарианское меню")))
                .andExpect(jsonPath("$.menuCategoryName", is("Бюджетное")))
                .andExpect(jsonPath("$.authorName", is("Повар")))
                .andExpect(jsonPath("$.authorImageLink", is("/images/photo.jpg")))
                .andExpect(jsonPath("$.menuDescription", is("Без мяса")))
                .andExpect(jsonPath("$.isActive", is (true)))
                .andExpect(jsonPath("$.ownershipName", is("ADMIN")))
                .andExpect(jsonPath("$.menuRecipeDTOS", iterableWithSize(1)))
                .andExpect(jsonPath("$.menuPriceDTOS", iterableWithSize(1)));
    }

    @Test
    public void deleteMenuTest() throws Exception{
        MenuDTO menuDTO = createMenuDTO(1L, "Вкусное меню");
        when(menuService.findById(menuDTO.getId())).thenReturn(menuDTO);
        when(menuService.findById(2L)).thenReturn(null);

        mockMvc.perform(delete(UrlConsts.PATH_MENUS + "/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        mockMvc.perform(delete(UrlConsts.PATH_MENUS + "/2")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}