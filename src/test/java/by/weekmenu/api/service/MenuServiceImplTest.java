package by.weekmenu.api.service;

import by.weekmenu.api.dto.MenuDTO;
import by.weekmenu.api.dto.MenuPriceDTO;
import by.weekmenu.api.dto.MenuRecipeDTO;
import by.weekmenu.api.entity.*;
import by.weekmenu.api.repository.*;
import by.weekmenu.api.utils.MenuCalculations;
import lombok.RequiredArgsConstructor;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.DayOfWeek;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@RequiredArgsConstructor
public class MenuServiceImplTest {

    @MockBean
    private MenuRepository menuRepository;
    @MockBean
    private OwnershipRepository ownershipRepository;
    @MockBean
    private MenuCategoryRepository menuCategoryRepository;
    @MockBean
    private RecipeRepository recipeRepository;
    @MockBean
    private MealTypeRepository mealTypeRepository;
    @MockBean
    private MenuRecipeRepository menuRecipeRepository;
    @MockBean
    private MenuCalculations menuCalculations;
    @MockBean
    private RecycleBinRepository recycleBinRepository;
    @MockBean
    private MenuPriceRepository menuPriceRepository;
    @MockBean
    private ModelMapper modelMapper;

    private MenuService menuService;

    @Before
    public void setup() {
        menuService = new MenuServiceImpl(menuRepository, ownershipRepository, menuCategoryRepository,
                recipeRepository, mealTypeRepository, menuRecipeRepository,
                menuCalculations, recycleBinRepository, menuPriceRepository, modelMapper);
    }

    private MenuDTO createMenuDTO(String name) {
        MenuDTO menuDTO = new MenuDTO();
        menuDTO.setName(name);
        menuDTO.setMenuCategoryName("Бюджетное");
        menuDTO.setAuthorName("Повар");
        menuDTO.setAuthorImageLink("/images/photo.jpg");
        menuDTO.setMenuDescription("Очень вкусно");
        menuDTO.setIsActive(true);
        menuDTO.setOwnershipName("ADMIN");
        Set<MenuRecipeDTO> menuRecipeDTOS = getMenuRecipeDTOS();
        menuDTO.setMenuRecipeDTOS(menuRecipeDTOS);
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

    private Menu createMenu (String name) {
        Menu menu = new Menu();
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
    public void updateMenus() {
        List<MenuRecipe> menuRecipes = new ArrayList<>();
        MenuRecipe menuRecipe = mock(MenuRecipe.class);
        menuRecipes.add(menuRecipe);
        when(menuRecipeRepository.findAllByRecipe_Id(anyLong())).thenReturn(menuRecipes);
        MenuDTO menuDTO = createMenuDTO("Вкусное меню");
        Menu menu = createMenu("Вкусное меню");
        when(menuRecipe.getMenu()).thenReturn(menu);
        when(modelMapper.map(menu, MenuDTO.class)).thenReturn(menuDTO);
        menuService.updateMenus(1L);
        verify(menuRepository, times(1)).save(menu);
    }

    @Test
    public void delete() {
        menuService.delete(1L);
        verify(menuRecipeRepository, times(1)).deleteAllByMenu_Id(1L);
        verify(menuPriceRepository, times(1)).deleteAllById_MenuId(1L);
        verify(menuRepository, times(1)).deleteById(1L);
    }

    @Test
    public void save() {
        MenuDTO menuDTO = createMenuDTO("Вкусное меню");
        Menu menu = createMenu("Вкусное меню");
        when(modelMapper.map(menuDTO, Menu.class)).thenReturn(menu);
        when(menuRepository.save(menu)).thenReturn(menu);
        when(modelMapper.map(menu, MenuDTO.class)).thenReturn(menuDTO);
        when(recipeRepository.findByNameIgnoreCase(anyString())).thenReturn(Optional.of(new Recipe()));
        when(mealTypeRepository.findByNameIgnoreCase(anyString())).thenReturn(Optional.of(new MealType()));
        MenuDTO saved = menuService.save(menuDTO);
        assertThat(saved).isNotNull();
        assertThat(saved.getName()).isEqualTo("Вкусное меню");
        assertThat(saved.getMenuCategoryName()).isEqualTo("Бюджетное");
        assertThat(saved.getAuthorName()).isEqualTo("Повар");
        assertThat(saved.getAuthorImageLink()).isEqualTo("/images/photo.jpg");
        assertThat(saved.getMenuDescription()).isEqualTo("Очень вкусно");
        assertThat(saved.getOwnershipName()).isEqualTo("ADMIN");
    }

    @Test
    public void findById() {
        MenuDTO menuDTO = createMenuDTO("Вкусное меню");
        Menu menu = createMenu("Вкусное меню");
        when(modelMapper.map(menu, MenuDTO.class)).thenReturn(menuDTO);
        when(menuRepository.findById(menuDTO.getId())).thenReturn(Optional.of(menu));
        MenuDTO found = menuService.findById(menuDTO.getId());
        assertThat(found).isNotNull();
        assertThat(found.getName()).isEqualTo("Вкусное меню");
        assertThat(found.getMenuCategoryName()).isEqualTo("Бюджетное");
        assertThat(found.getAuthorName()).isEqualTo("Повар");
        assertThat(found.getAuthorImageLink()).isEqualTo("/images/photo.jpg");
        assertThat(found.getMenuDescription()).isEqualTo("Очень вкусно");
        assertThat(found.getOwnershipName()).isEqualTo("ADMIN");
    }

    @Test
    public void findAll() {
        Menu menu1 = createMenu("Вкусное меню");
        Menu menu2 = createMenu("Праздничное меню");
        List<Menu> menus = Arrays.asList(menu1, menu2);
        MenuDTO menuDTO1 = createMenuDTO("Вкусное меню");
        MenuDTO menuDTO2 = createMenuDTO("Праздничное меню");
        when(menuRepository.findAllByIsArchivedIsFalse()).thenReturn(menus);
        when(modelMapper.map(menu1, MenuDTO.class)).thenReturn(menuDTO1);
        when(modelMapper.map(menu2, MenuDTO.class)).thenReturn(menuDTO2);
        List<MenuDTO> result = menuService.findAll();
        assertThat(result.size()).isEqualTo(menus.size());
    }

    @Test
    public void moveToRecycleBin() {
        MenuDTO menuDTO = createMenuDTO("Вкусное меню");
        when(recycleBinRepository.save(any(RecycleBin.class))).thenReturn(new RecycleBin());
        menuService.moveToRecycleBin(menuDTO);
        verify(menuRepository, times(1)).softDelete(menuDTO.getId());
    }
}