package by.weekmenu.api.controller;

import by.weekmenu.api.dto.UnitOfMeasureDto;
import by.weekmenu.api.service.CrudService;
import by.weekmenu.api.service.UnitOfMeasureService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping({"/unitOfMeasures"})
@Api(description = "REST API для сущности UnitOfMeasure")
public class UnitOfMeasureController {

    private final CrudService<UnitOfMeasureDto, Long> crudService;
    private final UnitOfMeasureService unitOfMeasureService;

    @Autowired
    public UnitOfMeasureController(CrudService<UnitOfMeasureDto, Long> crudService, UnitOfMeasureService unitOfMeasureService) {
        this.crudService = crudService;
        this.unitOfMeasureService = unitOfMeasureService;
    }

    @GetMapping
    @ApiOperation("Возвращает список всех UnitOfMeasure")
    public List<UnitOfMeasureDto> findAllUnitOfMeasure() {
        return crudService.findAll();
    }

    @GetMapping("/{id}")
    @ApiOperation("Находит UnitOfMeasure по его Id")
    public UnitOfMeasureDto findUnitOfMeasureById(@PathVariable("id") Long id) {
        return crudService.findById(id);
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
    public UnitOfMeasureDto addUnitOfMeasure(@RequestBody UnitOfMeasureDto unitOfMeasureDTO) {
        return crudService.save(unitOfMeasureDTO);
    }

    @PutMapping("/{id}")
    @ApiOperation("Обновляет UnitOfMeasure по Id.")
    public UnitOfMeasureDto updateUnitOfMeasure(@RequestBody UnitOfMeasureDto unitOfMeasureDTO, @PathVariable("id") Long id) {
        UnitOfMeasureDto newUnitOfMeasureDto = crudService.findById(id);
        if (newUnitOfMeasureDto != null) {
            newUnitOfMeasureDto.setFullName(unitOfMeasureDTO.getFullName());
            newUnitOfMeasureDto.setShortName(unitOfMeasureDTO.getShortName());
        }
        return crudService.save(newUnitOfMeasureDto);
    }

    @DeleteMapping("/{id}")
    @ApiOperation("Удаляет UnitOfMeasure по Id.")
    public void deleteUnitOfMeasureById(@PathVariable("id") Long id) {
        UnitOfMeasureDto newUnitOfMeasureDto = crudService.findById(id);
        if (newUnitOfMeasureDto != null) crudService.delete(id);
    }
}
