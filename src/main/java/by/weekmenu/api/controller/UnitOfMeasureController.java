package by.weekmenu.api.controller;

import by.weekmenu.api.dto.UnitOfMeasureDto;
import by.weekmenu.api.service.CrudService;
import by.weekmenu.api.service.UnitOfMeasureServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping({"/unitOfMeasures"})
public class UnitOfMeasureController {

    private final CrudService<UnitOfMeasureDto, Long> unitOfMeasureService;

    @Autowired
    public UnitOfMeasureController(UnitOfMeasureServiceImp unitOfMeasureService) {
        this.unitOfMeasureService = unitOfMeasureService;
    }

    @GetMapping
    public List<UnitOfMeasureDto> findAllUnitOfMeasure() {
        return unitOfMeasureService.findAll();
    }

    @GetMapping("/{id}")
    public UnitOfMeasureDto findUnitOfMeasureById(@PathVariable("id") Long id) {
        return unitOfMeasureService.findById(id);
    }

    @PostMapping
    public UnitOfMeasureDto addUnitOfMeasure(@RequestBody UnitOfMeasureDto unitOfMeasureDTO) {
        return unitOfMeasureService.save(unitOfMeasureDTO);
    }

    @PutMapping("/{id}")
    public UnitOfMeasureDto updateUnitOfMeasure(@RequestBody UnitOfMeasureDto unitOfMeasureDTO, @PathVariable("id") Long id) {
        UnitOfMeasureDto newUnitOfMeasureDto = unitOfMeasureService.findById(id);
        if (newUnitOfMeasureDto != null) newUnitOfMeasureDto.setName(unitOfMeasureDTO.getName());
        return unitOfMeasureService.save(newUnitOfMeasureDto);
    }

    @DeleteMapping("/{id}")
    public void deleteUnitOfMeasureById(@PathVariable("id") Long id) {
        UnitOfMeasureDto newUnitOfMeasureDto = unitOfMeasureService.findById(id);
        if (newUnitOfMeasureDto != null) unitOfMeasureService.delete(id);
    }
}
