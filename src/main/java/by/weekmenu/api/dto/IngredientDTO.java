package by.weekmenu.api.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Set;

@NoArgsConstructor
@Getter
@Setter
public class IngredientDTO {

    private Long id;
    private String name;
    private BigDecimal calories;
    private BigDecimal proteins;
    private BigDecimal fats;
    private BigDecimal carbs;
    //key - uom name, Value - equivalent
    private Map<String, BigDecimal> unitOfMeasureEquivalent;
    private Set<IngredientPriceDTO> ingredientPrices;
    private String ingredientCategoryName;
}
