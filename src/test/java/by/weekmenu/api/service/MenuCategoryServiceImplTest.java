//package by.weekmenu.api.service;
//
//import by.weekmenu.api.dto.MenuCategoryDTO;
//import by.weekmenu.api.entity.*;
//import by.weekmenu.api.repository.MenuCategoryRepository;
//import by.weekmenu.api.repository.MenuRepository;
//import by.weekmenu.api.repository.RecycleBinRepository;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.modelmapper.ModelMapper;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.test.context.junit4.SpringRunner;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.mockito.Mockito.*;
//
//@RunWith(SpringRunner.class)
//public class MenuCategoryServiceImplTest {
//
//    @MockBean
//    private MenuCategoryRepository menuCategoryRepository;
//
//    @MockBean
//    private RecycleBinRepository recycleBinRepository;
//
//    @MockBean
//    private MenuRepository menuRepository;
//
//    @MockBean
//    private ModelMapper modelMapper;
//
//    private MenuCategoryServiceImpl menuCategoryService;
//
//    @Before
//    public void setup() {
//        menuCategoryService = new MenuCategoryServiceImpl(menuCategoryRepository, recycleBinRepository,
//                menuRepository, modelMapper);
//    }
//
//    @Test
//    public void getAllMenuCategoryTest() {
//        List<MenuCategory> menuCategories = new ArrayList<>();
//        menuCategories.add(new MenuCategory(1, "Постное", false));
//        menuCategories.add(new MenuCategory(2, "Диетическое", false));
//        when(menuCategoryRepository.findAllByIsArchivedIsFalse()).thenReturn(menuCategories);
//        List<MenuCategoryDTO> result = menuCategoryService.findAll();
//        assertThat(menuCategories.size()).isEqualTo(result.size());
//    }
//
//    @Test
//    public void saveMenuCategoryTest() {
//        MenuCategoryDTO menuCategoryDTO = new MenuCategoryDTO();
//        menuCategoryDTO.setId(1);
//        menuCategoryDTO.setName("Постное");
//        MenuCategory menuCategory = new MenuCategory(1, "Постное", true);
//        when(modelMapper.map(menuCategoryDTO, MenuCategory.class)).thenReturn(menuCategory);
//        when(menuCategoryRepository.save(menuCategory)).thenReturn(menuCategory);
//        when(modelMapper.map(menuCategory, MenuCategoryDTO.class)).thenReturn(menuCategoryDTO);
//        MenuCategoryDTO saved = menuCategoryService.save(menuCategoryDTO);
//        assertThat(saved).isNotNull();
//        assertThat(saved.getName()).isEqualTo("Постное");
//    }
//
//    @Test
//    public void findMenuCategoryByIdTest() {
//        MenuCategoryDTO menuCategoryDTO = new MenuCategoryDTO();
//        menuCategoryDTO.setId(1);
//        menuCategoryDTO.setName("Постное");
//        MenuCategory menuCategory = new MenuCategory(1, "Постное", true);
//        when(modelMapper.map(menuCategory, MenuCategoryDTO.class)).thenReturn(menuCategoryDTO);
//        when(menuCategoryRepository.findById(menuCategoryDTO.getId())).thenReturn(Optional.of(menuCategory));
//        MenuCategoryDTO found = menuCategoryService.findById(menuCategoryDTO.getId());
//        assertThat(found).isNotNull();
//        assertThat(found.getName()).isEqualTo("Постное");
//    }
//
//    @Test
//    public void checkUniqueMenuCategoryNameTest() {
//        when(menuCategoryRepository.findByNameIgnoreCase(anyString())).thenReturn(Optional.empty());
//        MenuCategory menuCategory = menuCategoryService.findByName("Постное");
//        assertThat(menuCategory).isNull();
//    }
//
//    @Test
//    public void moveToRecycleBinTest() {
//        MenuCategoryDTO menuCategoryDTO = new MenuCategoryDTO();
//        menuCategoryDTO.setId(1);
//        menuCategoryDTO.setName("Постное");
//        when(recycleBinRepository.save(any(RecycleBin.class))).thenReturn(new RecycleBin());
//        menuCategoryService.moveToRecycleBin(menuCategoryDTO);
//        verify(menuCategoryRepository, times(1)).softDelete(menuCategoryDTO.getId());
//    }
//
//    @Test
//    public void checkConnectedElementsTest() {
//        MenuCategory menuCategory = new MenuCategory(1, "Постное", true);
//        Menu menu = new Menu("Бюджетное", true, new Ownership(OwnershipName.USER));
//        menu.setMenuCategory(menuCategory);
//        List<Menu> menus = new ArrayList();
//        menus.add(menu);
//        when(menuCategoryRepository.findById(menuCategory.getId())).thenReturn(Optional.of(menuCategory));
//        when(menuRepository.findAllByMenuCategory_Id(menuCategory.getId())).thenReturn(menus);
//        List<String> list = menuCategoryService.checkConnectedElements(menuCategory.getId());
//        assertThat(list.size()).isEqualTo(1);
//    }
//}
