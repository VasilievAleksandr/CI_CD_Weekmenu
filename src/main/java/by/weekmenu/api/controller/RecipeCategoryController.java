package by.weekmenu.api.controller;

import by.weekmenu.api.dto.RecipeCategoryDto;
import by.weekmenu.api.service.RecipeCategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/recipecategories")
@Api(description = "REST API для сущности RecipeCategory")
public class RecipeCategoryController {

    private final RecipeCategoryService recipeCategoryService;

    public RecipeCategoryController(RecipeCategoryService recipeCategoryService) {
        this.recipeCategoryService = recipeCategoryService;
    }

    @GetMapping
    @ApiOperation("Возвращает список всех RecipeCategory")
    public List<RecipeCategoryDto> findAllRecipeCategories() {
        return recipeCategoryService.findAll();
    }

    @GetMapping("/{id}")
    @ApiOperation("Находит RecipeCategory по его Id")
    public RecipeCategoryDto findRecipeCategoryById(@PathVariable("id") Long id) {
        return recipeCategoryService.findById(id);
    }

    @PostMapping
    @ApiOperation("Сохраняет RecipeCategory.")
    public RecipeCategoryDto addRecipeCategory(@RequestBody RecipeCategoryDto recipeCategoryDto) {
        return recipeCategoryService.save(recipeCategoryDto);
    }

    @PutMapping("/{id}")
    @ApiOperation("Обновляет RecipeCategory по Id.")
    public RecipeCategoryDto updateRecipeCategory(@RequestBody RecipeCategoryDto updatedRecipeCategoryDto, @PathVariable("id") Long id) {
        RecipeCategoryDto recipeCategoryDto = recipeCategoryService.findById(id);
        if (recipeCategoryDto != null) {
            recipeCategoryDto.setName(updatedRecipeCategoryDto.getName());
        }
        return recipeCategoryService.save(recipeCategoryDto);
    }

    @DeleteMapping("/{id}")
    @ApiOperation("Удаляет RecipeCategory по Id.")
    public void deleteRecipeCategory(@PathVariable("id") Long id) {
        RecipeCategoryDto recipeCategoryDto = recipeCategoryService.findById(id);
        if (recipeCategoryDto != null) {
            recipeCategoryService.delete(id);
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

    @GetMapping("/names")
    @ApiOperation("Возвращает список всех name из RecipeCategory")
    public List<String> getAllRecipeCategoryNames() {
        return recipeCategoryService.getAllRecipeCategoryNames();
    }
}
