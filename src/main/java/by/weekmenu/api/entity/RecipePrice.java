package by.weekmenu.api.entity;

import lombok.*;

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
@Table(name = "RECIPE_PRICE")
public class RecipePrice {
    private static final long serialVersionUID = 1115642071168789375L;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Embeddable
    public static class Id implements Serializable {

        private static final long serialVersionUID = 1125642071168789375L;

        @Column(name = "RECIPE_ID")
        private Long recipeId;

        @Column(name = "REGION_ID")
        private Long regionId;
    }

    @EmbeddedId
    private Id id = new Id();

    @Column(name = "PRICE_VALUE")
    @Digits(
            integer = 7,
            fraction = 2,
            message = "Recipe_Price's Price_Value '${validatedValue}' must have up to '{integer}' integer digits and '{fraction}' fraction digits."
    )
    @Positive(message = "Recipe_Price's Price_Value '${validatedValue}' must be positive.")
    private BigDecimal priceValue;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "RECIPE_ID",
            updatable = false,
            insertable = false)
    @Valid
    @NotNull(message = "Recipe_Price's Recipe mustn't be null.")
    private Recipe recipe;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "REGION_ID",
            updatable = false,
            insertable = false)
    @Valid
    @NotNull(message = "Recipe_Price's Currency mustn't be null.")
    private Region region;

    public RecipePrice(BigDecimal priceValue, Recipe recipe, Region region) {
        this.priceValue = priceValue;
        this.recipe = recipe;
        this.region = region;
    }

    public RecipePrice(BigDecimal priceValue) {
        this.priceValue = priceValue;
    }
}
