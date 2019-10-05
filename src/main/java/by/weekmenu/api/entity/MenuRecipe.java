package by.weekmenu.api.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(exclude = {"id"})
@Entity
@Table(name = "MENU_RECIPE")
public class MenuRecipe implements Serializable {

    private static final long serialVersionUID = -785102211168469155L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "MENU_ID")
    @Valid
    @NotNull(message = "MenuRecipe must have menu.")
    private Menu menu;

    @ManyToOne
    @JoinColumn(name = "RECIPE_ID")
    @Valid
    @NotNull(message = "MenuRecipe must have recipe.")
    private Recipe recipe;

    @ManyToOne
    @JoinColumn(name = "MEAL_TYPE_ID")
    @Valid
    @NotNull(message = "MenuRecipe must have mealType.")
    private MealType mealType;

    @Column(name = "DAY_OF_WEEK")
    private java.time.DayOfWeek dayOfWeek;
}
