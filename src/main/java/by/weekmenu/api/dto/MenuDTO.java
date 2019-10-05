package by.weekmenu.api.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

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
    private Boolean isActive;
    private String ownershipName;
    private Set<MenuRecipeDTO> menuRecipeDTOS;
}
