package by.weekmenu.api.controller;

import by.weekmenu.api.dto.IngredientDto;
import by.weekmenu.api.service.IngredientService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ingredients")
@Api(description = "REST API для сущности ингредиент")
public class IngredientController {

    private final IngredientService ingredientService;
    private final ModelMapper modelMapper;

    public IngredientController(IngredientService ingredientService, ModelMapper modelMapper) {
        this.ingredientService = ingredientService;
        this.modelMapper = modelMapper;
    }

    @GetMapping
    @ApiOperation("Возвращает список всех ингредиентов")
    public ResponseEntity<List<IngredientDto>> findAllIngredients() {
        return new ResponseEntity<>(ingredientService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @ApiOperation("Находит ингредиент по его Id")
    public ResponseEntity<IngredientDto> findIngredientById(@PathVariable("id") Long id) {
        try {
            return new ResponseEntity<>(ingredientService.findById(id), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>( HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    @ApiOperation("Сохраняет ингредиент")
    public ResponseEntity<IngredientDto> addIngredient(@RequestBody IngredientDto ingredientDto) {
        return new ResponseEntity<>(ingredientService.save(ingredientDto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @ApiOperation("Обновляет ингредиент по Id")
    public ResponseEntity<IngredientDto> updateIngredient(@RequestBody IngredientDto updatedIngredientDto, @PathVariable ("id") Long id) {
        IngredientDto ingredientDto = ingredientService.findById(id);
        if (ingredientDto!=null) {
            return new ResponseEntity<>(ingredientService.save(modelMapper.map(updatedIngredientDto, IngredientDto.class)),
                    HttpStatus.OK);
        } else {
            return new ResponseEntity<>( HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    @ApiOperation("Удаляет ингредиент по Id")
    public ResponseEntity<Void> deleteIngredient(@PathVariable("id") Long id) {
        IngredientDto ingredientDto = ingredientService.findById(id);
        if (ingredientDto!=null) {
            ingredientService.delete(id);
            return ResponseEntity.noContent().build();
        } else {
            return new ResponseEntity<>( HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/checkUniqueName")
    @ApiOperation("Проверяет поле name у ингредиента на уникальность. Возвращает -1, если поле есть в БД и 0, если нет.")
    public Integer checkUniqueName(@RequestParam String name) {
        if (ingredientService.findByName(name) != null) {
            return -1;
        } else {
            return 0;
        }
    }

    @GetMapping("/getUnitOfMeasures")
    @ApiOperation("Возвращает список всех единиц измерения для данного названия ингредиента")
    public ResponseEntity<List<String>> getAllUnitsOfMeasure(@RequestParam String name) {
        return new ResponseEntity<>(ingredientService.findAllUnitsOfMeasure(name), HttpStatus.OK);
    }
}
