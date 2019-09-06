package by.weekmenu.api.controller;

import by.weekmenu.api.dto.CountryDTO;
import by.weekmenu.api.service.CountryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<List<CountryDTO>> findAllCountries() {
        return new ResponseEntity<>(countryService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @ApiOperation("Находит Country по его Id")
    public ResponseEntity<CountryDTO> findCountryById(@PathVariable("id") Long id) {
        return new ResponseEntity<>(countryService.findById(id), HttpStatus.OK);
    }

    @PostMapping
    @ApiOperation("Сохраняет Country.")
    public ResponseEntity<CountryDTO> addCountry(@RequestBody CountryDTO countryDto) {
        return new ResponseEntity<>(countryService.save(countryDto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @ApiOperation("Обновляет Country по Id.")
    public ResponseEntity<CountryDTO> updateCountry(@RequestBody CountryDTO updatedCountryDTO, @PathVariable("id") Long id) {
        CountryDTO countryDto = countryService.findById(id);
        if (countryDto != null) {
            countryDto.setName(updatedCountryDTO.getName());
            countryDto.setAlphaCode2(updatedCountryDTO.getAlphaCode2());
            countryDto.setCurrencyCode(updatedCountryDTO.getCurrencyCode());
            return new ResponseEntity<>(countryService.save(countryDto), HttpStatus.OK);
        } else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    @ApiOperation("Перемещает в корзину Country по Id.")
    public ResponseEntity<Void> deleteCountry(@PathVariable("id") Long id) {
        CountryDTO countryDTO = countryService.findById(id);
        if (countryDTO != null) {
            countryService.moveToRecycleBin(countryDTO);
            return ResponseEntity.noContent().build();
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
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

    @GetMapping("/checkConnectedElements/{id}")
    @ApiOperation("Проверяет наличие связанных элементов по Id")
    public List<String> checkConnectedElements(@PathVariable("id") Long id) {
        return countryService.checkConnectedElements(id);
    }
}
