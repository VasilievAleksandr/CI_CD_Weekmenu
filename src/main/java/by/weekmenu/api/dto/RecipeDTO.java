package by.weekmenu.api.dto;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Set;

@NoArgsConstructor
@Data
public class RecipeDTO {

    private Long id;
    private String name;
    private String cookingTime;
    private String activeTime;
    private BigDecimal calories;
    private BigDecimal proteins;
    private BigDecimal fats;
    private BigDecimal carbs;
    private String imageLink;
    private String source;
    private Short portions;
    private BigDecimal gramsPerPortion;
    private String cookingMethodName;
    private String ownershipName;
    private Set<RecipeIngredientDTO> recipeIngredients;
    private Set<CookingStepDTO> cookingSteps;
    private Set<RecipePriceDTO> recipePrices;
    private Set<String> categoryNames;
    private Set<String> subcategoryNames;
}
