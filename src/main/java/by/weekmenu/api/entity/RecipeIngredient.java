package by.weekmenu.api.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import lombok.EqualsAndHashCode;

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

        @Column(name = "ID")
        private Long ingredientId;

        @Column(name = "ID")
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
    private Long qty;

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn(name = "INGREDIENT_ID")
    private Ingredient ingredient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "RECIPE_ID")
    private Recipe recipe;

    public RecipeIngredient(Long qty, Ingredient ingredient, Recipe recipe) {
        this.qty = qty;
        this.ingredient = ingredient;
        this.recipe = recipe;
    }
}
