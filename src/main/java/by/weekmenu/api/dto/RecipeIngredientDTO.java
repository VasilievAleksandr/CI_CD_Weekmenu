package by.weekmenu.api.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@NoArgsConstructor
@Data
public class RecipeIngredientDTO {

    private String ingredientName;
    private BigDecimal quantity;
    private String unitOfMeasureShortName;
}
