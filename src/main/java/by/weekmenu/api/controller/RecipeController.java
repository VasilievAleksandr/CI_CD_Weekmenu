package by.weekmenu.api.controller;

import by.weekmenu.api.dto.RecipeDTO;
import by.weekmenu.api.service.RecipeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/recipes")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Api(description = "REST API для сущности рецепт")
public class RecipeController {

    private final RecipeService recipeService;
    private final ModelMapper modelMapper;

    @GetMapping
    @ApiOperation("Возвращает список всех рецептов")
    public ResponseEntity<List<RecipeDTO>> findAllRecipes() {
        return new ResponseEntity<>(recipeService.findAll(), HttpStatus.OK);
    }

    @PostMapping
    @ApiOperation("Сохраняет рецепт")
    public ResponseEntity<RecipeDTO> addRecipe(@RequestBody RecipeDTO recipeDto) {
        return new ResponseEntity<>(recipeService.save(recipeDto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @ApiOperation("Обновляет рецепт по Id")
    public ResponseEntity<RecipeDTO> updateRecipe(@RequestBody RecipeDTO updatedRecipeDTO, @PathVariable ("id") Long id) {
        RecipeDTO recipeDto = recipeService.findById(id);
        if (recipeDto!=null) {
            return new ResponseEntity<>(recipeService.save(modelMapper.map(updatedRecipeDTO, RecipeDTO.class)), HttpStatus.OK);
        } else {
            return new ResponseEntity<>( HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    @ApiOperation("Перемещает в корзину рецепт по Id")
    public ResponseEntity<Void> deleteRecipe(@PathVariable("id") Long id) {
        RecipeDTO recipeDto = recipeService.findById(id);
        if (recipeDto!=null) {
            recipeService.moveToRecycleBin(recipeDto);
            return ResponseEntity.noContent().build();
        } else {
            return new ResponseEntity<>( HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/checkUniqueName")
    @ApiOperation("Проверяет поле name у рецепта на уникальность. Возвращает -1, если поле есть в БД и 0, если нет.")
    public Integer checkUniqueName(@RequestParam String name) {
        if (recipeService.findByName(name) != null) {
            return -1;
        } else {
            return 0;
        }
    }

    @GetMapping("/checkConnectedElements/{id}")
    @ApiOperation("Проверяет наличие связанных элементов по Id")
    public List<String> checkConnectedElements(@PathVariable("id") Long id) {
        return recipeService.checkConnectedElements(id);
    }
}
