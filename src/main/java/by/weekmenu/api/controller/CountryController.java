package by.weekmenu.api.controller;

import by.weekmenu.api.dto.CountryDto;
import by.weekmenu.api.service.CountryService;
import by.weekmenu.api.service.CrudService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/countries")
public class CountryController {

    private final CrudService<CountryDto, Long> crudService;
    private final CountryService countryService;

    public CountryController(CrudService<CountryDto, Long> crudService, CountryService countryService) {
        this.crudService = crudService;
        this.countryService = countryService;
    }

    @GetMapping
    public List<CountryDto> findAllCountries() {
        return crudService.findAll();
    }

    @GetMapping("/{id}")
    public CountryDto findCountryById(@PathVariable("id") Long id) {
        return crudService.findById(id);
    }

    @PostMapping
    public CountryDto addCountry(@RequestBody CountryDto countryDto) {
        return crudService.save(countryDto);
    }

    @PutMapping("/{id}")
    public CountryDto updateCountry(@RequestBody CountryDto updatedCountryDto, @PathVariable("id") Long id) {
        CountryDto countryDto = crudService.findById(id);
        if (countryDto!=null) {
            countryDto.setName(updatedCountryDto.getName());
            countryDto.setAlphaCode2(updatedCountryDto.getAlphaCode2());
            countryDto.setCurrencyCode(updatedCountryDto.getCurrencyCode());
        }
        return crudService.save(countryDto);
    }

    @DeleteMapping("/{id}")
    public void deleteCountry(@PathVariable("id") Long id) {
        CountryDto countryDto = crudService.findById(id);
        if (countryDto!=null) {
            crudService.delete(id);
        }
    }
    
    @GetMapping("/checkUniqueName")
    public Integer checkUniqueName(@RequestParam String name) {
        if (countryService.findByName(name) != null) {
            return -1;
        } else {
            return 0;
        }
    }

    @GetMapping("/checkUniqueAlphaCode2")
    public Integer checkUniqueCode(@RequestParam String alphaCode2) {
        if (countryService.findByAlphaCode2(alphaCode2) != null) {
            return -1;
        } else {
            return 0;
        }
    }
}
