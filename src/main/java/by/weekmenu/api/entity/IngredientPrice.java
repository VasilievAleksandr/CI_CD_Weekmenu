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
@Table(name = "INGREDIENT_PRICE")
public class IngredientPrice {

    private static final long serialVersionUID = -2495048957992775103L;

    @Embeddable
    public static class Id implements Serializable {

        private static final long serialVersionUID = 7648594292389656966L;

        @Column(name = "INGREDIENT_ID")
        private Long ingredientId;

        @Column(name = "REGION_ID")
        private Byte regionId;

        public Id() {

        }

        public Id(Long ingredientId, Byte regionId) {
            this.ingredientId = ingredientId;
            this.regionId = regionId;
        }

        public boolean equals(Object o) {
            if (o != null && o instanceof Id) {
                Id that = (Id) o;
                return this.ingredientId.equals(that.ingredientId) && this.regionId.equals(that.regionId);
            }

            return false;
        }

        public int hashCode() {
            return ingredientId.hashCode() + regionId.hashCode();
        }

        public Byte getRegionId() {
            return regionId;
        }

        public Long getIngredientId() {
            return ingredientId;
        }
    }

    @EmbeddedId
    private Id id = new Id();


    @Column(name = "PRICE_VALUE")
    @Digits(
            integer = 7,
            fraction = 2,
            message = "Ingredient_Price's Price_Value '${validatedValue}' must have up to '{integer}' integer digits and '{fraction}' fraction digits."
    )
    @Positive(message = "Ingredient_Price's Price_Value '${validatedValue}' must be positive.")
    private BigDecimal priceValue;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "INGREDIENT_ID",
            updatable = false,
            insertable = false)
    @Valid
    @NotNull(message = "Ingredient_Price's Ingredient mustn't be null.")
    private Ingredient ingredient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "REGION_ID",
            updatable = false,
            insertable = false)
    @Valid
    @NotNull(message = "Ingredient_Price's Region mustn't be null.")
    private Region region;

    public IngredientPrice(BigDecimal priceValue, Ingredient ingredient, Region region) {
        this.priceValue = priceValue;
        this.ingredient = ingredient;
        this.region = region;
    }

    public IngredientPrice(BigDecimal priceValue) {
        this.priceValue = priceValue;
    }
}
