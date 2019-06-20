package by.weekmenu.api.dto;

import by.weekmenu.api.entity.UnitOfMeasure;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class UnitOfMeasureDto {

    private Long id;

    private String fullName;

    private String shortName;

    public UnitOfMeasureDto(UnitOfMeasure unitOfMeasure) {
        this.id = unitOfMeasure.getId();
        this.fullName = unitOfMeasure.getFullName();
        this.shortName = unitOfMeasure.getShortName();
    }
}
