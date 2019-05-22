package by.weekmenu.api.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;

import lombok.EqualsAndHashCode;

@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(exclude = {"id"})
@Entity
@Table(name = "UNIT_OF_MEASURE")
public class UnitOfMeasure implements Serializable {

    private static final long serialVersionUID = 1000642071168789374L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "NAME", unique = true)
    @NotBlank(message = "UnitOfMeasure must have name.")
    @Size(max = 255,
            message = "UnitOfMeasure's name '${validatedValue}' mustn't be more than '{max}' characters long."
    )
    private String name;

    @Column(name = "FULL_NAME", unique = true)
    @NotBlank(message = "UnitOfMeasure must have full name.")
    @Size(max = 255,
            message = "UnitOfMeasure's full name '${validatedValue}' mustn't be more than '{max}' characters long."
    )
    private String fullName;

    public UnitOfMeasure(String name, String fullName) {
        this.name = name;
        this.fullName = fullName;
    }
}
