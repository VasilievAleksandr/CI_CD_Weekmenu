package by.weekmenu.api.controller;

import by.weekmenu.api.dto.SubcategoryDto;
import by.weekmenu.api.service.SubcategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/subcategories")
@Api(description = "REST API для сущности Subcategory")
public class SubcategoryController {

    private final SubcategoryService subcategoryService;

    @Autowired
    public SubcategoryController(SubcategoryService subcategoryService) {
        this.subcategoryService = subcategoryService;
    }

    @GetMapping
    @ApiOperation("Возвращает список всех Subcategory")
    public List<SubcategoryDto> findAllSubcategories() {
        return subcategoryService.findAll();
    }

    @PostMapping
    @ApiOperation("Сохраняет Subcategory.")
    public ResponseEntity<SubcategoryDto> addSubcategory(@RequestBody SubcategoryDto subcategoryDto) {
        HttpStatus status = HttpStatus.CREATED;
        return new ResponseEntity<>(subcategoryService.save(subcategoryDto), status);
    }

    @GetMapping("/{id}")
    @ApiOperation("Находит Subcategory по его Id")
    public SubcategoryDto findSubcategoryById(@PathVariable ("id") Long id) {
        return subcategoryService.findById(id);
    }

    @PutMapping("/{id}")
    @ApiOperation("Обновляет Subcategory по Id.")
    public SubcategoryDto updateSubcategory (@RequestBody SubcategoryDto updatedSubcategoryDto, @PathVariable("id")Long id) {
        SubcategoryDto  subcategoryDto = subcategoryService.findById(id);
        if (subcategoryDto!=null) {
            subcategoryDto.setName(updatedSubcategoryDto.getName());
            subcategoryDto.setCategoryName(updatedSubcategoryDto.getCategoryName());
            subcategoryDto.setDescription(updatedSubcategoryDto.getDescription());
        }
        return subcategoryService.save(subcategoryDto);
    }

    @DeleteMapping("/{id}")
    @ApiOperation("Удаляет Subcategory по Id.")
    public void deleteSubcategory(@PathVariable ("id") Long id) {
        SubcategoryDto subcategoryDto = subcategoryService.findById(id);
        if (subcategoryDto!=null) {
            subcategoryService.delete(id);
        }
    }

    @GetMapping("/checkUniqueSubcategoryName")
    @ApiOperation("Проверяет поле name у Subcategory на уникальность. Возвращает -1, если поле есть в БД и 0, если нет.")
    public Integer checkUniqueSubcategoryName(@RequestParam String name) {
        if(subcategoryService.findByName(name)!=null) {
            return -1;
        } else {
            return 0;
        }
    }

    @GetMapping("/name")
    public SubcategoryDto findSubcategoryByName(@RequestParam String name) {
        return subcategoryService.findByName(name);
    }
}
