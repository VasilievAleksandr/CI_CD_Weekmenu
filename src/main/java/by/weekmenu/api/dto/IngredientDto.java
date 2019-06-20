package by.weekmenu.api.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
public class IngredientDto {

    private Long id;
    private String name;
    private Integer calories;
    private Integer proteins;
    private Integer fats;
    private Integer carbs;
    //key - uom name, Value - equivalent
    private Map<String, BigDecimal> unitOfMeasureEquivalent;
}
