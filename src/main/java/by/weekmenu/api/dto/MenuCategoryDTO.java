package by.weekmenu.api.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MenuCategoryDTO {

    private Integer id;
    private String name;
    private Integer priority;
    private String imageLink;
    private boolean isArchived;

}
