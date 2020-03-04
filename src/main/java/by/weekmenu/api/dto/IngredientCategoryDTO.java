package by.weekmenu.api.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class IngredientCategoryDTO {

    private Integer id;
    private String name;
    private Integer priority;
    private String imageLink;
    private boolean isArchived;

}
