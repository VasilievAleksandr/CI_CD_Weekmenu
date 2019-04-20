package by.weekmenu.api.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import javax.validation.Valid;
import javax.validation.constraints.*;

import lombok.EqualsAndHashCode;

@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(exclude = {"id"})
@Entity
@Table(name = "RECIPE_CURRENCY")
public class RecipeCurrency {
    private static final long serialVersionUID = 1115642071168789375L;

    @Embeddable
    public static class Id implements Serializable {

        private static final long serialVersionUID = 1125642071168789375L;

        @Column(name = "RECIPE_ID")
        private Long recipeId;

        @Column(name = "CURRENCY_ID")
        private Byte currencyId;

        public Id() {

        }

        public Id(Long recipeId, Byte currencyId) {
            this.recipeId = recipeId;
            this.currencyId = currencyId;
        }

        public boolean equals(Object o) {
            if (o != null && o instanceof Id) {
                Id that = (Id) o;
                return this.recipeId.equals(that.recipeId) && this.currencyId.equals(that.currencyId);
            }

            return false;
        }

        public int hashCode() {
            return recipeId.hashCode() + currencyId.hashCode();
        }

        public Byte getCurrencyId() {
            return currencyId;
        }

        public Long getRecipeId() {
            return recipeId;
        }
    }

    @EmbeddedId
    private Id id = new Id();


    @Column(name = "PRICE_VALUE")
    @Digits(
            integer = 7,
            fraction = 2,
            message = "Recipe_Currency's Price_Value '${validatedValue}' must have up to '{integer}' integer digits and '{fraction}' fraction digits."
    )
    @Positive(message = "Recipe_Currency's Price_Value '${validatedValue}' must be positive.")
    private BigDecimal priceValue;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "RECIPE_ID")
    @Valid
    @NotNull(message = "Recipe_Currency's Recipe mustn't be null.")
    private Recipe recipe;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CURRENCY_ID")
    @Valid
    @NotNull(message = "Recipe_Currency's Currency mustn't be null.")
    private Currency currency;

    public RecipeCurrency(BigDecimal priceValue, Recipe recipe, Currency currency) {
        this.priceValue = priceValue;
        this.recipe = recipe;
        this.currency = currency;
    }

    public RecipeCurrency(BigDecimal priceValue) {
        this.priceValue = priceValue;
    }
}
