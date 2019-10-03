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

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Embeddable
    public static class Id implements Serializable {

        private static final long serialVersionUID = 1015642071168789374L;

        @Column(name = "MENU_ID")
        private Long menuId;

        @Column(name = "RECIPE_ID")
        private Long recipeId;
    }

    @EmbeddedId
    private Id id = new Id();

    @ManyToOne
    @JoinColumn(name = "MENU_ID",
            insertable = false,
            updatable = false)
    @Valid
    @NotNull(message = "MenuRecipe must have menu.")
    private Menu menu;

    @ManyToOne
    @JoinColumn(name = "RECIPE_ID",
            insertable = false,
            updatable = false)
    @Valid
    @NotNull(message = "MenuRecipe must have recipe.")
    private Recipe recipe;

    @ManyToOne
    @JoinColumn(name = "MEAL_TYPE_ID")
    @Valid
    @NotNull(message = "MenuRecipe must have mealType.")
    private MealType mealType;

//    @ManyToOne
//    @JoinColumn(name = "DAY_OF_WEEK_ID")
//    @Valid
//    @NotNull(message = "MenuRecipe must have dayOfWeek.")
//    private DayOfWeek dayOfWeek;

    @Column(name = "DAY_OF_WEEK")
    private java.time.DayOfWeek dayOfWeek;

//    public MenuRecipe(Menu menu, Recipe recipe, MealType mealType, DayOfWeek dayOfWeek) {
//        this.menu = menu;
//        this.recipe = recipe;
//        this.mealType = mealType;
//        this.dayOfWeek = dayOfWeek;
//    }

//    public MenuRecipe(Recipe recipe, MealType mealType, DayOfWeek dayOfWeek) {
//        this.recipe = recipe;
//        this.mealType = mealType;
//        this.dayOfWeek = dayOfWeek;
//    }
}
