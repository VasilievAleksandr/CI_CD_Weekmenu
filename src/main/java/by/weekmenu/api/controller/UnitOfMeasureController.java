package by.weekmenu.api.controller;

import by.weekmenu.api.dto.UnitOfMeasureDTO;
import by.weekmenu.api.service.UnitOfMeasureService;
import by.weekmenu.api.utils.UrlConsts;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(UrlConsts.PATH_UOM)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Api(description = "REST API для сущности UnitOfMeasure")
public class UnitOfMeasureController {

    private final UnitOfMeasureService unitOfMeasureService;

    @GetMapping
    @ApiOperation("Возвращает список всех UnitOfMeasure")
    public ResponseEntity<List<UnitOfMeasureDTO>> findAllUnitOfMeasure() {
        return new ResponseEntity<>(unitOfMeasureService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/checkUniqueShortName")
    @ApiOperation("Проверяет поле shortName у UnitOfMeasure на уникальность. Возвращает -1, если поле есть в БД и 0, если нет.")
    public Integer checkUniqueShortName(@RequestParam String shortName) {
        if (unitOfMeasureService.findByShortName(shortName) != null) {
            return -1;
        } else {
            return 0;
        }
    }

    @GetMapping("/checkUniqueFullName")
    @ApiOperation("Проверяет поле fullName у UnitOfMeasure на уникальность. Возвращает -1, если поле есть в БД и 0, если нет.")
    public Integer checkUniqueFullName(@RequestParam String fullName) {
        if (unitOfMeasureService.findByFullName(fullName) != null) {
            return -1;
        } else {
            return 0;
        }
    }

    @PostMapping
    @ApiOperation("Сохраняет UnitOfMeasure.")
    public ResponseEntity<UnitOfMeasureDTO> addUnitOfMeasure(@RequestBody UnitOfMeasureDTO unitOfMeasureDTO) {
        return new ResponseEntity<>(unitOfMeasureService.save(unitOfMeasureDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @ApiOperation("Обновляет UnitOfMeasure по Id.")
    public ResponseEntity<UnitOfMeasureDTO> updateUnitOfMeasure(@RequestBody UnitOfMeasureDTO unitOfMeasureDTO, @PathVariable("id") Long id) {
        UnitOfMeasureDTO newUnitOfMeasureDTO = unitOfMeasureService.findById(id);
        if (newUnitOfMeasureDTO != null) {
            newUnitOfMeasureDTO.setFullName(unitOfMeasureDTO.getFullName());
            newUnitOfMeasureDTO.setShortName(unitOfMeasureDTO.getShortName());
        }
        return new ResponseEntity<>(unitOfMeasureService.save(newUnitOfMeasureDTO), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @ApiOperation("Перемещает в корзину UnitOfMeasure по Id.")
    public ResponseEntity<Void> deleteUnitOfMeasureById(@PathVariable("id") Long id) {
        UnitOfMeasureDTO unitOfMeasureDTO = unitOfMeasureService.findById(id);
        if (unitOfMeasureDTO != null) {
            unitOfMeasureService.moveToRecycleBin(unitOfMeasureDTO);
            return ResponseEntity.noContent().build();
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/checkConnectedElements/{id}")
    @ApiOperation("Проверяет наличие связанных элементов по Id")
    public List<String> checkConnectedElements(@PathVariable("id") Long id) {
        return unitOfMeasureService.checkConnectedElements(id);
    }
}
