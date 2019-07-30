package by.weekmenu.api.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Set;

@NoArgsConstructor
@Getter
@Setter
public class RecipeDTO {

    private Long id;
    private String name;
    private String cookingTime;
    private String preparingTime;
    private BigDecimal calories;
    private BigDecimal proteins;
    private BigDecimal fats;
    private BigDecimal carbs;
    private String imageLink;
    private String source;
    private Short portions;
    private String cookingMethodName;
    private String ownershipName;
    private Set<RecipeIngredientDTO> recipeIngredients;
    private Set<CookingStepDTO> cookingSteps;
    private Set<RecipePriceDTO> recipePrices;
}
