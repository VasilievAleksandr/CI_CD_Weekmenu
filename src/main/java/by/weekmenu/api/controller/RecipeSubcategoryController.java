package by.weekmenu.api.controller;

import by.weekmenu.api.dto.RecipeSubcategoryDTO;
import by.weekmenu.api.service.RecipeSubcategoryService;
import by.weekmenu.api.utils.UrlConsts;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(UrlConsts.PATH_RECIPESUBCATEGORIES)
@Api(description = "REST API для сущности RecipeSubcategory")
public class RecipeSubcategoryController {

    private final RecipeSubcategoryService recipeSubcategoryService;

    @Autowired
    public RecipeSubcategoryController(RecipeSubcategoryService recipeSubcategoryService) {
        this.recipeSubcategoryService = recipeSubcategoryService;
    }

    @GetMapping
    @ApiOperation("Возвращает список всех RecipeSubcategory")
    public ResponseEntity<List<RecipeSubcategoryDTO>> findAllRecipeSubcategories() {
        return new ResponseEntity<>(recipeSubcategoryService.findAll(), HttpStatus.OK);
    }

    @PostMapping
    @ApiOperation("Сохраняет RecipeSubcategory.")
    public ResponseEntity<RecipeSubcategoryDTO> addRecipeSubcategory(@RequestBody RecipeSubcategoryDTO recipeSubcategoryDTO) {
        HttpStatus status = HttpStatus.CREATED;
        return new ResponseEntity<>(recipeSubcategoryService.save(recipeSubcategoryDTO), status);
    }

    @PutMapping("/{id}")
    @ApiOperation("Обновляет RecipeSubcategory по Id.")
    public ResponseEntity<RecipeSubcategoryDTO> updateRecipeSubcategory(@RequestBody RecipeSubcategoryDTO updatedRecipeSubcategoryDTO, @PathVariable("id") Long id) {
        RecipeSubcategoryDTO recipeSubcategoryDTO = recipeSubcategoryService.findById(id);
        if (recipeSubcategoryDTO != null) {
            recipeSubcategoryDTO.setName(updatedRecipeSubcategoryDTO.getName());
            return new ResponseEntity<>(recipeSubcategoryService.save(recipeSubcategoryDTO), HttpStatus.OK);
        }
        else return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    @ApiOperation("Перемещает в корзину RecipeSubcategory по Id.")
    public ResponseEntity<Void> deleteRecipeSubcategory(@PathVariable("id") Long id) {
        RecipeSubcategoryDTO recipeSubcategoryDTO = recipeSubcategoryService.findById(id);
        if (recipeSubcategoryDTO != null) {
            recipeSubcategoryService.moveToRecycleBin(recipeSubcategoryDTO);
            return ResponseEntity.noContent().build();
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/checkRecipeSubcategoryUniqueName")
    @ApiOperation("Проверяет поле name у RecipeSubcategory на уникальность. Возвращает -1, если поле есть в БД и 0, если нет.")
    public Integer checkRecipeSubcategoryUniqueName(@RequestParam String name) {
        if (recipeSubcategoryService.findByName(name) != null) {
            return -1;
        } else {
            return 0;
        }
    }

    @GetMapping("/checkConnectedElements/{id}")
    @ApiOperation("Проверяет наличие связанных элементов по Id")
    public List<String> checkConnectedElements(@PathVariable("id") Long id) {
        return recipeSubcategoryService.checkConnectedElements(id);
    }
}
