package by.weekmenu.api.controller;

import by.weekmenu.api.dto.RegionDto;
import by.weekmenu.api.service.RegionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/regions")
public class RegionController {

    private final RegionService regionService;

    @Autowired
    public RegionController(RegionService regionService) {
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

    @GetMapping("/checkUniqueName")
    public Integer checkUniqueName(@RequestParam String name) {
        if(regionService.findByName(name)!=null) {
            return -1;
        } else {
            return 0;
        }
    }

    @GetMapping("/name")
    public RegionDto findRegionByName(@RequestParam String name) {
        return regionService.findByName(name);
    }

}
