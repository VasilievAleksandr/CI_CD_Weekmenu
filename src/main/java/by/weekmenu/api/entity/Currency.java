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
@EqualsAndHashCode(exclude = {"id"})
@Entity
@Table(name = "CURRENCY")
public class Currency implements Serializable {

    private static final long serialVersionUID = 3124565380015916507L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Integer id;

    @Column(name = "NAME", unique = true)
    @NotBlank(message = "Currency must have name.")
    @Size(max = 255, message = "Currency's name '${validatedValue}' must be '{max}' characters long")
    private String name;

    @Column(name = "CODE", unique = true)
    @NotBlank(message = "Currency must have code.")
    @Size(
            min = 3,
            max = 3,
            message = "Currency's code '${validatedValue}' must be '{min}' characters long.")
    private String code;

    @Column(name = "IS_ARCHIVED")
    @NotNull(message = "Currency must have field 'isArchived' defined.")
    private boolean isArchived;

//    public Currency(String name, String code, String symbol, boolean isArchived) {
//        this.name = name;
//        this.code = code;
//        this.symbol = symbol;
//        this.isArchived = isArchived;
//    }
//
//    public Currency(String name, String code, boolean isArchived) {
//        this.name = name;
//        this.code = code;
//        this.isArchived = isArchived;
//    }
}
