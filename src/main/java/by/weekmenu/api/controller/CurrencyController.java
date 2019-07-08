package by.weekmenu.api.controller;

import by.weekmenu.api.dto.CurrencyDto;
import by.weekmenu.api.service.CrudService;
import by.weekmenu.api.service.CurrencyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Api(description = "REST API для сущности Currency")
@RequestMapping({"/currencies"})
public class CurrencyController {

    private final CurrencyService currencyService;

    private final CrudService<CurrencyDto, Integer> currencyCrudService;

    @Autowired
    public CurrencyController(CurrencyService currencyService, CrudService<CurrencyDto, Integer> currencyCrudService) {
        this.currencyService = currencyService;
        this.currencyCrudService = currencyCrudService;
    }

    @GetMapping
    @ApiOperation("Возвращает список всех Currency")
    public List<CurrencyDto> findAllCurrencies() {
        return currencyCrudService.findAll();
    }

    @GetMapping("/{id}")
    @ApiOperation("Находит Currency по его Id")
    public CurrencyDto findCurrencyById(@PathVariable("id") Integer id) {
        return currencyCrudService.findById(id);
    }

    @GetMapping("/checkCurrencyUniqueName")
    @ApiOperation("Проверяет поле name у Currency на уникальность. Возвращает -1, если поле есть в БД и 0, если нет.")
    public Integer checkCurrencyUniqueName(@RequestParam String name) {
        if (currencyService.findByName(name) != null) {
            return -1;
        } else {
            return 0;
        }
    }

    @GetMapping("/checkCurrencyUniqueCode")
    @ApiOperation("Проверяет поле code у Currency на уникальность. Возвращает -1, если поле есть в БД и 0, если нет.")
    public Integer checkCurrencyUniqueCode(@RequestParam String code) {
        if (currencyService.findByCode(code) != null) {
            return -1;
        } else {
            return 0;
        }
    }

    @GetMapping("/checkCurrencyUniqueSymbol")
    @ApiOperation("Проверяет поле symbol у Currency на уникальность. Возвращает -1, если поле есть в БД и 0, если нет.")
    public Integer checkCurrencyUniqueSymbol(@RequestParam String symbol) {
        if (currencyService.findBySymbol(symbol) != null) {
            return -1;
        } else {
            return 0;
        }
    }

    @PostMapping
    @ApiOperation("Сохраняет Currency.")
    public CurrencyDto addCurrency(@RequestBody CurrencyDto currencyDTO) {
        return currencyCrudService.save(currencyDTO);
    }

    @PutMapping("/{id}")
    @ApiOperation("Обновляет Currency по Id.")
    public CurrencyDto updateCurrency(@RequestBody CurrencyDto currencyDTO, @PathVariable("id") Integer id) {
        CurrencyDto newCurrencyDto = currencyCrudService.findById(id);
        if (newCurrencyDto != null) {
            newCurrencyDto.setName(currencyDTO.getName());
            newCurrencyDto.setCode(currencyDTO.getCode());
            newCurrencyDto.setSymbol(currencyDTO.getSymbol());
            newCurrencyDto.setIsActive(currencyDTO.getIsActive());
        }
        return currencyCrudService.save(newCurrencyDto);
    }

    @DeleteMapping("/{id}")
    @ApiOperation("Удаляет Currency по Id.")
    public void deleteCurrencyById(@PathVariable("id") Integer id) {
        CurrencyDto newCurrencyDto = currencyCrudService.findById(id);
        if (newCurrencyDto != null) currencyCrudService.delete(id);
    }

    @GetMapping("/codes")
    @ApiOperation("Возвращает список полей code у активированных Currency ")
    public List<String> getAllCurrencyCodes() {
        return currencyService.getAllCurrencyCodes();
    }

    @GetMapping("/isActive")
    @ApiOperation("Возвращает активированные Currency при true и деактивированные при false")
    public ResponseEntity getAllCurrencyIsActiveIsTrue(@RequestParam Boolean isActive) {
        if (isActive) {
            return ResponseEntity.ok(currencyService.findAllByIsActiveTrueOrderByIsActive());
        }

        return ResponseEntity.ok(currencyService.findAllByIsActiveFalseOrderByIsActive());
    }
}
