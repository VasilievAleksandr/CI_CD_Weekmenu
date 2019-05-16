package by.weekmenu.api.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class CurrencyDto implements Serializable {

    private Byte id;

    private String name;

    private String code;

    private String symbol;

    private Boolean isActive;

    private Set<String> countries;

}
