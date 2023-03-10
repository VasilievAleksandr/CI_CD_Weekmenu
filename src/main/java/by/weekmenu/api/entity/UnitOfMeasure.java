package by.weekmenu.api.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

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

    @Column(name = "FULL_NAME", unique = true)
    @NotBlank(message = "UnitOfMeasure must have full name.")
    @Size(max = 255,
            message = "UnitOfMeasure's full name '${validatedValue}' mustn't be more than '{max}' characters long."
    )
    private String fullName;

    @Column(name = "SHORT_NAME", unique = true)
    @NotBlank(message = "UnitOfMeasure must have short name.")
    @Size(max = 255,
            message = "UnitOfMeasure's short name '${validatedValue}' mustn't be more than '{max}' characters long."
    )
    private String shortName;

    @Column(name = "IS_ARCHIVED")
    @NotNull(message = "UnitOfMeasure must have field 'isArchived' defined.")
    private boolean isArchived;

    public UnitOfMeasure(String shortName, String fullName) {
        this.shortName = shortName;
        this.fullName = fullName;
    }

    public UnitOfMeasure(Long id, String shortName, String fullName) {
        this.id = id;
        this.shortName = shortName;
        this.fullName = fullName;
    }

    @PrePersist
    @PreUpdate
    private void fullNameFirstCapitalLetter(){
        this.fullName = fullName == null ? null : StringUtils.capitalize(fullName);
    }
}
