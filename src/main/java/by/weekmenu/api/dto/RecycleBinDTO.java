package by.weekmenu.api.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class RecycleBinDTO {

    private Long id;
    private String elementName;
    private String entityName;
    private LocalDateTime deleteDate;
}
