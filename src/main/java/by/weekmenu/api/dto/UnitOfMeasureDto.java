package by.weekmenu.api.dto;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
public class UnitOfMeasureDto {

    private Long id;

    private String fullName;

    private String shortName;
}
