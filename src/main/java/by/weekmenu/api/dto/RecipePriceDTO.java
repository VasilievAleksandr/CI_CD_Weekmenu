package by.weekmenu.api.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class RecipePriceDTO {

    private String regionName;
    private String recipeName;
    private String priceValue;
    private String currencyCode;
}
