package by.weekmenu.api.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.DayOfWeek;

@NoArgsConstructor
@Data
public class MenuRecipeDTO {

    private String recipeName;
    private String mealTypeName;
    private String dayOfWeekName;
    private DayOfWeek dayOfWeek;
}
