package by.weekmenu.api.controller;

import by.weekmenu.api.dto.MenuCategoryDTO;
import by.weekmenu.api.service.MenuCategoryService;
import by.weekmenu.api.utils.UrlConsts;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(UrlConsts.PATH_MENU_CATEGORIES)
@Api(description = "REST API для сущности MenuCategory")
public class MenuCategoryController {

    private final MenuCategoryService menuCategoryService;

    public MenuCategoryController(MenuCategoryService menuCategoryService) {
        this.menuCategoryService = menuCategoryService;
    }

    @GetMapping
    @ApiOperation("Возвращает список всех MenuCategory")
    public ResponseEntity<List<MenuCategoryDTO>> findAllMenuCategories() {
        return new ResponseEntity<>(menuCategoryService.findAll(), HttpStatus.OK);
    }

    @PostMapping
    @ApiOperation("Сохраняет MenuCategory.")
    public ResponseEntity<MenuCategoryDTO> addMenuCategory(@RequestBody MenuCategoryDTO menuCategoryDTO) {
        return new ResponseEntity<>(menuCategoryService.save(menuCategoryDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @ApiOperation("Обновляет MenuCategory по Id.")
    public ResponseEntity<MenuCategoryDTO> updateMenuCategory(@RequestBody MenuCategoryDTO updatedMenuCategoryDTO, @PathVariable("id") Integer id) {
        MenuCategoryDTO menuCategoryDTO = menuCategoryService.findById(id);
        if (menuCategoryDTO != null) {
            menuCategoryDTO.setName(updatedMenuCategoryDTO.getName());
            menuCategoryDTO.setImageLink(updatedMenuCategoryDTO.getImageLink());
            menuCategoryDTO.setPriority(updatedMenuCategoryDTO.getPriority());
            return new ResponseEntity<>(menuCategoryService.save(menuCategoryDTO), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    @ApiOperation("Перемещает в корзину MenuCategory по Id.")
    public ResponseEntity<Void> deleteMenuCategory(@PathVariable("id") Integer id) {
        MenuCategoryDTO menuCategoryDTO = menuCategoryService.findById(id);
        if (menuCategoryDTO != null) {
            menuCategoryService.moveToRecycleBin(menuCategoryDTO);
            return ResponseEntity.noContent().build();
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/checkMenuCategoryUniqueName")
    @ApiOperation("Проверяет поле name у MenuCategory на уникальность. Возвращает -1, если поле есть в БД и 0, если нет.")
    public Integer checkMenuCategoryUniqueName(@RequestParam String name) {
        if (menuCategoryService.findByName(name) != null) {
            return -1;
        } else {
            return 0;
        }
    }

    @GetMapping("/checkConnectedElements/{id}")
    @ApiOperation("Проверяет наличие связанных элементов по Id")
    public List<String> checkConnectedElements(@PathVariable("id") Integer id) {
        return menuCategoryService.checkConnectedElements(id);
    }
}
