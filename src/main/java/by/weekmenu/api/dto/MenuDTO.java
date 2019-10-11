package by.weekmenu.api.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Set;

@NoArgsConstructor
@Data
public class MenuDTO {

    private Long id;
    private String name;
    private String menuCategoryName;
    private String authorName;
    private String authorImageLink;
    private String menuDescription;
    private BigDecimal calories;
    private BigDecimal proteins;
    private BigDecimal fats;
    private BigDecimal carbs;
    private Boolean isActive;
    private String ownershipName;
    private Set<MenuRecipeDTO> menuRecipeDTOS;
    private Set<MenuPriceDTO> menuPriceDTOS;
}
