package by.weekmenu.api.controller;

import by.weekmenu.api.dto.RegionDto;
import by.weekmenu.api.service.RegionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/regions")
@Api(description = "REST API для сущности Region")
public class RegionController {

    private final RegionService regionService;

    @Autowired
    public RegionController(RegionService regionService) {
        this.regionService = regionService;
    }

    @GetMapping
    @ApiOperation("Возвращает список всех Region")
    public List<RegionDto> findAllRegions() {
        return regionService.findAll();
    }

    @PostMapping
    @ApiOperation("Сохраняет Region.")
    public ResponseEntity<RegionDto> addRegion(@RequestBody RegionDto regionDto) {
        HttpStatus status = HttpStatus.CREATED;
        return new ResponseEntity<>(regionService.save(regionDto), status);
    }

    @GetMapping("/{id}")
    @ApiOperation("Находит Region по его Id")
    public RegionDto findRegionById(@PathVariable ("id") Long id) {
        return regionService.findById(id);
    }

    @PutMapping("/{id}")
    @ApiOperation("Обновляет Region по Id.")
    public RegionDto updateRegion(@RequestBody RegionDto updatedRegionDto, @PathVariable("id")Long id) {
        RegionDto  regionDto = regionService.findById(id);
        if (regionDto!=null) {
            regionDto.setName(updatedRegionDto.getName());
            regionDto.setCountryName(updatedRegionDto.getCountryName());
        }
        return regionService.save(regionDto);
    }

    @DeleteMapping("/{id}")
    @ApiOperation("Удаляет Region по Id.")
    public void deleteRegion(@PathVariable ("id") Long id) {
        RegionDto regionDto = regionService.findById(id);
        if (regionDto!=null) {
            regionService.delete(id);
        }
    }

    @GetMapping("/checkUniqueName")
    @ApiOperation("Проверяет поле name у Region на уникальность. Возвращает -1, если поле есть в БД и 0, если нет.")
    public Integer checkUniqueName(@RequestParam String name) {
        if(regionService.findByName(name)!=null) {
            return -1;
        } else {
            return 0;
        }
    }
}
