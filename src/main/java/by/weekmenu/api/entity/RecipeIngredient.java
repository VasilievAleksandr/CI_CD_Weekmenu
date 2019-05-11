package by.weekmenu.api.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.io.Serializable;
import java.math.BigDecimal;

@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(exclude = {"id"})
@Entity
@Table(name = "RECIPE_INGREDIENT")
public class RecipeIngredient implements Serializable {

    private static final long serialVersionUID = 1005642071168789374L;

    @Embeddable
    public static class Id implements Serializable {

        private static final long serialVersionUID = 1015642071168789374L;

        @Column(name = "INGREDIENT_ID")
        private Long ingredientId;

        @Column(name = "RECIPE_ID")
        private Long recipeId;

        public Id() {

        }

        public Id(Long ingredientId, Long recipeId) {
            this.ingredientId = ingredientId;
            this.recipeId = recipeId;
        }

        public boolean equals(Object o) {
            if (o != null && o instanceof Id) {
                Id that = (Id) o;
                return this.ingredientId.equals(that.ingredientId) && this.recipeId.equals(that.recipeId);
            }

            return false;
        }

        public int hashCode() {
            return ingredientId.hashCode() + recipeId.hashCode();
        }

        public Long getRecipeId() {
            return recipeId;
        }

        public Long getIngredientId() {
            return ingredientId;
        }
    }

    @EmbeddedId
    private Id id = new Id();


    @Column(name = "QTY")
    @Digits(
            integer = 5,
            fraction = 2,
            message = "RecipeIngredient's qty '${validatedValue}' must have up to '{integer}' integer digits and '{fraction}' fraction digits."
    )
    @Positive(message = "RecipeIngredient's qty '${validatedValue}' must be positive.")
    private BigDecimal qty;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "INGREDIENT_ID",
            insertable = false,
            updatable = false)
    @Valid
    @NotNull(message = "RecipeIngredient's Ingredient mustn't be null.")
    private Ingredient ingredient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "RECIPE_ID",
            insertable = false,
            updatable = false)
    @Valid
    @NotNull(message = "RecipeIngredient's Recipe mustn't be null.")
    private Recipe recipe;

    public RecipeIngredient(BigDecimal qty, Ingredient ingredient, Recipe recipe) {
        this.qty = qty;
        this.ingredient = ingredient;
        this.recipe = recipe;
    }

    public RecipeIngredient(BigDecimal qty) {
        this.qty = qty;
    }
}
