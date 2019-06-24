package by.weekmenu.api.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;

@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(exclude = {"id"})
@Entity
@Table(name = "INGREDIENT")
public class Ingredient implements Serializable {

    private static final long serialVersionUID = 1001642071168789374L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "NAME", unique = true)
    @NotBlank(message = "Ingredient must have name.")
    @Size(  max = 255,
            message = "Ingredient's name '${validatedValue}' mustn't be more than '{max}' characters long."
    )
    private String name;

    @Column(name = "CALORIES")
    @Digits(
            integer = 7,
            fraction = 1,
            message = "Calories '${validatedValue}' must have up to '{integer}' integer digits and '{fraction}' fraction digits."
    )
    @Positive(message = "Ingredient's calories '${validatedValue}' must be positive.")
    private BigDecimal calories;

    @Column(name = "PROTEINS")
    @Digits(
            integer = 3,
            fraction = 1,
            message = "Proteins '${validatedValue}' must have up to '{integer}' integer digits and '{fraction}' fraction digits."
    )
    @DecimalMax(value = "100", message = "Ingredient's proteins '${validatedValue}' must be lower than '{value}'.")
    @PositiveOrZero(message = "Ingredient's proteins '${validatedValue}' must be positive or '0'.")
    private BigDecimal proteins;

    @Column(name = "FATS")
    @Digits(
            integer = 3,
            fraction = 1,
            message = "Fats '${validatedValue}' must have up to '{integer}' integer digits and '{fraction}' fraction digits."
    )
    @DecimalMax(value = "100", message = "Ingredient's fats '${validatedValue}' must be lower than '{value}'.")
    @PositiveOrZero(message = "Ingredient's fats '${validatedValue}' must be positive or '0'.")
    private BigDecimal fats;

    @Column(name = "CARBS")
    @Digits(
            integer = 3,
            fraction = 1,
            message = "Carbs '${validatedValue}' must have up to '{integer}' integer digits and '{fraction}' fraction digits."
    )
    @DecimalMax(value = "100", message = "Ingredient's carbs '${validatedValue}' must be lower than '{value}'.")
    @PositiveOrZero(message = "Ingredient's carbs '${validatedValue}' must be positive or '0'.")
    private BigDecimal carbs;

    @Transient
    private BaseUOM baseUOM = BaseUOM.GRAMM;

    @ManyToOne
    @JoinColumn(name = "OWNERSHIP_ID")
    @Valid
    @NotNull(message = "Ingredient's ownership mustn't be null.")
    private Ownership ownership;

    public Ingredient(String name, BigDecimal calories, BigDecimal proteins, BigDecimal fats, BigDecimal carbs,
                      Ownership ownership) {
        this.name = name;
        this.calories = calories;
        this.proteins = proteins;
        this.fats = fats;
        this.carbs = carbs;
        this.ownership = ownership;
    }

    public Ingredient(BigDecimal calories, BigDecimal proteins, BigDecimal fats, BigDecimal carbs) {
        this.calories = calories;
        this.proteins = proteins;
        this.fats = fats;
        this.carbs = carbs;
    }

    public Ingredient(String name, Ownership ownership) {
        this.name = name;
        this.ownership = ownership;
    }
}
