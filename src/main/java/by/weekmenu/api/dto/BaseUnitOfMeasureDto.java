package by.weekmenu.api.dto;

import by.weekmenu.api.entity.BaseUnitOfMeasure;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class BaseUnitOfMeasureDto {

    private Long id;

    private String fullName;

    private String shortName;

    public BaseUnitOfMeasureDto(BaseUnitOfMeasure baseUnitOfMeasure) {
        this.id = baseUnitOfMeasure.getId();
        this.fullName = baseUnitOfMeasure.getFullName();
        this.shortName = baseUnitOfMeasure.getShortName();
    }
}
