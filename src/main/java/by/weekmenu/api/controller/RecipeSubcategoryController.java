package by.weekmenu.api.controller;

import by.weekmenu.api.dto.RecipeSubcategoryDTO;
import by.weekmenu.api.service.RecipeSubcategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/recipesubcategories")
@Api(description = "REST API для сущности Recipesubcategory")
public class RecipeSubcategoryController {

    private final RecipeSubcategoryService recipeSubcategoryService;

    @Autowired
    public RecipeSubcategoryController(RecipeSubcategoryService recipeSubcategoryService) {
        this.recipeSubcategoryService = recipeSubcategoryService;
    }

    @GetMapping
    @ApiOperation("Возвращает список всех RecipeSubcategory")
    public ResponseEntity<List<RecipeSubcategoryDTO>> findAllReecipeSubcategories() {
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
            return new ResponseEntity<>(recipeSubcategoryDTO, HttpStatus.OK);
        }
        else return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    @ApiOperation("Удаляет RecipeSubcategory по Id.")
    public ResponseEntity<Void> deleteRecipeSubcategory(@PathVariable("id") Long id) {
        RecipeSubcategoryDTO recipeSubcategoryDTO = recipeSubcategoryService.findById(id);
        if (recipeSubcategoryDTO != null) {
            recipeSubcategoryService.delete(id);
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

    @GetMapping("/names")
    @ApiOperation("Возвращает список всех name из RecipeSubcategory")
    public List<String> getAllRecipeCategoryNames() {
        return recipeSubcategoryService.getAllRecipeSubcategoryNames();
    }
}
