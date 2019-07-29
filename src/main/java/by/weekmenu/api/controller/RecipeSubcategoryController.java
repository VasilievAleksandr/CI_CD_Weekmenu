package by.weekmenu.api.controller;

import by.weekmenu.api.dto.RecipeSubcategoryDto;
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
    public List<RecipeSubcategoryDto> findAllReecipeSubcategories() {
        return recipeSubcategoryService.findAll();
    }

    @PostMapping
    @ApiOperation("Сохраняет RecipeSubcategory.")
    public ResponseEntity<RecipeSubcategoryDto> addRecipeSubcategory(@RequestBody RecipeSubcategoryDto recipeSubcategoryDto) {
        HttpStatus status = HttpStatus.CREATED;
        return new ResponseEntity<>(recipeSubcategoryService.save(recipeSubcategoryDto), status);
    }

    @GetMapping("/{id}")
    @ApiOperation("Находит RecipeSubcategory по его Id")
    public RecipeSubcategoryDto findRecipeSubcategoryById(@PathVariable("id") Long id) {
        return recipeSubcategoryService.findById(id);
    }

    @PutMapping("/{id}")
    @ApiOperation("Обновляет RecipeSubcategory по Id.")
    public RecipeSubcategoryDto updateRecipeSubcategory(@RequestBody RecipeSubcategoryDto updatedRecipeSubcategoryDto, @PathVariable("id") Long id) {
        RecipeSubcategoryDto recipeSubcategoryDto = recipeSubcategoryService.findById(id);
        if (recipeSubcategoryDto != null) {
            recipeSubcategoryDto.setName(updatedRecipeSubcategoryDto.getName());
        }
        return recipeSubcategoryService.save(recipeSubcategoryDto);
    }

    @DeleteMapping("/{id}")
    @ApiOperation("Удаляет RecipeSubcategory по Id.")
    public void deleteRecipeSubcategory(@PathVariable("id") Long id) {
        RecipeSubcategoryDto recipeSubcategoryDto = recipeSubcategoryService.findById(id);
        if (recipeSubcategoryDto != null) {
            recipeSubcategoryService.delete(id);
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
