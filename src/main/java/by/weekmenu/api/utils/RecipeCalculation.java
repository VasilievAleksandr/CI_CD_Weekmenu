package by.weekmenu.api.utils;

import by.weekmenu.api.dto.RecipeDTO;
import by.weekmenu.api.dto.RecipeIngredientDTO;
import by.weekmenu.api.entity.*;
import by.weekmenu.api.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RecipeCalculation {

    private final IngredientPriceRepository ingredientPriceRepository;
    private final IngredientRepository ingredientRepository;
    private final UnitOfMeasureRepository unitOfMeasureRepository;
    private final IngredientUnitOfMeasureRepository ingredientUnitOfMeasureRepository;
    private final RecipePriceRepository recipePriceRepository;

    public void calculateRecipePrice(RecipeDTO recipeDto, Recipe recipe) {
        Set<RecipeIngredientDTO> recipeIngredients = recipeDto.getRecipeIngredients();
        Set<IngredientPrice> allIngredientPrices = getIngredientPrices(recipeIngredients);
        createRecipePrice(recipe, recipeIngredients, allIngredientPrices);
    }

    private void createRecipePrice(Recipe recipe,
                                   Set<RecipeIngredientDTO> recipeIngredients,
                                   Set<IngredientPrice> allIngredientPrices) {
        List<IngredientPrice> list = new ArrayList<>(allIngredientPrices);
        Map<Region, List<IngredientPrice>> lists = list.stream().collect(Collectors.groupingBy(IngredientPrice::getRegion));
        lists.forEach(((region, ingredientPrices) -> {
            RecipePrice recipePrice = new RecipePrice();
            recipePrice.setId(new RecipePrice.Id(recipe.getId(), region.getId()));
            recipePrice.setRegion(region);
            recipePrice.setRecipe(recipe);
            BigDecimal priceValue = BigDecimal.ZERO;
            priceValue = calculatePrice(recipeIngredients, ingredientPrices, priceValue);
            //recipe price per portion
            recipePrice.setPriceValue(priceValue.divide(new BigDecimal(recipe.getPortions()), 2, BigDecimal.ROUND_HALF_UP));
            recipePriceRepository.save(recipePrice);
        }));
    }

    private BigDecimal calculatePrice(Set<RecipeIngredientDTO> recipeIngredients,
                                      List<IngredientPrice> ingredientPrices,
                                      BigDecimal priceValue) {
        for (IngredientPrice ingredientPrice : ingredientPrices) {
            //count price depends on uom and quantity
            for (RecipeIngredientDTO recipeIngredientDTO : recipeIngredients) {
                if (recipeIngredientDTO.getIngredientName().equals(ingredientPrice.getIngredient().getName())) {
                    if (recipeIngredientDTO.getUnitOfMeasureShortName().equals(ingredientPrice.getUnitOfMeasure().getShortName())) {
                        priceValue = priceValue
                                .add(recipeIngredientDTO.getQuantity().multiply(ingredientPrice.getPriceValue())
                                        .divide(ingredientPrice.getQuantity(), 2, BigDecimal.ROUND_HALF_UP));
                    } else {
                        Optional<UnitOfMeasure> byShortNameUOM = unitOfMeasureRepository.findByShortNameIgnoreCase(recipeIngredientDTO.getUnitOfMeasureShortName());
                        Optional<IngredientUnitOfMeasure> ingredientUnitOfMeasure = ingredientUnitOfMeasureRepository
                                .findById(new IngredientUnitOfMeasure.Id(ingredientPrice.getIngredient().getId(),
                                        byShortNameUOM.get().getId()));
                        if (ingredientUnitOfMeasure.isPresent()) {
                            priceValue = priceValue
                                    .add(recipeIngredientDTO.getQuantity().multiply(ingredientUnitOfMeasure.get().getEquivalent())
                                            .multiply(ingredientPrice.getPriceValue())
                                            .divide(ingredientPrice.getQuantity(), 2, BigDecimal.ROUND_HALF_UP));
                        }
                    }
                }
            }
        }
        return priceValue;
    }

    private Set<IngredientPrice> getIngredientPrices(Set<RecipeIngredientDTO> recipeIngredients) {
        Set<IngredientPrice> allIngredientPrices = new HashSet<>();
        for (RecipeIngredientDTO recipeIngredientDTO : recipeIngredients) {
            Set<IngredientPrice> ingredientPrices = ingredientPriceRepository.findAllById_IngredientId(ingredientRepository
                    .findByNameIgnoreCase(recipeIngredientDTO.getIngredientName()).get().getId());
            allIngredientPrices.addAll(ingredientPrices);
        }
        return allIngredientPrices;
    }

    public void calculateCPFC(RecipeDTO recipeDto, Recipe recipe) {
        Set<RecipeIngredientDTO> recipeIngredients = recipeDto.getRecipeIngredients();
        BigDecimal totalCalories = BigDecimal.ZERO;
        BigDecimal totalCarbs= BigDecimal.ZERO;
        BigDecimal totalFats= BigDecimal.ZERO;
        BigDecimal totalProteins= BigDecimal.ZERO;
        for (RecipeIngredientDTO recipeIngredientDTO : recipeIngredients) {
            Optional<Ingredient> ingredient = ingredientRepository.findByNameIgnoreCase(recipeIngredientDTO.getIngredientName());
            Optional<UnitOfMeasure> unitOfMeasure = unitOfMeasureRepository.findByShortNameIgnoreCase(recipeIngredientDTO.getUnitOfMeasureShortName());
            if (ingredient.isPresent() && unitOfMeasure.isPresent()) {
                Optional<IngredientUnitOfMeasure> ingredientUnitOfMeasure = ingredientUnitOfMeasureRepository.findById_IngredientIdAndId_UnitOfMeasureId(ingredient.get().getId(),
                        unitOfMeasure.get().getId());
                if (ingredientUnitOfMeasure.isPresent()) {
                    BigDecimal rate = ingredientUnitOfMeasure.get().getEquivalent()
                            .divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP)
                            .multiply(recipeIngredientDTO.getQuantity())
                            .divide(new BigDecimal(recipe.getPortions()), 2, BigDecimal.ROUND_HALF_UP);

                    totalCalories = totalCalories
                            .add(ingredient.get().getCalories().multiply(rate))
                            .setScale(0, BigDecimal.ROUND_HALF_UP);
                    totalProteins = totalProteins
                            .add(ingredient.get().getProteins().multiply(rate))
                            .setScale(1, BigDecimal.ROUND_HALF_UP);
                    totalCarbs = totalCarbs
                            .add(ingredient.get().getCarbs().multiply(rate))
                            .setScale(1, BigDecimal.ROUND_HALF_UP);
                    totalFats = totalFats
                            .add(ingredient.get().getFats().multiply(rate))
                            .setScale(1, BigDecimal.ROUND_HALF_UP);
                }
            }
        }
        recipe.setCalories(totalCalories);
        recipe.setCarbs(totalCarbs);
        recipe.setFats(totalFats);
        recipe.setProteins(totalProteins);
    }

    public void calculateGramsPerPortion(RecipeDTO recipeDTO, Recipe recipe) {
        Set<RecipeIngredientDTO> recipeIngredients = recipeDTO.getRecipeIngredients();
        BigDecimal totalGrams = BigDecimal.ZERO;
        for (RecipeIngredientDTO recipeIngredientDTO : recipeIngredients) {
            Optional<Ingredient> ingredient = ingredientRepository.findByNameIgnoreCase(recipeIngredientDTO.getIngredientName());
            Optional<UnitOfMeasure> unitOfMeasure = unitOfMeasureRepository.findByShortNameIgnoreCase(recipeIngredientDTO.getUnitOfMeasureShortName());
            if (ingredient.isPresent() && unitOfMeasure.isPresent()) {
                Optional<IngredientUnitOfMeasure> ingredientUnitOfMeasure = ingredientUnitOfMeasureRepository.findById_IngredientIdAndId_UnitOfMeasureId(ingredient.get().getId(),
                        unitOfMeasure.get().getId());
                if (ingredientUnitOfMeasure.isPresent()) {
                    totalGrams = totalGrams.add(ingredientUnitOfMeasure.get().getEquivalent()
                            .multiply(recipeIngredientDTO.getQuantity()))
                            .setScale(1, BigDecimal.ROUND_HALF_UP);
                }
            }
        }
        recipe.setGramsPerPortion(totalGrams.divide(new BigDecimal(recipe.getPortions()),
                1, RoundingMode.HALF_UP));
    }
}
