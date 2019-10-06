package by.weekmenu.api.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.DayOfWeek;

@NoArgsConstructor
@Data
public class MenuRecipeDTO {

    private Long id;
    private String recipeName;
    private String mealTypeName;
    private DayOfWeek dayOfWeek;
    private String recipeImageLink;
}
