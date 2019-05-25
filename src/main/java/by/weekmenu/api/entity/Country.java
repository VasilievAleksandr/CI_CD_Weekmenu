package by.weekmenu.api.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(exclude = "id")
@Entity
@Table(name = "COUNTRY")
public class Country implements Serializable {

    private static final long serialVersionUID = 5158608710377090853L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "NAME", unique = true)
    @NotBlank(message = "Country must have name.")
    @Size(max = 255,
            message = "Country's name '${validatedValue}' mustn't be more than '{max}' characters long."
    )
    private String name;

    @Column(name = "ALPHA_CODE_2", unique = true)
    @NotBlank(message = "Country must have alphaCode2.")
    @Size(
            min = 2,
            max = 2,
            message = "Country's alphaCode2 '${validatedValue}' must be '{min}' characters long."
    )
    private String alphaCode2;

    @ManyToOne
    @JoinColumn(name = "CURRENCY_ID")
    @Valid
    @NotNull(message = "Country's currency mustn't be null.")
    private Currency currency;

    public Country(String name, String alphaCode2, Currency currency) {
        this.name = name;
        this.alphaCode2 = alphaCode2;
        this.currency = currency;
    }
}
