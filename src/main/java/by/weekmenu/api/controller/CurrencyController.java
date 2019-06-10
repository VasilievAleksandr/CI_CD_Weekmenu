package by.weekmenu.api.controller;

import by.weekmenu.api.dto.CurrencyDto;
import by.weekmenu.api.service.CrudService;
import by.weekmenu.api.service.CurrencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
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
    public List<CurrencyDto> findAllCurrencies() {
        return currencyCrudService.findAll();
    }

    @GetMapping("/{id}")
    public CurrencyDto findCurrencyById(@PathVariable("id") Integer id) {
        return currencyCrudService.findById(id);
    }

    @GetMapping("/checkCurrencyUniqueName")
    public Integer checkCurrencyUniqueName(@RequestParam String name) {
        if (currencyService.findByName(name) != null) {
            return -1;
        } else {
            return 0;
        }
    }

    @GetMapping("/checkCurrencyUniqueCode")
    public Integer checkCurrencyUniqueCode(@RequestParam String code) {
        if (currencyService.findByCode(code) != null) {
            return -1;
        } else {
            return 0;
        }
    }

    @GetMapping("/checkCurrencyUniqueSymbol")
    public Integer checkCurrencyUniqueSymbol(@RequestParam String symbol) {
        if (currencyService.findBySymbol(symbol) != null) {
            return -1;
        } else {
            return 0;
        }
    }

    @PostMapping
    public CurrencyDto addCurrency(@RequestBody CurrencyDto currencyDTO) {
        return currencyCrudService.save(currencyDTO);
    }

    @PutMapping("/{id}")
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
    public void deleteCurrencyById(@PathVariable("id") Integer id) {
        CurrencyDto newCurrencyDto = currencyCrudService.findById(id);
        if (newCurrencyDto != null) currencyCrudService.delete(id);
    }

    @GetMapping("/codes")
    public List<String> getAllCurrencyCodes() {
        return currencyService.getAllCurrencyCodes();
    }

    @GetMapping("/isActive")
    public ResponseEntity getAllCurrencyIsActiveIsTrue(@RequestParam Boolean isActive) {
        if (isActive) {
            return ResponseEntity.ok(currencyService.findAllByIsActiveTrueOrderByIsActive());
        }

        return ResponseEntity.ok(currencyService.findAllByIsActiveFalseOrderByIsActive());
    }
}
