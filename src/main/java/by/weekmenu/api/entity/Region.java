package by.weekmenu.api.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;


@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(exclude = {"id"})
@Entity
@Table(name = "REGION")
public class Region implements Serializable {

    private static final long serialVersionUID = -7333500515825298207L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "NAME", unique = true)
    @NotBlank(message = "Region must have name.")
    @Size(max = 255,
            message = "Region's name '${validatedValue}' mustn't be more than '{max}' characters long."
    )
    private String name;

    @Column(name = "IS_ARCHIVED")
    private boolean isArchived;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "COUNTRY_ID")
    @Valid
    @NotNull(message = "Region's Country mustn't be null.")
    private Country country;

    public Region(String name, Country country) {
        this.name = name;
        this.country = country;
    }

    @PrePersist
    @PreUpdate
    private void nameFirstCapitalLetter(){
        this.name = name == null ? null : StringUtils.capitalize(name);
    }
}
