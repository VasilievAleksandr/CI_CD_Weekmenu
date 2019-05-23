package by.weekmenu.api.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
public class CurrencyDto implements Serializable {

    private Integer id;

    private String name;

    private String code;

    private String symbol;

    private Boolean isActive;

 }
