package by.weekmenu.api.controller;

import by.weekmenu.api.dto.CookingMethodDTO;
import by.weekmenu.api.service.CookingMethodService;
import by.weekmenu.api.utils.UrlConsts;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(UrlConsts.PATH_COOKINGMETHODS)
@Api(description = "REST API для сущности CookingMethod")
public class CookingMethodController {

    private final CookingMethodService cookingMethodService;

    public CookingMethodController(CookingMethodService cookingMethodService) {
        this.cookingMethodService = cookingMethodService;
    }

    @GetMapping
    @ApiOperation("Возвращает список всех CookingMethod")
    public ResponseEntity<List<CookingMethodDTO>> findAllCookingMethods() {
        return new ResponseEntity<>(cookingMethodService.findAll(), HttpStatus.OK);
    }

    @PostMapping
    @ApiOperation("Сохраняет CookingMethod.")
    public ResponseEntity<CookingMethodDTO> addCookingMethod(@RequestBody CookingMethodDTO cookingMethodDTO) {
        return new ResponseEntity<>(cookingMethodService.save(cookingMethodDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @ApiOperation("Обновляет CookingMethod по Id.")
    public ResponseEntity<CookingMethodDTO> updateCookingMethod (@RequestBody CookingMethodDTO updatedCookingMethodDTO,
                                                                 @PathVariable("id") Integer id) {
        CookingMethodDTO cookingMethodDTO = cookingMethodService.findById(id);
        if (cookingMethodDTO != null) {
            cookingMethodDTO.setName(updatedCookingMethodDTO.getName());
            return new ResponseEntity<>(cookingMethodService.save(cookingMethodDTO), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    @ApiOperation("Перемещает в корзину CookingMethod по Id.")
    public ResponseEntity<Void> deleteCookingMethod (@PathVariable("id") Integer id) {
        CookingMethodDTO cookingMethodDTO = cookingMethodService.findById(id);
        if (cookingMethodDTO != null) {
            cookingMethodService.moveToRecycleBin(cookingMethodDTO);
            return ResponseEntity.noContent().build();
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/checkCookingMethodUniqueName")
    @ApiOperation("Проверяет поле name у CookingMethod на уникальность. Возвращает -1, если поле есть в БД и 0, если нет.")
    public Integer checkCookingMethodUniqueName(@RequestParam String name) {
        if (cookingMethodService.findByName(name) != null) {
            return -1;
        } else {
            return 0;
        }
    }

    @GetMapping("/checkConnectedElements/{id}")
    @ApiOperation("Проверяет наличие связанных элементов по Id")
    public List<String> checkConnectedElements(@PathVariable("id") Integer id) {
        return cookingMethodService.checkConnectedElements(id);
    }
}
