package by.weekmenu.api.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.io.Serializable;


@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(exclude = {"id", "imageLink"})
@Entity
@Table(name = "COOKING_STEP")
public class CookingStep implements Serializable {

    private static final long serialVersionUID = 1004642071168789374L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Integer id;

    @Column(name = "PRIORITY")
    @Positive(message = "Cooking priority '${validatedValue}' must be positive.")
    private Integer priority;

    @Column(name = "DESCRIPTION")
    @NotBlank(message = "Cooking Step must have field 'description' filled.")
    private String description;

    @Column(name = "IMAGE_LINK")
    @Size(
            max = 255,
            message = "ImageLink's length of the cookingStep '${validatedValue}' mustn't be more than '{max}' characters long."
    )
    private String imageLink;

    public CookingStep(Integer priority, String description, String imageLink) {
        this.priority = priority;
        this.description = description;
        this.imageLink = imageLink;
    }
}
