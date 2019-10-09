package by.weekmenu.api.utils;

import by.weekmenu.api.dto.MenuDTO;
import by.weekmenu.api.dto.MenuRecipeDTO;
import by.weekmenu.api.entity.Menu;
import by.weekmenu.api.entity.Recipe;
import by.weekmenu.api.repository.RecipeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;
import java.util.Set;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MenuCalculations {

    private final RecipeRepository recipeRepository;

    public void calculateCPFC(MenuDTO menuDTO, Menu menu) {
        Set<MenuRecipeDTO> menuRecipeDTOS = menuDTO.getMenuRecipeDTOS();
        BigDecimal totalCalories = BigDecimal.ZERO;
        BigDecimal totalCarbs= BigDecimal.ZERO;
        BigDecimal totalFats= BigDecimal.ZERO;
        BigDecimal totalProteins= BigDecimal.ZERO;
        for (MenuRecipeDTO menuRecipeDTO : menuRecipeDTOS) {
            Optional<Recipe> recipe = recipeRepository.findByNameIgnoreCase(menuRecipeDTO.getRecipeName());
            if (recipe.isPresent()) {
                totalCalories = totalCalories.add(recipe.get().getCalories())
                        .setScale(1, BigDecimal.ROUND_HALF_UP);
                totalProteins = totalProteins.add(recipe.get().getProteins())
                        .setScale(1, BigDecimal.ROUND_HALF_UP);
                totalFats = totalFats.add(recipe.get().getFats())
                        .setScale(1, BigDecimal.ROUND_HALF_UP);
                totalCarbs = totalCarbs.add(recipe.get().getCarbs())
                        .setScale(1, BigDecimal.ROUND_HALF_UP);
            }
        }
        BigDecimal recipesCount = new BigDecimal(menuRecipeDTOS.size());
        menu.setCalories(totalCalories.divide(recipesCount, 1, RoundingMode.HALF_UP));
        menu.setProteins(totalProteins.divide(recipesCount, 1, RoundingMode.HALF_UP));
        menu.setFats(totalFats.divide(recipesCount, 1, RoundingMode.HALF_UP));
        menu.setCarbs(totalCarbs.divide(recipesCount, 1, RoundingMode.HALF_UP));
    }
}
