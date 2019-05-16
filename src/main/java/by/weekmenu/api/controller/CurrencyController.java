package by.weekmenu.api.controller;

import by.weekmenu.api.dto.CurrencyDto;
import by.weekmenu.api.service.CurrencyServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping({"/currencies"})
public class CurrencyController {

    private final CurrencyServiceImp currencyService;

    @Autowired
    public CurrencyController(CurrencyServiceImp currencyService) {
        this.currencyService = currencyService;
    }

    @GetMapping
    public List<CurrencyDto> findAllCurrencies() {
        return currencyService.findAll();
    }

    @GetMapping("/{id}")
    public CurrencyDto findCurrencyById(@PathVariable("id") Byte id) {
        return currencyService.findById(id);
    }

    @PostMapping
    public CurrencyDto addCurrency(@RequestBody CurrencyDto currencyDTO) {
        return currencyService.save(currencyDTO);
    }

    @PutMapping("/{id}")
    public CurrencyDto updateCurrency(@RequestBody CurrencyDto currencyDTO, @PathVariable("id") Byte id) {
        CurrencyDto newCurrencyDto = currencyService.findById(id);
        if (newCurrencyDto != null) newCurrencyDto.setName(currencyDTO.getName());
        return currencyService.save(currencyDTO);
    }

    @DeleteMapping("/{id}")
    public void deleteCurrencyById(@PathVariable("id") Byte id) {
        CurrencyDto newCurrencyDto = currencyService.findById(id);
        if (newCurrencyDto != null) currencyService.delete(id);
    }
}
