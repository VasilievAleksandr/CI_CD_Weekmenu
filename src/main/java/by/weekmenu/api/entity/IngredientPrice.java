package by.weekmenu.api.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
public class IngredientPrice implements Serializable{

    private static final long serialVersionUID = -2495048957992775103L;

    @Embeddable
    public static class Id implements Serializable {

        private static final long serialVersionUID = 7648594292389656966L;

        @Column(name = "INGREDIENT_ID")
        private Long ingredientId;

        @Column(name = "REGION_ID")
        private Long regionId;

        Id() {

        }

        public Id(Long ingredientId, Long regionId) {
            this.ingredientId = ingredientId;
            this.regionId = regionId;
        }

        public boolean equals(Object o) {
            if (o instanceof Id) {
                Id that = (Id) o;
                return this.ingredientId.equals(that.ingredientId) && this.regionId.equals(that.regionId);
            }

            return false;
        }

        public int hashCode() {
            return ingredientId.hashCode() + regionId.hashCode();
        }

        public Long getRegionId() {
            return regionId;
        }

        public Long getIngredientId() {
            return ingredientId;
        }
    }

    @EmbeddedId
    private Id id = new Id();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "INGREDIENT_ID",
            updatable = false,
            insertable = false)
    @Valid
    @NotNull(message = "Ingredient_Price's Ingredient mustn't be null.")
    @JsonIgnore
    private Ingredient ingredient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "REGION_ID",
            updatable = false,
            insertable = false)
    @Valid
    @NotNull(message = "Ingredient_Price's Region mustn't be null.")
    private Region region;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "UNIT_OF_MEASURE_ID")
    @NotNull(message = "IngredientPrice must have UnitOfMeasure")
    private UnitOfMeasure unitOfMeasure;

    @Column(name = "QUANTITY")
    @Digits(
            integer = 7,
            fraction = 1,
            message = "Quantity '${validatedValue}' must have up to '{integer}' integer digits and '{fraction}' fraction digits."
    )
    @Positive(message = "Quantity '${validatedValue}' must be positive.")
    @NotNull(message = "IngredientPrice must have quantity")
    private BigDecimal quantity;

    @Column(name = "PRICE_VALUE")
    @Digits(
            integer = 7,
            fraction = 2,
            message = "Price_Value '${validatedValue}' must have up to '{integer}' integer digits and '{fraction}' fraction digits."
    )
    @Positive(message = "Price_Value '${validatedValue}' must be positive.")
    @NotNull(message = "IngredientPrice must have priceValue")
    private BigDecimal priceValue;

    public IngredientPrice(Ingredient ingredient, Region region) {
        this.ingredient = ingredient;
        this.region = region;
    }
}
