//package by.weekmenu.api.integrationTests;
//
//import by.weekmenu.api.ApiApplication;
//import by.weekmenu.api.dto.RecipeSubcategoryDTO;
//import by.weekmenu.api.entity.*;
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
//public class RecipeSubcategoryIntegrationTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Autowired
//    private RecipeSubcategoryRepository recipeSubcategoryRepository;
//
//    @Autowired
//    private RecycleBinRepository recycleBinRepository;
//
//    @Autowired
//    private CookingMethodRepository cookingMethodRepository;
//
//    @Autowired
//    private OwnershipRepository ownershipRepository;
//
//    @Autowired
//    private RecipeRepository recipeRepository;
//
//    @After
//    public void cleanDB() {
//        recipeSubcategoryRepository.deleteAll();
//        recycleBinRepository.deleteAll();
//    }
//
//    @Test
//    @Transactional
//    public void saveRecipeSubcategoryIntegrationTest() throws Exception {
//        RecipeSubcategoryDTO recipeSubcategoryDTO = new RecipeSubcategoryDTO();
//        recipeSubcategoryDTO.setName("????????????");
//        ObjectMapper objectMapper = new ObjectMapper();
//        mockMvc.perform(post(UrlConsts.PATH_RECIPESUBCATEGORIES)
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(objectMapper.writeValueAsBytes(recipeSubcategoryDTO)));
//        Iterable<RecipeSubcategory> recipeSubcategories = recipeSubcategoryRepository.findAll();
//        assertThat(recipeSubcategories).extracting(RecipeSubcategory::getName).containsOnly("????????????");
//    }
//
//    @Test
//    public void getAllRecipeSubcategoryIntegrationTest() throws Exception {
//        RecipeSubcategory recipeSubcategory1 = new RecipeSubcategory("????????????");
//        RecipeSubcategory recipeSubcategory2 = new RecipeSubcategory("????????");
//        recipeSubcategoryRepository.save(recipeSubcategory1);
//        recipeSubcategoryRepository.save(recipeSubcategory2);
//        mockMvc.perform(get(UrlConsts.PATH_RECIPESUBCATEGORIES)
//                .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$", hasSize(2)))
//                .andExpect(jsonPath("$[0].name", is("????????????")))
//                .andExpect(jsonPath("$[1].name", is("????????")));
//    }
//
//    @Test
//    public void updateRecipeSubcategoryIntegrationTest() throws Exception {
//        RecipeSubcategory recipeSubcategory = new RecipeSubcategory("????????????");
//        recipeSubcategoryRepository.save(recipeSubcategory);
//        RecipeSubcategoryDTO recipeSubcategoryDTO = new RecipeSubcategoryDTO();
//        recipeSubcategoryDTO.setName("????????");
//        ObjectMapper objectMapper = new ObjectMapper();
//        mockMvc.perform(put(UrlConsts.PATH_RECIPESUBCATEGORIES + "/" + recipeSubcategory.getId().toString())
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(objectMapper.writeValueAsBytes(recipeSubcategoryDTO)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.name", is("????????")));
//    }
//
//    @Test
//    public void deleteRecipeSubcategoryIntegrationTest() throws Exception {
//        RecipeSubcategory recipeSubcategory = recipeSubcategoryRepository.save(new RecipeSubcategory("????????"));
//        mockMvc.perform(delete(UrlConsts.PATH_RECIPESUBCATEGORIES + "/" + recipeSubcategory.getId().toString())
//                .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isNoContent());
//
//        Iterable<RecycleBin> recycleBins = recycleBinRepository.findAll();
//        assertThat(recycleBins).extracting(RecycleBin::getElementName).containsOnly("????????");
//        assertThat(recycleBins).extracting(RecycleBin::getEntityName).containsOnly("???????????????????????? ??????????????");
//        assertThat(recycleBins).extracting(RecycleBin::getDeleteDate).isNotNull();
//
//        Optional<RecipeSubcategory> recipeSubcategoryAfterSoftDelete = recipeSubcategoryRepository.findById(recipeSubcategory.getId());
//        assertThat(recipeSubcategoryAfterSoftDelete.get().isArchived()).isTrue();
//    }
//
//    @Test
//    @Transactional
//    public void checkConnectedElementsTest() throws Exception {
//        RecipeSubcategory recipeSubcategory = recipeSubcategoryRepository.save(new RecipeSubcategory("????????"));
//        Recipe recipe = new Recipe();
//        recipe.setName("?????????????????? ????????");
//        recipe.setCookingTime(new Short("30"));
//        recipe.setActiveTime(new Short("15"));
//        recipe.setPortions((short) 2);
//        recipe.setImageLink("images/image.png");
//        recipe.setSource("http://bestrecipes.com/best-recipe");
//        recipe.setCookingMethod(cookingMethodRepository.save(new CookingMethod("??????????")));
//        recipe.setOwnership(ownershipRepository.findByName(OwnershipName.ADMIN.name()).orElse(null));
//        recipe.addRecipeSubcategory(recipeSubcategory);
//        recipeRepository.save(recipe);
//
//        mockMvc.perform(get(UrlConsts.PATH_RECIPESUBCATEGORIES + "/checkConnectedElements/" + recipeSubcategory.getId().toString())
//                .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$", hasSize(1)));
//    }
//}
