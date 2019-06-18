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
@Table(name = "BASE_UNIT_OF_MEASURE")
public class BaseUnitOfMeasure implements Serializable {

    private static final long serialVersionUID = 1000642071168789374L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "FULL_NAME", unique = true)
    @NotBlank(message = "BaseUnitOfMeasure must have full name.")
    @Size(max = 255,
            message = "BaseUnitOfMeasure's full name '${validatedValue}' mustn't be more than '{max}' characters long."
    )
    private String fullName;

    @Column(name = "SHORT_NAME", unique = true)
    @NotBlank(message = "BaseUnitOfMeasure must have short name.")
    @Size(max = 255,
            message = "BaseUnitOfMeasure's short name '${validatedValue}' mustn't be more than '{max}' characters long."
    )
    private String shortName;

    public BaseUnitOfMeasure(String shortName, String fullName) {
        this.shortName = shortName;
        this.fullName = fullName;
    }
}
