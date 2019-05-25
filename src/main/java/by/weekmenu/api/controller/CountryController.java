package by.weekmenu.api.controller;

import by.weekmenu.api.dto.CountryDto;
import by.weekmenu.api.service.CrudService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/countries")
public class CountryController {

    private final CrudService<CountryDto, Long> service;

    public CountryController(CrudService<CountryDto, Long> service) {
        this.service = service;
    }
    
    @GetMapping
    public List<CountryDto> findAllCountries() {
        return service.findAll();
    }
    
    @GetMapping("/{id}")
    public CountryDto findCountryById(@PathVariable("id") Long id) {
        return service.findById(id);
    }

    @PostMapping
    public CountryDto addCountry(@RequestBody CountryDto countryDto) {
        return service.save(countryDto);
    }
    
    @PutMapping("/{id}")
    public CountryDto updateCountry(@RequestBody CountryDto updatedCountryDto, @PathVariable("id") Long id) {
        CountryDto countryDto = service.findById(id);
        if (countryDto!=null) {
            countryDto.setName(updatedCountryDto.getName());
            countryDto.setAlphaCode2(updatedCountryDto.getAlphaCode2());
            countryDto.setCurrencyCode(updatedCountryDto.getCurrencyCode());
        }
        return service.save(countryDto);
    }

    @DeleteMapping("/{id}")
    public void deleteCountry(@PathVariable("id") Long id) {
        CountryDto countryDto = service.findById(id);
        if (countryDto!=null) {
            service.delete(id);
        }
    }
}
