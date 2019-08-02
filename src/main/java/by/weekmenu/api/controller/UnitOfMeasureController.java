package by.weekmenu.api.controller;

import by.weekmenu.api.dto.UnitOfMeasureDTO;
import by.weekmenu.api.service.UnitOfMeasureService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping({"/unitOfMeasures"})
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Api(description = "REST API для сущности UnitOfMeasure")
public class UnitOfMeasureController {
    
    private final UnitOfMeasureService unitOfMeasureService;

    @GetMapping
    @ApiOperation("Возвращает список всех UnitOfMeasure")
    public List<UnitOfMeasureDTO> findAllUnitOfMeasure() {
        return unitOfMeasureService.findAll();
    }

    @GetMapping("/{id}")
    @ApiOperation("Находит UnitOfMeasure по его Id")
    public UnitOfMeasureDTO findUnitOfMeasureById(@PathVariable("id") Long id) {
        return unitOfMeasureService.findById(id);
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
    public UnitOfMeasureDTO addUnitOfMeasure(@RequestBody UnitOfMeasureDTO unitOfMeasureDTO) {
        return unitOfMeasureService.save(unitOfMeasureDTO);
    }

    @PutMapping("/{id}")
    @ApiOperation("Обновляет UnitOfMeasure по Id.")
    public UnitOfMeasureDTO updateUnitOfMeasure(@RequestBody UnitOfMeasureDTO unitOfMeasureDTO, @PathVariable("id") Long id) {
        UnitOfMeasureDTO newUnitOfMeasureDTO = unitOfMeasureService.findById(id);
        if (newUnitOfMeasureDTO != null) {
            newUnitOfMeasureDTO.setFullName(unitOfMeasureDTO.getFullName());
            newUnitOfMeasureDTO.setShortName(unitOfMeasureDTO.getShortName());
        }
        return unitOfMeasureService.save(newUnitOfMeasureDTO);
    }

    @DeleteMapping("/{id}")
    @ApiOperation("Перемещает в корзину UnitOfMeasure по Id.")
    public void deleteUnitOfMeasureById(@PathVariable("id") Long id) {
        unitOfMeasureService.delete(id);
    }

    @GetMapping("/checkConnectedElements/{id}")
    @ApiOperation("Проверяет наличие связанных элементов по Id")
    public List<String> checkConnectedElements(@PathVariable("id") Long id) {
        return unitOfMeasureService.checkConnectedElements(id);
    }
}
