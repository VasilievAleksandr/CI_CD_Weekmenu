package by.weekmenu.api.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table
public class CookingStep implements Serializable {

    private static final long serialVersionUID = 1004642071168789374L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer cookingStepId;

    @ManyToOne
    @JoinColumn(name = "recipeId", nullable = false)
    private Recipe recipe;

    @Column
    private int priority;

    @Column
    private String description;

    @Column
    private String imageLink;

    public CookingStep(int priority, String description, String imageLink) {
        this.priority = priority;
        this.description = description;
        this.imageLink = imageLink;
    }

}
