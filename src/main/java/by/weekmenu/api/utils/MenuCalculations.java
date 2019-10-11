package by.weekmenu.api.utils;

import by.weekmenu.api.dto.MenuDTO;
import by.weekmenu.api.dto.MenuRecipeDTO;
import by.weekmenu.api.entity.*;
import by.weekmenu.api.repository.MenuPriceRepository;
import by.weekmenu.api.repository.RecipePriceRepository;
import by.weekmenu.api.repository.RecipeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.DayOfWeek;
import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MenuCalculations {

    private final RecipeRepository recipeRepository;
    private final RecipePriceRepository recipePriceRepository;
    private final MenuPriceRepository menuPriceRepository;

    public void calculateCPFC(MenuDTO menuDTO, Menu menu) {
        Set<MenuRecipeDTO> menuRecipeDTOS = menuDTO.getMenuRecipeDTOS();
        BigDecimal totalCalories = BigDecimal.ZERO;
        BigDecimal totalCarbs = BigDecimal.ZERO;
        BigDecimal totalFats = BigDecimal.ZERO;
        BigDecimal totalProteins = BigDecimal.ZERO;

        //get all days of week, when there are recipes in menu
        Set<DayOfWeek> daysOfWeek = menuRecipeDTOS.stream()
                .map(MenuRecipeDTO::getDayOfWeek).collect(Collectors.toSet());
        BigDecimal daysCount = BigDecimal.valueOf(daysOfWeek.size());

        //sum of calories, proteins, fats and carbs from each recipe which is present in menu
        for (MenuRecipeDTO menuRecipeDTO : menuRecipeDTOS) {
            Optional<Recipe> recipe = recipeRepository.findByNameIgnoreCase(menuRecipeDTO.getRecipeName());
            if (recipe.isPresent()) {
                totalCalories = totalCalories.add(recipe.get().getCalories());
                totalProteins = totalProteins.add(recipe.get().getProteins());
                totalFats = totalFats.add(recipe.get().getFats());
                totalCarbs = totalCarbs.add(recipe.get().getCarbs());
            }
        }

        //daily average sum of calories, proteins, fats and carbs
        menu.setCalories(totalCalories.divide(daysCount, 1, RoundingMode.HALF_UP));
        menu.setProteins(totalProteins.divide(daysCount, 1, RoundingMode.HALF_UP));
        menu.setFats(totalFats.divide(daysCount, 1, RoundingMode.HALF_UP));
        menu.setCarbs(totalCarbs.divide(daysCount, 1, RoundingMode.HALF_UP));
    }

    public void calculateMenuPrice(MenuDTO menuDTO, Menu menu) {
        List<RecipePrice> recipePrices = new ArrayList<>();
        for (MenuRecipeDTO menuRecipeDTO : menuDTO.getMenuRecipeDTOS()) {
            recipeRepository.findByNameIgnoreCase(menuRecipeDTO.getRecipeName()).ifPresent(recipe ->
                recipePrices.addAll(recipePriceRepository.findAllById_RecipeId(recipe.getId()))
            );
        }
        Map<Region, List<RecipePrice>> pricesByRegions = recipePrices.stream()
                .collect(Collectors.groupingBy(RecipePrice::getRegion));
        pricesByRegions.forEach(((region, recipePricesByRegion) -> {
            MenuPrice menuPrice = new MenuPrice();
            menuPrice.setId(new MenuPrice.Id(menu.getId(), region.getId()));
            menuPrice.setMenu(menu);
            menuPrice.setRegion(region);
            menuPrice.setPriceValue(recipePricesByRegion.stream()
                    .map(RecipePrice::getPriceValue).reduce(BigDecimal.ZERO, BigDecimal::add));
            menuPriceRepository.save(menuPrice);
        }));
    }
}
