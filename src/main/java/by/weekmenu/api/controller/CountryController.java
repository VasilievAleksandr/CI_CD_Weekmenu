package by.weekmenu.api.controller;

import by.weekmenu.api.dto.CountryDto;
import by.weekmenu.api.service.CountryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/countries")
@Api(description = "REST API для сущности Country")
public class CountryController {
    
    private final CountryService countryService;

    public CountryController(CountryService countryService) {
        this.countryService = countryService;
    }

    @GetMapping
    @ApiOperation("Возвращает список всех Country")
    public List<CountryDto> findAllCountries() {
        return countryService.findAll();
    }

    @GetMapping("/{id}")
    @ApiOperation("Находит Country по его Id")
    public CountryDto findCountryById(@PathVariable("id") Long id) {
        return countryService.findById(id);
    }

    @PostMapping
    @ApiOperation("Сохраняет Country.")
    public CountryDto addCountry(@RequestBody CountryDto countryDto) {
        return countryService.save(countryDto);
    }

    @PutMapping("/{id}")
    @ApiOperation("Обновляет Country по Id.")
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
    @ApiOperation("Удаляет Country по Id.")
    public void deleteCountry(@PathVariable("id") Long id) {
        CountryDto countryDto = countryService.findById(id);
        if (countryDto!=null) {
            countryService.delete(id);
        }
    }
    
    @GetMapping("/checkUniqueName")
    @ApiOperation("Проверяет поле name у Country на уникальность. Возвращает -1, если поле есть в БД и 0, если нет.")
    public Integer checkUniqueName(@RequestParam String name) {
        if (countryService.findByName(name) != null) {
            return -1;
        } else {
            return 0;
        }
    }

    @GetMapping("/checkUniqueAlphaCode2")
    @ApiOperation("Проверяет поле alphaCode2 у Country на уникальность. Возвращает -1, если поле есть в БД и 0, если нет.")
    public Integer checkUniqueCode(@RequestParam String alphaCode2) {
        if (countryService.findByAlphaCode2(alphaCode2) != null) {
            return -1;
        } else {
            return 0;
        }
    }
}
