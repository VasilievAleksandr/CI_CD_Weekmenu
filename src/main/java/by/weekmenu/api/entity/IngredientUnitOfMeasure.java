package by.weekmenu.api.entity;


import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;

@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(exclude = {"id"})
@Entity
@Table(name = "INGREDIENT_UNIT_OF_MEASURE")
public class IngredientUnitOfMeasure implements Serializable {

    private static final long serialVersionUID = -434425910110552718L;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Embeddable
    public static class Id implements Serializable {

        private static final long serialVersionUID = 8661129179791241163L;

        @Column(name = "INGREDIENT_ID")
        private Long ingredientId;

        @Column(name = "UNIT_OF_MEASURE_ID")
        private Long unitOfMeasureId;
    }

    @EmbeddedId
    private Id id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "INGREDIENT_ID",
            updatable = false,
            insertable = false)
    private Ingredient ingredient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "UNIT_OF_MEASURE_ID",
            updatable = false,
            insertable = false)
    private UnitOfMeasure unitOfMeasure;

    @Column(name = "EQUIVALENT")
    @NotNull(message = "IngredientUnitOfMeasure must have equivalent")
    @Digits(
            integer = 7,
            fraction = 2,
            message = "Equivalent '${validatedValue}' must have up to '{integer}' integer digits and '{fraction}' fraction digits."
    )
    @Positive(message = "Equivalent '${validatedValue}' must be positive.")
    private BigDecimal equivalent;

    public IngredientUnitOfMeasure(BigDecimal equivalent) {
        this.equivalent = equivalent;
    }
}
