package by.weekmenu.api.controller;

import by.weekmenu.api.dto.CategoryDto;
import by.weekmenu.api.service.CategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
@Api(description = "REST API для сущности Category")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    @ApiOperation("Возвращает список всех Category")
    public List<CategoryDto> findAllCategories() {
        return categoryService.findAll();
    }

    @GetMapping("/{id}")
    @ApiOperation("Находит Category по его Id")
    public CategoryDto findCategoryById(@PathVariable("id") Long id) {
        return categoryService.findById(id);
    }

    @PostMapping
    @ApiOperation("Сохраняет Category.")
    public CategoryDto addCategory(@RequestBody CategoryDto categoryDto) {
        return categoryService.save(categoryDto);
    }

    @PutMapping("/{id}")
    @ApiOperation("Обновляет Category по Id.")
    public CategoryDto updateCategory(@RequestBody CategoryDto updatedCategoryDto, @PathVariable("id") Long id) {
        CategoryDto categoryDto = categoryService.findById(id);
        if (categoryDto != null) {
            categoryDto.setName(updatedCategoryDto.getName());
            categoryDto.setDescription(updatedCategoryDto.getDescription());
        }
        return categoryService.save(categoryDto);
    }

    @DeleteMapping("/{id}")
    @ApiOperation("Удаляет Category по Id.")
    public void deleteCategory(@PathVariable("id") Long id) {
        CategoryDto categoryDto = categoryService.findById(id);
        if (categoryDto != null) {
            categoryService.delete(id);
        }
    }

    @GetMapping("/checkCategoryUniqueName")
    @ApiOperation("Проверяет поле name у Category на уникальность. Возвращает -1, если поле есть в БД и 0, если нет.")
    public Integer checkCategoryUniqueName(@RequestParam String name) {
        if (categoryService.findByName(name) != null) {
            return -1;
        } else {
            return 0;
        }
    }

    @GetMapping("/names")
    @ApiOperation("Возвращает список всех name из Country")
    public List<String> getAllCategoryNames() {
        return categoryService.getAllCategoryNames();
    }
}

