package by.weekmenu.api.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RecipeCategoryDTO {

    private Long id;
    private String name;
    private String priority;

}
