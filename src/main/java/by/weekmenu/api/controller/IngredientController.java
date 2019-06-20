package by.weekmenu.api.controller;

import by.weekmenu.api.dto.IngredientDto;
import by.weekmenu.api.service.IngredientService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ingredients")
public class IngredientController {

    private final IngredientService ingredientService;

    public IngredientController(IngredientService ingredientService) {
        this.ingredientService = ingredientService;
    }

    @GetMapping
    public List<IngredientDto> findAllIngredients() {
        return ingredientService.findAll();
    }

    @GetMapping("/{id}")
    public IngredientDto findIngredientById(@PathVariable("id") Long id) {
        return ingredientService.findById(id);
    }

    @PostMapping
    public ResponseEntity<IngredientDto> addIngredient(@RequestBody IngredientDto ingredientDto) {
        return new ResponseEntity<>(ingredientService.save(ingredientDto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public IngredientDto updateIngredient(@RequestBody IngredientDto updatedIngredientDto, @PathVariable ("id") Long id) {
        IngredientDto ingredientDto = ingredientService.findById(id);
        if (ingredientDto!=null) {
            ingredientDto.setName(updatedIngredientDto.getName());
            ingredientDto.setCalories(updatedIngredientDto.getCalories());
            ingredientDto.setCarbs(updatedIngredientDto.getCarbs());
            ingredientDto.setFats(updatedIngredientDto.getFats());
            ingredientDto.setProteins(updatedIngredientDto.getProteins());
            ingredientDto.setUnitOfMeasureEquivalent(updatedIngredientDto.getUnitOfMeasureEquivalent());
        }
        return ingredientService.save(ingredientDto);
    }

    @DeleteMapping("/{id}")
    public void deleteIngredient(@PathVariable("id") Long id) {
        IngredientDto ingredientDto = ingredientService.findById(id);
        if (ingredientDto!=null) {
            ingredientService.delete(id);
        }
    }

    @GetMapping("/checkUniqueName")
    public Integer checkUniqueName(@RequestParam String name) {
        if (ingredientService.findByName(name) != null) {
            return -1;
        } else {
            return 0;
        }
    }
}
