package by.weekmenu.api.integrationTests;

import by.weekmenu.api.ApiApplication;
import by.weekmenu.api.dto.UnitOfMeasureDTO;
import by.weekmenu.api.entity.*;
import by.weekmenu.api.repository.*;
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

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ApiApplication.class)
@AutoConfigureMockMvc
public class UnitOfMeasureIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UnitOfMeasureRepository unitOfMeasureRepository;

    @Autowired
    private RecycleBinRepository recycleBinRepository;

    @Autowired
    private OwnershipRepository ownershipRepository;

    @Autowired
    private IngredientRepository ingredientRepository;

    @Autowired
    IngredientUnitOfMeasureRepository ingredientUnitOfMeasureRepository;

    @After
    public void cleanDB() {
        recycleBinRepository.deleteAll();
        unitOfMeasureRepository.deleteAll();
        ingredientRepository.deleteAll();
        ingredientUnitOfMeasureRepository.deleteAll();
    }

    @Test
    @Transactional
    public void saveUnitOfMeasureIntegrationTest() throws Exception {
        unitOfMeasureRepository.deleteAll();
        UnitOfMeasureDTO unitOfMeasureDTO = new UnitOfMeasureDTO();
        unitOfMeasureDTO.setShortName("л");
        unitOfMeasureDTO.setFullName("Литр");
        ObjectMapper objectMapper = new ObjectMapper();
        mockMvc.perform(post("/unitOfMeasures")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(unitOfMeasureDTO)));
        Iterable<UnitOfMeasure> unitOfMeasure = unitOfMeasureRepository.findAll();
        assertThat(unitOfMeasure).extracting(UnitOfMeasure::getFullName).containsOnly("Литр");
        assertThat(unitOfMeasure).extracting(UnitOfMeasure::getShortName).containsOnly("л");
    }

    @Test
    public void getAllUnitOfMeasureIntegrationTest() throws Exception {
        unitOfMeasureRepository.deleteAll();
        UnitOfMeasure unitOfMeasure = new UnitOfMeasure("л", "Литр");
        UnitOfMeasure unitOfMeasure2 = new UnitOfMeasure("Гр", "Грамм");
        unitOfMeasureRepository.save(unitOfMeasure);
        unitOfMeasureRepository.save(unitOfMeasure2);
        mockMvc.perform(get("/unitOfMeasures")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].shortName", is("л")))
                .andExpect(jsonPath("$[0].fullName", is("Литр")))
                .andExpect(jsonPath("$[1].shortName", is("Гр")))
                .andExpect(jsonPath("$[1].fullName", is("Грамм")));
    }

    @Test
    public void updateUnitOfMeasureIntegrationTest() throws Exception {
        UnitOfMeasure unitOfMeasure = new UnitOfMeasure("л", "Литр");
        unitOfMeasureRepository.save(unitOfMeasure);
        UnitOfMeasureDTO unitOfMeasureDTO = new UnitOfMeasureDTO();
        unitOfMeasureDTO.setFullName("ЛЛитРР");
        unitOfMeasureDTO.setShortName("л");
        ObjectMapper objectMapper = new ObjectMapper();
        mockMvc.perform(put("/unitOfMeasures/" + unitOfMeasure.getId().toString())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(unitOfMeasureDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fullName", is("ЛЛитРР")));
    }

    @Test
    public void deleteUnitOfMeasureIntegrationTest() throws Exception {
        UnitOfMeasure unitOfMeasure = new UnitOfMeasure("л", "Литр");
        unitOfMeasureRepository.save(unitOfMeasure);
        mockMvc.perform(delete("/unitOfMeasures/" + unitOfMeasure.getId().toString())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
        Iterable<RecycleBin> recycleBins = recycleBinRepository.findAll();
        assertThat(recycleBins).extracting(RecycleBin::getElementName).containsOnly("Литр");
        assertThat(recycleBins).extracting(RecycleBin::getEntityName).containsOnly("Единица измерения");
        assertThat(recycleBins).extracting(RecycleBin::getDeleteDate).isNotNull();
        Optional<UnitOfMeasure> unitOfMeasureAfterSoftDelete = unitOfMeasureRepository.findById(unitOfMeasure.getId());
        assertThat(unitOfMeasureAfterSoftDelete.get().isArchived()).isTrue();
    }

    @Test
    public void checkUniqueFullNameUnitOfMeasureIntegrationTest() throws Exception {
        UnitOfMeasure unitOfMeasure = new UnitOfMeasure("л", "Литр");
        unitOfMeasureRepository.save(unitOfMeasure);
        mockMvc.perform(get("/unitOfMeasures/checkUniqueFullName?fullName=" + unitOfMeasure.getFullName())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(String.valueOf(-1)));
    }

    @Test
    public void checkUniqueShortNameUnitOfMeasureIntegrationTest() throws Exception {
        UnitOfMeasure unitOfMeasure = new UnitOfMeasure("л", "Литр");
        unitOfMeasureRepository.save(unitOfMeasure);
        mockMvc.perform(get("/unitOfMeasures/checkUniqueShortName?shortName=" + unitOfMeasure.getShortName())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(String.valueOf(-1)));
    }

    @Test
    @Transactional
    public void checkConnectedElementsTest() throws Exception {
        UnitOfMeasure unitOfMeasure = new UnitOfMeasure("л", "Литр");
        unitOfMeasureRepository.save(unitOfMeasure);
        Ingredient ingredient = new Ingredient();
        ingredient.setName("Курица");
        ingredient.setCalories(new BigDecimal("100"));
        ingredient.setCarbs(new BigDecimal("100"));
        ingredient.setFats(new BigDecimal("100"));
        ingredient.setProteins(new BigDecimal("100"));
        ingredient.setOwnership(ownershipRepository.findByName(OwnershipName.ADMIN.name()).orElse(null));
        ingredientRepository.save(ingredient);
        IngredientUnitOfMeasure ingredientUnitOfMeasure = new IngredientUnitOfMeasure(new BigDecimal(3));
        ingredientUnitOfMeasure.setId(new IngredientUnitOfMeasure.Id(ingredient.getId(), unitOfMeasure.getId()));
        ingredientUnitOfMeasureRepository.save(ingredientUnitOfMeasure);
        mockMvc.perform(get("/unitOfMeasures/checkConnectedElements/" + unitOfMeasure.getId().toString())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }
}
