package by.weekmenu.api.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class MenuPriceDTO {

    private String regionName;
    private String menuName;
    private String priceValue;
    private String currencyCode;
}
