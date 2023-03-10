//package by.weekmenu.api.integrationTests;
//
//import by.weekmenu.api.ApiApplication;
//import by.weekmenu.api.dto.IngredientCategoryDTO;
//import by.weekmenu.api.entity.Ingredient;
//import by.weekmenu.api.entity.IngredientCategory;
//import by.weekmenu.api.entity.OwnershipName;
//import by.weekmenu.api.entity.RecycleBin;
//import by.weekmenu.api.repository.*;
//import by.weekmenu.api.utils.UrlConsts;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.junit.After;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.MediaType;
//import org.springframework.test.context.junit4.SpringRunner;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.math.BigDecimal;
//import java.util.Optional;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.hamcrest.Matchers.hasSize;
//import static org.hamcrest.Matchers.is;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@RunWith(SpringRunner.class)
//@SpringBootTest(classes = ApiApplication.class)
//@AutoConfigureMockMvc
//public class ingredientsCategoryIntegrationTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Autowired
//    private IngredientCategoryRepository ingredientCategoryRepository;
//
//    @Autowired
//    private RecycleBinRepository recycleBinRepository;
//
//    @Autowired
//    private OwnershipRepository ownershipRepository;
//
//    @Autowired
//    private IngredientRepository ingredientRepository;
//
//    @After
//    public void cleanDB() {
//        ingredientCategoryRepository.deleteAll();
//        recycleBinRepository.deleteAll();
//    }
//
//    private IngredientCategoryDTO createIngredientCategoryDTO(Integer id, String name) {
//        IngredientCategoryDTO ingredientCategoryDTO = new IngredientCategoryDTO();
//        ingredientCategoryDTO.setId(id);
//        ingredientCategoryDTO.setName(name);
//        ingredientCategoryDTO.setPriority(3);
//        ingredientCategoryDTO.setImageLink("imagelink");
//        ingredientCategoryDTO.setArchived(false);
//        return ingredientCategoryDTO;
//    }
//
//    @Test
//    @Transactional
//    public void saveIngredientCategoryIntegrationTest() throws Exception {
//        IngredientCategoryDTO ingredientCategoryDTO = createIngredientCategoryDTO(1, "Milk");
//        ObjectMapper objectMapper = new ObjectMapper();
//        mockMvc.perform(post(UrlConsts.PATH_INGREDIENT_CATEGORIES)
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(objectMapper.writeValueAsBytes(ingredientCategoryDTO)));
//        Iterable<IngredientCategory> ingredientCategories = ingredientCategoryRepository.findAll();
//        assertThat(ingredientCategories).extracting(IngredientCategory::getName).containsOnly("Milk");
//    }
//
//    @Test
//    public void getAllIngredientCategoryIntegrationTest() throws Exception {
//        IngredientCategory ingredientCategory1 = new IngredientCategory("Milk", false);
//        IngredientCategory ingredientCategory2 = new IngredientCategory("Bread", false);
//        ingredientCategoryRepository.save(ingredientCategory1);
//        ingredientCategoryRepository.save(ingredientCategory2);
//        mockMvc.perform(get(UrlConsts.PATH_INGREDIENT_CATEGORIES)
//                .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$", hasSize(2)))
//                .andExpect(jsonPath("$[0].name", is("Milk")))
//                .andExpect(jsonPath("$[1].name", is("Bread")));
//    }
//
//    @Test
//    public void updateIngredientCategoryIntegrationTest() throws Exception {
//        IngredientCategory ingredientCategory = new IngredientCategory("Milk", false);
//        ingredientCategory.setPriority(3);
//        ingredientCategory.setImageLink("imagelink");
//        ingredientCategoryRepository.save(ingredientCategory);
//        IngredientCategoryDTO ingredientCategoryDTO = createIngredientCategoryDTO(ingredientCategory.getId(), "Bread");
//        ObjectMapper objectMapper = new ObjectMapper();
//        mockMvc.perform(put(UrlConsts.PATH_INGREDIENT_CATEGORIES + "/" + ingredientCategory.getId())
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(objectMapper.writeValueAsBytes(ingredientCategoryDTO)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.name", is("Bread")));
//    }
//
//    @Test
//    public void deleteIngredientCategoryIntegrationTest() throws Exception {
//        IngredientCategory ingredientCategory = new IngredientCategory("Milk", false);
//        ingredientCategoryRepository.save(ingredientCategory);
//        mockMvc.perform(delete(UrlConsts.PATH_INGREDIENT_CATEGORIES + "/" + ingredientCategory.getId().toString())
//                .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isNoContent());
//
//        Iterable<RecycleBin> recycleBins = recycleBinRepository.findAll();
//        assertThat(recycleBins).extracting(RecycleBin::getElementName).containsOnly("Milk");
//        assertThat(recycleBins).extracting(RecycleBin::getEntityName).containsOnly("Категория ингредиента");
//        assertThat(recycleBins).extracting(RecycleBin::getDeleteDate).isNotNull();
//
//        Optional<IngredientCategory> ingredientCategoryAfterSoftDelete = ingredientCategoryRepository.findById(ingredientCategory.getId());
//        assertThat(ingredientCategoryAfterSoftDelete.get().isArchived()).isTrue();
//    }
//
//    @Test
//    @Transactional
//    public void checkConnectedElementsTest() throws Exception {
//        IngredientCategory ingredientCategory = new IngredientCategory("Milk", false);
//        ingredientCategoryRepository.save(ingredientCategory);
//        Ingredient ingredient = new Ingredient();
//        ingredient.setName("Молоко");
//        ingredient.setCalories(new BigDecimal("100"));
//        ingredient.setCarbs(new BigDecimal("100"));
//        ingredient.setFats(new BigDecimal("100"));
//        ingredient.setProteins(new BigDecimal("100"));
//        ingredient.setIngredientCategory(ingredientCategory);
//        ownershipRepository.findByName(OwnershipName.ADMIN.name()).ifPresent(ingredient::setOwnership);
//        ingredientRepository.save(ingredient);
//        mockMvc.perform(get(UrlConsts.PATH_INGREDIENT_CATEGORIES + "/checkConnectedElements/" + ingredientCategory.getId().toString())
//                .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$", hasSize(1)));
//    }
//}
