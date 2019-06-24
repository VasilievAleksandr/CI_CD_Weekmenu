package by.weekmenu.api.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@NoArgsConstructor
@Data
public class IngredientPriceDTO {

    private String regionName;
    private BigDecimal priceValue;
    private String unitOfMeasureName;
    private BigDecimal quantity;
    private String currencyCode;
}
