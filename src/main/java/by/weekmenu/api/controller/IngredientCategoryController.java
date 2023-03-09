package by.weekmenu.api.controller;

import by.weekmenu.api.dto.IngredientCategoryDTO;
import by.weekmenu.api.service.IngredientCategoryService;
import by.weekmenu.api.utils.UrlConsts;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(UrlConsts.PATH_INGREDIENT_CATEGORIES)
@Api(description = "REST API для сущности IngredientCategory")
public class IngredientCategoryController {

    private final IngredientCategoryService ingredientCategoryService;

    public IngredientCategoryController(IngredientCategoryService ingredientCategoryService) {
        this.ingredientCategoryService = ingredientCategoryService;
    }

    @GetMapping
    @ApiOperation("Возвращает список всех IngredientCategory")
    public ResponseEntity<List<IngredientCategoryDTO>> findAllIngredientCategories() {
        return new ResponseEntity<>(ingredientCategoryService.findAll(), HttpStatus.OK);
    }

    @PostMapping
    @ApiOperation("Сохраняет IngredientCategory.")
    public ResponseEntity<IngredientCategoryDTO> addIngredientCategory(@RequestBody IngredientCategoryDTO ingredientCategoryDTO) {
        return new ResponseEntity<>(ingredientCategoryService.save(ingredientCategoryDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @ApiOperation("Обновляет IngredientCategory по Id.")
    public ResponseEntity<IngredientCategoryDTO> updateIngredientCategory(@RequestBody IngredientCategoryDTO updatedIngredientCategoryDTO,
                                                                          @PathVariable("id") Integer id) {
        IngredientCategoryDTO ingredientCategoryDTO = ingredientCategoryService.findById(id);
        if (ingredientCategoryDTO != null) {
            ingredientCategoryDTO.setName(updatedIngredientCategoryDTO.getName());
            ingredientCategoryDTO.setImageLink(updatedIngredientCategoryDTO.getImageLink());
            ingredientCategoryDTO.setPriority(updatedIngredientCategoryDTO.getPriority());
            return new ResponseEntity<>(ingredientCategoryService.save(ingredientCategoryDTO), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    @ApiOperation("Перемещает в корзину IngredientCategory по Id.")
    public ResponseEntity<Void> deleteIngredientCategory(@PathVariable("id") Integer id) {
        IngredientCategoryDTO ingredientCategoryDTO = ingredientCategoryService.findById(id);
        if (ingredientCategoryDTO != null) {
            ingredientCategoryService.moveToRecycleBin(ingredientCategoryDTO);
            return ResponseEntity.noContent().build();
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/checkConnectedElements/{id}")
    @ApiOperation("Проверяет наличие связанных элементов по Id")
    public List<String> checkConnectedElements(@PathVariable("id") Integer id) {
        return ingredientCategoryService.checkConnectedElements(id);
    }
}
