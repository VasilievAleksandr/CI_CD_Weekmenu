package by.weekmenu.api.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import javax.validation.Valid;
import lombok.EqualsAndHashCode;

@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(exclude = {"id", "imageLink", "recipe"})
@Entity
@Table(name = "COOKING_STEP")
public class CookingStep implements Serializable {

    private static final long serialVersionUID = 1004642071168789374L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "COOKING_STEP_ID")
    private Integer id;

    @Column(name = "COOKING_STEP_PRIORITY")
    private Integer priority;

    @Column(name = "COOKING_STEP_DESCRIPTION")
    private String description;

    @Column(name = "COOKING_STEP_IMAGELINK")
    private String imageLink;

    @ManyToOne
    @JoinColumn(name = "COOKING_STEP_RECIPE_ID")
    @Valid
    private Recipe recipe;

    public CookingStep(Integer priority, String description, String imageLink) {
        this.priority = priority;
        this.description = description;
        this.imageLink = imageLink;
    }
}
