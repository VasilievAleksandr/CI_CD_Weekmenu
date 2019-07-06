package by.weekmenu.api.controller;

import by.weekmenu.api.dto.CountryDto;
import by.weekmenu.api.service.CountryService;
import by.weekmenu.api.service.CrudService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/countries")
public class CountryController {
    
    private final CountryService countryService;

    public CountryController(CountryService countryService) {
        this.countryService = countryService;
    }

    @GetMapping
    public List<CountryDto> findAllCountries() {
        return countryService.findAll();
    }

    @GetMapping("/{id}")
    public CountryDto findCountryById(@PathVariable("id") Long id) {
        return countryService.findById(id);
    }

    @PostMapping
    public CountryDto addCountry(@RequestBody CountryDto countryDto) {
        return countryService.save(countryDto);
    }

    @PutMapping("/{id}")
    public CountryDto updateCountry(@RequestBody CountryDto updatedCountryDto, @PathVariable("id") Long id) {
        CountryDto countryDto = countryService.findById(id);
        if (countryDto!=null) {
            countryDto.setName(updatedCountryDto.getName());
            countryDto.setAlphaCode2(updatedCountryDto.getAlphaCode2());
            countryDto.setCurrencyCode(updatedCountryDto.getCurrencyCode());
        }
        return countryService.save(countryDto);
    }

    @DeleteMapping("/{id}")
    public void deleteCountry(@PathVariable("id") Long id) {
        CountryDto countryDto = countryService.findById(id);
        if (countryDto!=null) {
            countryService.delete(id);
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

    @GetMapping("/names")
    public List<String> getAllCountryNames() {
        return countryService.getAllCountryNames();
    }
}
