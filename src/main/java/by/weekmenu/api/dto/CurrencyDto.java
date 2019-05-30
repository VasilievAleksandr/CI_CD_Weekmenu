package by.weekmenu.api.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CurrencyDto {

    private Integer id;

    private String name;

    private String code;

    private String symbol;

    private Boolean isActive;

 }
