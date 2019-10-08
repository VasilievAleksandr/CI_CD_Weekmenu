package by.weekmenu.api.controller;

import by.weekmenu.api.dto.MenuDTO;
import by.weekmenu.api.service.MenuService;
import by.weekmenu.api.utils.UrlConsts;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(UrlConsts.PATH_MENUS)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Api(description = "REST API для сущности меню")
public class MenuController {

    private final MenuService menuService;
    private final ModelMapper modelMapper;

    @PostMapping
    @ApiOperation("Сохраняет меню")
    public ResponseEntity<MenuDTO> addMenu(@RequestBody MenuDTO menuDTO) {
        return new ResponseEntity<>(menuService.save(menuDTO), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @ApiOperation("Возвращает меню по id")
    public ResponseEntity<MenuDTO> getMenu(@PathVariable ("id") Long id) {
        MenuDTO menuDTO = menuService.findById(id);
        if (menuDTO!=null) {
            return new ResponseEntity<>(menuDTO, HttpStatus.FOUND);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{id}")
    @ApiOperation("Обновляет menu по Id")
    public ResponseEntity<MenuDTO> updateMenu(@RequestBody MenuDTO updatedMenuDTO, @PathVariable ("id") Long id) {
        MenuDTO menuDTO = menuService.findById(id);
        if (menuDTO!=null) {
            return new ResponseEntity<>(menuService.save(modelMapper.map(updatedMenuDTO, MenuDTO.class)), HttpStatus.OK);
        } else {
            return new ResponseEntity<>( HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    @ApiOperation("Перемещает в корзину меню по Id")
    public ResponseEntity<Void> deleteMenu(@PathVariable("id") Long id) {
        MenuDTO menuDTO = menuService.findById(id);
        if (menuDTO!=null) {
            menuService.moveToRecycleBin(menuDTO);
            return ResponseEntity.noContent().build();
        } else {
            return new ResponseEntity<>( HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/checkConnectedElements/{id}")
    @ApiOperation("Проверяет наличие связанных элементов по Id")
    public List<String> checkConnectedElements(@PathVariable("id") Long id) {
        return menuService.checkConnectedElements(id);
    }
}
