package by.weekmenu.api.controller;

import by.weekmenu.api.dto.BaseUnitOfMeasureDto;
import by.weekmenu.api.service.CrudService;
import by.weekmenu.api.service.BaseUnitOfMeasureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping({"/unitOfMeasures"})
public class BaseUnitOfMeasureController {

    private final CrudService<BaseUnitOfMeasureDto, Long> crudService;
    private final BaseUnitOfMeasureService baseUnitOfMeasureService;

    @Autowired
    public BaseUnitOfMeasureController(CrudService<BaseUnitOfMeasureDto, Long> crudService, BaseUnitOfMeasureService baseUnitOfMeasureService) {
        this.crudService = crudService;
        this.baseUnitOfMeasureService = baseUnitOfMeasureService;
    }

    @GetMapping
    public List<BaseUnitOfMeasureDto> findAllUnitOfMeasure() {
        return crudService.findAll();
    }

    @GetMapping("/{id}")
    public BaseUnitOfMeasureDto findUnitOfMeasureById(@PathVariable("id") Long id) {
        return crudService.findById(id);
    }
    
    @GetMapping("/checkUniqueShortName")
    public Integer checkUniqueShortName(@RequestParam String shortName) {
        if (baseUnitOfMeasureService.findByShortName(shortName) != null) {
            return -1;
        } else {
            return 0;
        }
    }

    @GetMapping("/checkUniqueFullName")
    public Integer checkUniqueFullName(@RequestParam String fullName) {
        if (baseUnitOfMeasureService.findByFullName(fullName) != null) {
            return -1;
        } else {
            return 0;
        }
    }

    @PostMapping
    public BaseUnitOfMeasureDto addUnitOfMeasure(@RequestBody BaseUnitOfMeasureDto baseUnitOfMeasureDTO) {
        return crudService.save(baseUnitOfMeasureDTO);
    }

    @PutMapping("/{id}")
    public BaseUnitOfMeasureDto updateUnitOfMeasure(@RequestBody BaseUnitOfMeasureDto baseUnitOfMeasureDTO, @PathVariable("id") Long id) {
        BaseUnitOfMeasureDto newBaseUnitOfMeasureDto = crudService.findById(id);
        if (newBaseUnitOfMeasureDto != null) {
            newBaseUnitOfMeasureDto.setFullName(baseUnitOfMeasureDTO.getFullName());
            newBaseUnitOfMeasureDto.setShortName(baseUnitOfMeasureDTO.getShortName());
        }
        return crudService.save(newBaseUnitOfMeasureDto);
    }

    @DeleteMapping("/{id}")
    public void deleteUnitOfMeasureById(@PathVariable("id") Long id) {
        BaseUnitOfMeasureDto newBaseUnitOfMeasureDto = crudService.findById(id);
        if (newBaseUnitOfMeasureDto != null) crudService.delete(id);
    }
}
