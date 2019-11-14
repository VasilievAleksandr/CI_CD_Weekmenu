package by.weekmenu.api.controller;

import by.weekmenu.api.dto.IngredientDTO;
import by.weekmenu.api.service.IngredientService;
import by.weekmenu.api.utils.UrlConsts;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(UrlConsts.PATH_INGREDIENTS)
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
    public ResponseEntity<List<IngredientDTO>> findAllIngredients() {
        return new ResponseEntity<>(ingredientService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @ApiOperation("Находит ингредиент по его Id")
    public ResponseEntity<IngredientDTO> findIngredientById(@PathVariable("id") Long id) {
        try {
            return new ResponseEntity<>(ingredientService.findById(id), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>( HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    @ApiOperation("Сохраняет ингредиент")
    public ResponseEntity<IngredientDTO> addIngredient(@RequestBody IngredientDTO ingredientDto) {
        return new ResponseEntity<>(ingredientService.save(ingredientDto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @ApiOperation("Обновляет ингредиент по Id")
    public ResponseEntity<IngredientDTO> updateIngredient(@RequestBody IngredientDTO updatedIngredientDTO, @PathVariable ("id") Long id) {
        IngredientDTO ingredientDto = ingredientService.findById(id);
        if (ingredientDto!=null) {
            return new ResponseEntity<>(ingredientService.save(modelMapper.map(updatedIngredientDTO, IngredientDTO.class)),
                    HttpStatus.OK);
        } else {
            return new ResponseEntity<>( HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    @ApiOperation("Перемещает в корзину ингредиент по Id")
    public ResponseEntity<Void> deleteIngredient(@PathVariable("id") Long id) {
        IngredientDTO ingredientDto = ingredientService.findById(id);
        if (ingredientDto!=null) {
            ingredientService.moveToRecycleBin(ingredientDto);
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

    @GetMapping("/search")
    @ApiOperation("Возвращает список ингредиентов, у которых в названии содержится параметр {name}")
    public ResponseEntity<List<IngredientDTO>> findIngredientByName(@RequestParam String name) {
        return new ResponseEntity<>(ingredientService.findIngredientByName(name), HttpStatus.OK);
    }


    @GetMapping("/getUnitOfMeasures")
    @ApiOperation("Возвращает список всех единиц измерения для данного названия ингредиента")
    public ResponseEntity<List<String>> getAllUnitsOfMeasure(@RequestParam String name) {
        return new ResponseEntity<>(ingredientService.findAllUnitsOfMeasure(name), HttpStatus.OK);
    }

    @GetMapping("/checkConnectedElements/{id}")
    @ApiOperation("Проверяет наличие связанных элементов по Id")
    public List<String> checkConnectedElements(@PathVariable("id") Long id) {
        return ingredientService.checkConnectedElements(id);
    }
}
