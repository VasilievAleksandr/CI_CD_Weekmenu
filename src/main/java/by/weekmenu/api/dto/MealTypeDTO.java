package by.weekmenu.api.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MealTypeDTO {

    private Short id;
    private String name;
    private Integer priority;
    private boolean isArchived;

}
