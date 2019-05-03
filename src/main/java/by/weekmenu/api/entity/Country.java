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
import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(exclude = {"id"})
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
            message = "UnitOfMeasure's name '${validatedValue}' mustn't be more than '{max}' characters long."
    )
    private String name;

    @Column(name = "ALPHA_CODE_2", unique = true)
    @NotBlank(message = "Country must have alphaCode2.")
    @Size(max = 255,
            message = "Country's alphaCode2 '${validatedValue}' mustn't be more than '{max}' characters long."
    )
    private String alphaCode2;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CURRENCY_ID",
            updatable = false,
            insertable = false)
    @Valid
    @NotNull(message = "Currency's Country mustn't be null.")
    private Currency currency;

    @OneToMany(mappedBy = "country", cascade = CascadeType.PERSIST)
    private Set<
            @Valid
            @NotNull(message = "Country must have list of regionWM without null elements.")
                    Region> regions = new HashSet<>();
}
