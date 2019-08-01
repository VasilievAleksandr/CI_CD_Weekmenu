package by.weekmenu.api.controller;

import by.weekmenu.api.dto.RecycleBinDTO;
import by.weekmenu.api.service.RecycleBinService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/recycleBin")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Api(description = "REST API для сущности Recycle Bin")
public class RecycleBinController {

    private final RecycleBinService recycleBinService;

    @PutMapping("/{id}")
    @ApiOperation("Восстанавливает запись из корзины по Id.")
    public void restoreElement(@PathVariable("id") Long id) {
        recycleBinService.restoreElement(id);
    }

    @GetMapping
    @ApiOperation("Возвращает список всех элементов в Корзине")
    public List<RecycleBinDTO> findAllBins() {
        return recycleBinService.findAll();
    }

    @DeleteMapping("/{id}")
    @ApiOperation("Удаляет запись из корзины по Id.")
    public void deleteElement(@PathVariable("id") Long id) {
        if (recycleBinService.findById(id)!=null) {
            recycleBinService.deleteElement(id);
        }
    }
}
