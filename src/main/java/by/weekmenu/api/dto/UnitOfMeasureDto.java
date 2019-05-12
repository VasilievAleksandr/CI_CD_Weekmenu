package by.weekmenu.api.dto;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
public class UnitOfMeasureDto implements Serializable {

    private Long id;

    private String name;
}
