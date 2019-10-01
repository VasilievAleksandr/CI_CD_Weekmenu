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
@EqualsAndHashCode(exclude = {"id", "priority"})
@Entity
@Table(name = "DISH_TYPE")
public class DishType implements Serializable {

    private static final long serialVersionUID = -8986552656545603913L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Short id;

    @Column(name = "NAME", unique = true)
    @NotBlank(message = "DishType must have name.")
    @Size(max = 255, message = "DishType's name '${validatedValue}' must be '{max}' characters long")
    private String name;

    @Column(name = "PRIORITY")
    @Positive(message = "DishType's priority '${validatedValue}' must be positive.")
    private Integer priority;

    @Column(name = "IS_ACTIVE")
    @NotNull(message = "DishType must have field 'isActive' defined.")
    private Boolean isActive;

    public DishType(String name, Boolean isActive) {
        this.name = name;
        this.isActive = isActive;
    }
}
