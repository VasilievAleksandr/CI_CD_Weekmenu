package by.weekmenu.api.controller;

import by.weekmenu.api.dto.RegionDto;
import by.weekmenu.api.service.CrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/regions")
public class RegionController {

    private final CrudService<RegionDto, Long> regionService;

    @Autowired
    public RegionController(CrudService<RegionDto, Long> regionService) {
        this.regionService = regionService;
    }

    @GetMapping
    public List<RegionDto> findAllRegions() {
        return regionService.findAll();
    }

    @PostMapping
    public ResponseEntity<RegionDto> addRegion(@RequestBody RegionDto regionDto) {
        HttpStatus status = HttpStatus.CREATED;
        return new ResponseEntity<>(regionService.save(regionDto), status);
    }

    @GetMapping("/{id}")
    public RegionDto findRegionById(@PathVariable ("id") Long id) {
        return regionService.findById(id);
    }

    @PutMapping("/{id}")
    public RegionDto updateRegion(@RequestBody RegionDto updatedRegionDto, @PathVariable("id")Long id) {
        RegionDto  regionDto = regionService.findById(id);
        if (regionDto!=null) {
            regionDto.setName(updatedRegionDto.getName());
            regionDto.setCountryName(updatedRegionDto.getCountryName());
        }
        return regionService.save(regionDto);
    }

    @DeleteMapping("/{id}")
    public void deleteRegion(@PathVariable ("id") Long id) {
        RegionDto regionDto = regionService.findById(id);
        if (regionDto!=null) {
            regionService.delete(id);
        }
    }
}
