package by.weekmenu.api.integrationTests;

import by.weekmenu.api.ApiApplication;
import by.weekmenu.api.entity.RecycleBin;
import by.weekmenu.api.entity.UnitOfMeasure;
import by.weekmenu.api.repository.RecycleBinRepository;
import by.weekmenu.api.repository.UnitOfMeasureRepository;
import by.weekmenu.api.utils.EntityNamesConsts;
import org.assertj.core.api.Assertions;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ApiApplication.class)
@AutoConfigureMockMvc
public class RecycleBinIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RecycleBinRepository recycleBinRepository;

    @Autowired
    private UnitOfMeasureRepository unitOfMeasureRepository;

    @After
    public void cleanDB() {
        recycleBinRepository.deleteAll();
        unitOfMeasureRepository.deleteAll();
    }

    private RecycleBin createRecycleBin(String elementName, String entityName, LocalDateTime deleteDate) {
        RecycleBin recycleBin = new RecycleBin();
        recycleBin.setElementName(elementName);
        recycleBin.setEntityName(entityName);
        recycleBin.setDeleteDate(deleteDate);
        return recycleBin;
    }

    @Test
    public void getAllRecycleBins() throws Exception{
        recycleBinRepository.save(createRecycleBin("Литр", EntityNamesConsts.UNIT_OF_MEASURE, LocalDateTime.of(2019, Month.AUGUST, 31, 11, 0)));
        recycleBinRepository.save(createRecycleBin("Молоко", EntityNamesConsts.INGREDIENT, LocalDateTime.of(2019, Month.AUGUST, 31, 10, 0)));
        mockMvc.perform(get("/recycleBin")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].elementName", is("Литр")))
                .andExpect(jsonPath("$[0].entityName", is("Единица измерения")))
                .andExpect(jsonPath("$[0].deleteDate", is("2019-08-31T11:00:00")))
                .andExpect(jsonPath("$[1].elementName", is("Молоко")))
                .andExpect(jsonPath("$[1].entityName", is("Ингредиент")))
                .andExpect(jsonPath("$[1].deleteDate", is("2019-08-31T10:00:00")));
    }

    @Test
    public void restoreElement() throws Exception{
        UnitOfMeasure unitOfMeasure = new UnitOfMeasure("л", "Литр");
        unitOfMeasure.setArchived(true);
        unitOfMeasureRepository.save(unitOfMeasure);
        RecycleBin recycleBin = recycleBinRepository
                .save(createRecycleBin("Литр", EntityNamesConsts.UNIT_OF_MEASURE,
                        LocalDateTime.of(2019, Month.AUGUST, 31, 11, 0)));
        mockMvc.perform(put("/recycleBin/" + recycleBin.getId().toString())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        Optional<UnitOfMeasure> unitOfMeasureFromDB = unitOfMeasureRepository.findByFullNameIgnoreCase("Литр");
        Assertions.assertThat(unitOfMeasureFromDB.get().isArchived()).isFalse();
        Assertions.assertThat(StreamSupport.stream(recycleBinRepository.findAll().spliterator(), false)
                .filter(recycleBin1 -> "Литр".equals(recycleBin1.getElementName()))
                .findAny()
                .orElse(null)).isNull();
    }

    @Test
    public void deleteElement() throws Exception{
        UnitOfMeasure unitOfMeasure = new UnitOfMeasure("л", "Литр");
        unitOfMeasure.setArchived(true);
        unitOfMeasureRepository.save(unitOfMeasure);
        RecycleBin recycleBin = recycleBinRepository
                .save(createRecycleBin("Литр", EntityNamesConsts.UNIT_OF_MEASURE,
                        LocalDateTime.of(2019, Month.AUGUST, 31, 11, 0)));
        mockMvc.perform(delete("/recycleBin/" + recycleBin.getId().toString())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        Optional<UnitOfMeasure> unitOfMeasureOptional = unitOfMeasureRepository.findByFullNameIgnoreCase("Литр");
        assertFalse(unitOfMeasureOptional.isPresent());
        Assertions.assertThat(StreamSupport.stream(recycleBinRepository.findAll().spliterator(), false)
                .filter(recycleBin1 -> "Литр".equals(recycleBin1.getElementName()))
                .findAny()
                .orElse(null)).isNull();
    }
}
