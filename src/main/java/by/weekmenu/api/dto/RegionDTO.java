package by.weekmenu.api.dto;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegionDTO {

    private Long id;
    private String name;
    private String countryName;
    private String countryCurrencyCode;

    @Override
    @JsonValue
    public String toString() {
        return  name + " / " +
                countryName + " / " +
                countryCurrencyCode;
    }
}
