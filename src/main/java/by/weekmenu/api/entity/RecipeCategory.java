package by.weekmenu.api.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;

@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(exclude = "id")
@Entity
@Table(name = "RECIPE_CATEGORY")
public class RecipeCategory implements Serializable {

    private static final long serialVersionUID = 5158608755557090853L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "NAME", unique = true)
    @NotBlank(message = "RecipeCategory must have name.")
    @Size(max = 255,
            message = "RecipeCategory's name '${validatedValue}' mustn't be more than '{max}' characters long."
    )
    private String name;

    public RecipeCategory(String name) {
        this.name = name;
    }
}
