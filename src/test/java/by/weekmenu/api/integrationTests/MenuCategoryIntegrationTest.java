package by.weekmenu.api.integrationTests;

import by.weekmenu.api.ApiApplication;
import by.weekmenu.api.dto.MenuCategoryDTO;
import by.weekmenu.api.entity.*;
import by.weekmenu.api.repository.*;
import by.weekmenu.api.utils.UrlConsts;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ApiApplication.class)
@AutoConfigureMockMvc
public class MenuCategoryIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MenuCategoryRepository menuCategoryRepository;

    @Autowired
    private RecycleBinRepository recycleBinRepository;

    @Autowired
    private OwnershipRepository ownershipRepository;

    @Autowired
    private MenuRepository menuRepository;

    @After
    public void cleanDB() {
        menuCategoryRepository.deleteAll();
        recycleBinRepository.deleteAll();
    }

    private MenuCategoryDTO createMenuCategoryDTO(Integer id, String name) {
        MenuCategoryDTO menuCategoryDTO = new MenuCategoryDTO();
        menuCategoryDTO.setId(id);
        menuCategoryDTO.setName(name);
        menuCategoryDTO.setPriority(3);
        menuCategoryDTO.setImageLink("imagelink");
        menuCategoryDTO.setArchived(false);
        return menuCategoryDTO;
    }

    @Test
    @Transactional
    public void saveMenuCategoryIntegrationTest() throws Exception {
        MenuCategoryDTO menuCategoryDTO = createMenuCategoryDTO(1, "Постное");
        ObjectMapper objectMapper = new ObjectMapper();
        mockMvc.perform(post(UrlConsts.PATH_MENU_CATEGORIES)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(menuCategoryDTO)));
        Iterable<MenuCategory> menuCategories = menuCategoryRepository.findAll();
        assertThat(menuCategories).extracting(MenuCategory::getName).containsOnly("Постное");
    }

    @Test
    public void getAllMenuCategoryIntegrationTest() throws Exception {
        MenuCategory menuCategory1 = new MenuCategory(1, "Постное", false);
        MenuCategory menuCategory2 = new MenuCategory(2, "Диетическое", false);
        menuCategoryRepository.save(menuCategory1);
        menuCategoryRepository.save(menuCategory2);
        mockMvc.perform(get(UrlConsts.PATH_MENU_CATEGORIES)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", is("Постное")))
                .andExpect(jsonPath("$[1].name", is("Диетическое")));
    }

    @Test
    public void updateMenuCategoryIntegrationTest() throws Exception {
        MenuCategory menuCategory = new MenuCategory("Постное", false);
        menuCategory.setPriority(3);
        menuCategory.setImageLink("imagelink");
        menuCategoryRepository.save(menuCategory);
        MenuCategoryDTO menuCategoryDTO = createMenuCategoryDTO(menuCategory.getId(), "Диетическое");
        ObjectMapper objectMapper = new ObjectMapper();
        mockMvc.perform(put(UrlConsts.PATH_MENU_CATEGORIES + "/" + menuCategory.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(menuCategoryDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Диетическое")));
    }

    @Test
    public void deleteMenuCategoryIntegrationTest() throws Exception {
        MenuCategory menuCategory = new MenuCategory("Постное", false);
        menuCategoryRepository.save(menuCategory);
        mockMvc.perform(delete(UrlConsts.PATH_MENU_CATEGORIES + "/" + menuCategory.getId().toString())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        Iterable<RecycleBin> recycleBins = recycleBinRepository.findAll();
        assertThat(recycleBins).extracting(RecycleBin::getElementName).containsOnly("Постное");
        assertThat(recycleBins).extracting(RecycleBin::getEntityName).containsOnly("Категория меню");
        assertThat(recycleBins).extracting(RecycleBin::getDeleteDate).isNotNull();

        Optional<MenuCategory> menuCategoryAfterSoftDelete = menuCategoryRepository.findById(menuCategory.getId());
        assertThat(menuCategoryAfterSoftDelete.get().isArchived()).isTrue();
    }

    @Test
    @Transactional
    public void checkConnectedElementsTest() throws Exception {
        MenuCategory menuCategory = new MenuCategory("Постное", false);
        menuCategoryRepository.save(menuCategory);
        Menu menu = new Menu();
        menu.setName("Бюджетное");
        menu.setIsActive(true);
        menu.setMenuCategory(menuCategory);
        ownershipRepository.findByName(OwnershipName.ADMIN.name()).ifPresent(menu::setOwnership);
        menuRepository.save(menu);
        mockMvc.perform(get(UrlConsts.PATH_MENU_CATEGORIES + "/checkConnectedElements/" + menuCategory.getId().toString())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }
}
