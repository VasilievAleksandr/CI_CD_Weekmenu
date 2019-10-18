package by.weekmenu.api.controller;

import by.weekmenu.api.dto.RecycleBinDTO;
import by.weekmenu.api.entity.RecycleBin;
import by.weekmenu.api.repository.MealTypeRepository;
import by.weekmenu.api.repository.OwnershipRepository;
import by.weekmenu.api.repository.UnitOfMeasureRepository;
import by.weekmenu.api.service.RecycleBinService;
import by.weekmenu.api.utils.UrlConsts;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(RecycleBinController.class)
public class RecycleBinControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RecycleBinService recycleBinService;

    @MockBean
    private OwnershipRepository ownershipRepository;

    @MockBean
    private UnitOfMeasureRepository unitOfMeasureRepository;

    @MockBean
    private MealTypeRepository mealTypeRepository;

    private RecycleBin createRecycleBin(Long id, String elementName, String entityName, LocalDateTime deleteDate) {
        RecycleBin recycleBin = new RecycleBin();
        recycleBin.setId(id);
        recycleBin.setElementName(elementName);
        recycleBin.setEntityName(entityName);
        recycleBin.setDeleteDate(deleteDate);
        return recycleBin;
    }

    private RecycleBinDTO createRecycleBinDTO(String elementName, String entityName, LocalDateTime deleteDate) {
        RecycleBinDTO recycleBinDTO = new RecycleBinDTO();
        recycleBinDTO.setElementName(elementName);
        recycleBinDTO.setEntityName(entityName);
        recycleBinDTO.setDeleteDate(deleteDate);
        return recycleBinDTO;
    }

    @Test
    public void restoreElement() throws Exception{
        RecycleBin recycleBin = createRecycleBin(1L,"Гречка", "Ингредиент",
                LocalDateTime.of(2019, Month.AUGUST, 31, 11, 0));
        when(recycleBinService.findById(1L)).thenReturn(recycleBin);
        mockMvc.perform(put(UrlConsts.PATH_RECYCLE_BIN + "/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    public void findAllBins() throws Exception{
        List<RecycleBinDTO> list = new ArrayList<>();
        list.add(createRecycleBinDTO("Гречка", "Ингредиент", LocalDateTime.of(2019, Month.AUGUST, 31, 11, 0)));
        list.add(createRecycleBinDTO("Гречневая каша", "Рецепт", LocalDateTime.of(2019, Month.AUGUST, 31, 10, 0)));
        when(recycleBinService.findAll()).thenReturn(list);
        mockMvc.perform(get(UrlConsts.PATH_RECYCLE_BIN)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].elementName", is("Гречка")))
                .andExpect(jsonPath("$[0].entityName", is("Ингредиент")))
                .andExpect(jsonPath("$[0].deleteDate", is("2019-08-31T11:00:00")))
                .andExpect(jsonPath("$[1].elementName", is("Гречневая каша")))
                .andExpect(jsonPath("$[1].entityName", is("Рецепт")))
                .andExpect(jsonPath("$[1].deleteDate", is("2019-08-31T10:00:00")))
                .andDo(print());
    }

    @Test
    public void deleteElement() throws Exception{
        RecycleBin recycleBin = createRecycleBin(1L,"Гречка", "Ингредиент",
                LocalDateTime.of(2019, Month.AUGUST, 31, 11, 0));
        when(recycleBinService.findById(1L)).thenReturn(recycleBin);
        mockMvc.perform(delete(UrlConsts.PATH_RECYCLE_BIN + "/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }
}