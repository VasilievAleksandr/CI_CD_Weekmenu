package by.weekmenu.api.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(exclude = "id")
@Entity
@Table(name = "RECIPE_SUBCATEGORY")
public class RecipeSubcategory implements Serializable {

    private static final long serialVersionUID = 5158608755557092853L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "NAME", unique = true)
    @NotBlank(message = "RecipeSubcategory must have name.")
    @Size(max = 255,
            message = "RecipeCategory's name '${validatedValue}' mustn't be more than '{max}' characters long."
    )
    private String name;

    @ManyToMany(mappedBy = "recipeSubcategories")
    private Set<Recipe> recipes = new HashSet<>();

    @Column(name = "IS_ARCHIVED")
    private boolean isArchived;

    @Column(name = "PRIORITY")
    private Integer priority;

    public RecipeSubcategory(String name) {
        this.name = name;
    }

    public RecipeSubcategory(Long id , String name) {
        this.id = id;
        this.name = name;
    }

    @PrePersist
    @PreUpdate
    private void nameFirstCapitalLetter(){
        this.name = name == null ? null : StringUtils.capitalize(name);
    }
}
