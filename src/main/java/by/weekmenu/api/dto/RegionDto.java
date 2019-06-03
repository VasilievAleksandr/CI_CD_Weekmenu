package by.weekmenu.api.dto;

import by.weekmenu.api.entity.Country;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegionDto {

    private Long id;
    private String name;
    private String countryName;

}
