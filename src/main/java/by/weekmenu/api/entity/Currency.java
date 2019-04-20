package by.weekmenu.api.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;

@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(exclude = "id")
@Entity
@Table(name = "CURRENCY")
public class Currency implements Serializable {

    private static final long serialVersionUID = 3124565380015916507L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Byte id;

    @Column(name = "NAME", unique = true)
    @NotBlank(message = "Currency must have name.")
    private String name;

    @Column(name = "CODE", unique = true)
    @NotBlank(message = "Currency must have code.")
    @Size(
            min = 3,
            max = 3,
            message = "Bank's code '${validatedValue}' must be '{min}' characters long.")
    private String code;

    @Column(name = "CODE", unique = true)
    @Pattern(regexp = "\\p{Sc}", message = "Currency's symbol must match pattern.")
    private String symbol;

    @Column(name = "IS_ACTIVE")
    @NotNull(message = "Currency must have field 'isActive' defined.")
    private Boolean isActive;

    public Currency(String name, String code, String symbol, Boolean isActive) {
        this.name = name;
        this.code = code;
        this.symbol = symbol;
        this.isActive = isActive;
    }
}
