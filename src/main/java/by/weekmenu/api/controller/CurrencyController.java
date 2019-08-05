package by.weekmenu.api.controller;

import by.weekmenu.api.dto.CurrencyDTO;
import by.weekmenu.api.service.CurrencyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Api(description = "REST API для сущности Currency")
@RequestMapping({"/currencies"})
public class CurrencyController {

    private final CurrencyService currencyService;
    private final ModelMapper modelMapper;

    @Autowired
    public CurrencyController(CurrencyService currencyService, ModelMapper modelMapper) {
        this.currencyService = currencyService;
        this.modelMapper = modelMapper;
    }

    @GetMapping
    @ApiOperation("Возвращает список всех Currency")
    public List<CurrencyDTO> findAllCurrencies() {
        return currencyService.findAll();
    }

    @GetMapping("/{id}")
    @ApiOperation("Находит Currency по его Id")
    public CurrencyDTO findCurrencyById(@PathVariable("id") Integer id) {
        return currencyService.findById(id);
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

    @PostMapping
    @ApiOperation("Сохраняет Currency.")
    public CurrencyDTO addCurrency(@RequestBody CurrencyDTO currencyDTO) {
        return currencyService.save(currencyDTO);
    }

    @PutMapping("/{id}")
    @ApiOperation("Обновляет Currency по Id.")
    public ResponseEntity<CurrencyDTO> updateCurrency(@RequestBody CurrencyDTO currencyDTO, @PathVariable("id") Integer id) {
        CurrencyDTO newCurrencyDTO = currencyService.findById(id);
        if (newCurrencyDTO != null) {
            return new ResponseEntity<>(currencyService.save(modelMapper.map(currencyDTO, CurrencyDTO.class)),
                    HttpStatus.OK);
        } else {
            return new ResponseEntity<>( HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    @ApiOperation("Удаляет Currency по Id.")
    public void deleteCurrencyById(@PathVariable("id") Integer id) {
        currencyService.delete(id);
    }

    @GetMapping("/codes")
    @ApiOperation("Возвращает список полей code у активированных Currency ")
    public List<String> getAllCurrencyCodes() {
        return currencyService.getAllCurrencyCodes();
    }

    @GetMapping("/checkConnectedElements/{id}")
    @ApiOperation("Проверяет наличие связанных элементов по Id")
    public List<String> checkConnectedElements(@PathVariable("id") Integer id) {
        return currencyService.checkConnectedElements(id);
    }
}
