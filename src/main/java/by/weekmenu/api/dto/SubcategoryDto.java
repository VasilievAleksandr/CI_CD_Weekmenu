package by.weekmenu.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SubcategoryDto {

    private Long id;
    private String name;
    private String categoryName;
    private String description;

}
