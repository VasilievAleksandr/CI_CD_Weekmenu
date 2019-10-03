package by.weekmenu.api.controller;

import by.weekmenu.api.dto.MenuDTO;
import by.weekmenu.api.service.MenuService;
import by.weekmenu.api.utils.UrlConsts;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(UrlConsts.PATH_MENUS)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Api(description = "REST API для сущности меню")
public class MenuController {

    private final MenuService menuService;

    @PostMapping
    @ApiOperation("Сохраняет меню")
    public ResponseEntity<MenuDTO> addMenu(@RequestBody MenuDTO menuDTO) {
        return new ResponseEntity<>(menuService.save(menuDTO), HttpStatus.CREATED);
    }
}
