package by.weekmenu.api.controller;

import by.weekmenu.api.dto.RecipeCategoryDTO;
import by.weekmenu.api.service.RecipeCategoryService;
import by.weekmenu.api.utils.UrlConsts;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(UrlConsts.PATH_RECIPECATEGORIES)
@Api(description = "REST API для сущности RecipeCategory")
public class RecipeCategoryController {

    private final RecipeCategoryService recipeCategoryService;

    public RecipeCategoryController(RecipeCategoryService recipeCategoryService) {
        this.recipeCategoryService = recipeCategoryService;
    }

    @GetMapping
    @ApiOperation("Возвращает список всех RecipeCategory")
    public ResponseEntity<List<RecipeCategoryDTO>> findAllRecipeCategories() {
        return new ResponseEntity<>(recipeCategoryService.findAll(), HttpStatus.OK);
    }

    @PostMapping
    @ApiOperation("Сохраняет RecipeCategory.")
    public ResponseEntity<RecipeCategoryDTO> addRecipeCategory(@RequestBody RecipeCategoryDTO recipeCategoryDTO) {
        return new ResponseEntity<>(recipeCategoryService.save(recipeCategoryDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @ApiOperation("Обновляет RecipeCategory по Id.")
    public ResponseEntity<RecipeCategoryDTO> updateRecipeCategory(@RequestBody RecipeCategoryDTO updatedRecipeCategoryDTO, @PathVariable("id") Long id) {
        RecipeCategoryDTO recipeCategoryDTO = recipeCategoryService.findById(id);
        if (recipeCategoryDTO != null) {
            recipeCategoryDTO.setName(updatedRecipeCategoryDTO.getName());
            return new ResponseEntity<>(recipeCategoryService.save(recipeCategoryDTO), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    @ApiOperation("Перемещает в корзину RecipeCategory по Id.")
    public ResponseEntity<Void> deleteRecipeCategory(@PathVariable("id") Long id) {
        RecipeCategoryDTO recipeCategoryDTO = recipeCategoryService.findById(id);
        if (recipeCategoryDTO != null) {
            recipeCategoryService.moveToRecycleBin(recipeCategoryDTO);
            return ResponseEntity.noContent().build();
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/checkRecipeCategoryUniqueName")
    @ApiOperation("Проверяет поле name у RecipeCategory на уникальность. Возвращает -1, если поле есть в БД и 0, если нет.")
    public Integer checkRecipeCategoryUniqueName(@RequestParam String name) {
        if (recipeCategoryService.findByName(name) != null) {
            return -1;
        } else {
            return 0;
        }
    }

    @GetMapping("/checkConnectedElements/{id}")
    @ApiOperation("Проверяет наличие связанных элементов по Id")
    public List<String> checkConnectedElements(@PathVariable("id") Long id) {
        return recipeCategoryService.checkConnectedElements(id);
    }
}
