package by.weekmenu.api.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CountryDTO {

    private Long id;
    private String name;
    private String alphaCode2;
    private String currencyCode;
}
