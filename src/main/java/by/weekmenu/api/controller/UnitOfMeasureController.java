package by.weekmenu.api.controller;

import by.weekmenu.api.dto.UnitOfMeasureDto;
import by.weekmenu.api.service.UnitOfMeasureServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping({"/unitOfMeasure"})
public class UnitOfMeasureController {

    @Autowired
    UnitOfMeasureServiceImp unitOfMeasureService;

    @GetMapping
    public List<UnitOfMeasureDto> findAllUnitOfMeasure() {
        return unitOfMeasureService.findAll();
    }

    @GetMapping("{id}")
    public UnitOfMeasureDto findUnitOfMeasureById(@PathVariable("id") Long id) {
        return unitOfMeasureService.findById(id);
    }

    @PostMapping
    public List<UnitOfMeasureDto> addUnitOfMeasure(@RequestBody UnitOfMeasureDto unitOfMeasureDTO) {
        unitOfMeasureService.save(unitOfMeasureDTO);
        return unitOfMeasureService.findAll();
    }

    @PutMapping("{id}")
    public List<UnitOfMeasureDto> updateUnitOfMeasure(@RequestBody UnitOfMeasureDto unitOfMeasureDTO) {
        unitOfMeasureService.update(unitOfMeasureDTO);
        return unitOfMeasureService.findAll();
    }

    @DeleteMapping("{id}")
    public List<UnitOfMeasureDto> deleteUnitOfMeasureById(@PathVariable("id") Long id) {
        unitOfMeasureService.delete(id);
        return unitOfMeasureService.findAll();
    }
}

