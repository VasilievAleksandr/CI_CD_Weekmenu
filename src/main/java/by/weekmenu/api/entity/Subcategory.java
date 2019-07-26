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
@EqualsAndHashCode(exclude = {"id"})
@Entity
@Table(name = "SUBCATEGORY")
public class Subcategory implements Serializable {

    private static final long serialVersionUID = -7333500515825298201L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "NAME")
    @NotBlank(message = "Subcategory must have name.")
    @Size(max = 255,
            message = "Subcategory's name '${validatedValue}' mustn't be more than '{max}' characters long."
    )
    private String name;

    @Column(name = "DESCRIPTION")
    @Size(max = 1000,
            message = "Subcategory's description '${validatedValue}' mustn't be more than '{max}' characters long."
    )
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CATEGORY_ID")
    @Valid
    @NotNull(message = "Subcategory's Category mustn't be null.")
    private Category category;

    public Subcategory(String name, Category category) {
        this.name = name;
        this.category = category;
    }
}
