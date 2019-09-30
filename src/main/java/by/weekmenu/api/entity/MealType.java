package by.weekmenu.api.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.io.Serializable;

@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(exclude = {"id", "priority"})
@Entity
@Table(name = "MEAL_TYPE")
public class MealType implements Serializable {

    private static final long serialVersionUID = -8986552656545603913L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Short id;

    @Column(name = "NAME", unique = true)
    @NotBlank(message = "MealType must have name.")
    @Size(max = 255, message = "MealType's name '${validatedValue}' must be '{max}' characters long")
    private String name;

    @Column(name = "PRIORITY")
    @Positive(message = "MealType's priority '${validatedValue}' must be positive.")
    private Integer priority;

    @Column(name = "IS_ARCHIVED")
    private boolean isArchived;

    public MealType(String name, boolean isArchived) {
        this.name = name;
        this.isArchived = isArchived;
    }

    public MealType(Short id, String name, Integer priority, boolean isArchived) {
        this.id = id;
        this.name = name;
        this.priority = priority;
        this.isArchived = isArchived;
    }
}
