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
    public ResponseEntity<List<CurrencyDTO>> findAllCurrencies() {
        return new ResponseEntity<>(currencyService.findAll(), HttpStatus.OK);
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
    public ResponseEntity<CurrencyDTO> addCurrency(@RequestBody CurrencyDTO currencyDTO) {
        return new ResponseEntity<>(currencyService.save(currencyDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @ApiOperation("Обновляет Currency по Id.")
    public ResponseEntity<CurrencyDTO> updateCurrency(@RequestBody CurrencyDTO currencyDTO, @PathVariable("id") Integer id) {
        CurrencyDTO newCurrencyDTO = currencyService.findById(id);
        if (newCurrencyDTO != null) {
            return new ResponseEntity<>(currencyService.save(currencyDTO),
                    HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    @ApiOperation("Перемещает в корзину Currency по Id.")
    public ResponseEntity<Void> deleteCurrencyById(@PathVariable("id") Integer id) {
        CurrencyDTO currencyDTO = currencyService.findById(id);
        if (currencyDTO != null) {
            currencyService.delete(currencyDTO.getId());
            return ResponseEntity.noContent().build();
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/checkConnectedElements/{id}")
    @ApiOperation("Проверяет наличие связанных элементов по Id")
    public List<String> checkConnectedElements(@PathVariable("id") Integer id) {
        return currencyService.checkConnectedElements(id);
    }
}
