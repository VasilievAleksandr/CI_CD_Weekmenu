package by.weekmenu.api.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class CookingStepDTO {

    private Integer id;
    private Integer priority;
    private String description;
    private String imageLink;
}
