package by.weekmenu.api.entity;

import lombok.*;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.io.Serializable;

@NoArgsConstructor
@Data
@EqualsAndHashCode(exclude = {"id", "priority", "imageLink"})
@Entity
@Table(name = "INGREDIENT_CATEGORY")
public class IngredientCategory implements Serializable {

    private static final long serialVersionUID = 6121210274126963841L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Integer id;

    @Column(name = "NAME", unique = true)
    @NotBlank(message = "IngredientCategory must have name.")
    @Size(max = 255, message = "IngredientCategory's name '${validatedValue}' must be '{max}' characters long")
    private String name;

    @Column(name = "PRIORITY")
    @Positive(message = "IngredientCategory's priority '${validatedValue}' must be positive.")
    private Integer priority;

    @Column(name = "IMAGE_LINK")
    @Size(
            max = 255,
            message = "ImageLink's length of the ingredientCategory '${validatedValue}' mustn't be more than '{max}' characters long."
    )
    private String imageLink;

    @Column(name = "IS_ARCHIVED")
    private boolean isArchived;

    public IngredientCategory(String name, boolean isArchived) {
        this.name = name;
        this.isArchived = isArchived;
    }

    public IngredientCategory(Integer id, String name, boolean isArchived) {
        this.id = id;
        this.name = name;
        this.isArchived = isArchived;
    }

    @PrePersist
    @PreUpdate
    private void nameFirstCapitalLetter(){
        this.name = name == null ? null : StringUtils.capitalize(name);
    }
}
