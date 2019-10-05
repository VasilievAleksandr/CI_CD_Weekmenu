package by.weekmenu.api.controller;

import by.weekmenu.api.dto.MealTypeDTO;
import by.weekmenu.api.service.MealTypeService;
import by.weekmenu.api.utils.UrlConsts;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(UrlConsts.PATH_MEAL_TYPES)
@Api(description = "REST API для сущности MealType")
public class MealTypeController {

    private final MealTypeService mealTypeService;

    public MealTypeController(MealTypeService mealTypeService) {
        this.mealTypeService = mealTypeService;
    }

    @GetMapping
    @ApiOperation("Возвращает список всех MealType")
    public ResponseEntity<List<MealTypeDTO>> findAllMealTypes() {
        return new ResponseEntity<>(mealTypeService.findAll(), HttpStatus.OK);
    }

    @PostMapping
    @ApiOperation("Сохраняет MealType.")
    public ResponseEntity<MealTypeDTO> addMealType(@RequestBody MealTypeDTO mealTypeDTO) {
        return new ResponseEntity<>(mealTypeService.save(mealTypeDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @ApiOperation("Обновляет MealType по Id.")
    public ResponseEntity<MealTypeDTO> updateMealType(@RequestBody MealTypeDTO updatedMealTypeDTO, @PathVariable("id") Short id) {
        MealTypeDTO mealTypeDTO = mealTypeService.findById(id);
        if (mealTypeDTO != null) {
            mealTypeDTO.setName(updatedMealTypeDTO.getName());
            mealTypeDTO.setPriority(updatedMealTypeDTO.getPriority());
            return new ResponseEntity<>(mealTypeService.save(mealTypeDTO), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    @ApiOperation("Перемещает в корзину MealType по Id.")
    public ResponseEntity<Void> deleteMealType(@PathVariable("id") Short id) {
        MealTypeDTO mealTypeDTO = mealTypeService.findById(id);
        if (mealTypeDTO != null) {
            mealTypeService.moveToRecycleBin(mealTypeDTO);
            return ResponseEntity.noContent().build();
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/checkMealTypeUniqueName")
    @ApiOperation("Проверяет поле name у MealType на уникальность. Возвращает -1, если поле есть в БД и 0, если нет.")
    public Integer checkMealTypeUniqueName(@RequestParam String name) {
        if (mealTypeService.findByName(name) != null) {
            return -1;
        } else {
            return 0;
        }
    }

    @GetMapping("/checkConnectedElements/{id}")
    @ApiOperation("Проверяет наличие связанных элементов по Id")
    public List<String> checkConnectedElements(@PathVariable("id") Short id) {
        return mealTypeService.checkConnectedElements(id);
    }
}
