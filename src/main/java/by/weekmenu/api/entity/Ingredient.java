package by.weekmenu.api.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import javax.validation.Valid;
import javax.validation.constraints.*;

import lombok.EqualsAndHashCode;

@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(exclude = {"id", "recipeIngredient"})
@Entity
@Table(name = "INGREDIENT")
public class Ingredient implements Serializable {

    private static final long serialVersionUID = 1001642071168789374L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Integer id;

    @Column(name = "NAME", unique = true)
    @NotBlank(message = "Ingredient must have name.")
    private String name;

    @Column(name = "PRICE")
    @Digits(
            integer = 7,
            fraction = 2,
            message = "Ingredient's price '${validatedValue}' must have up to '{integer}' integer digits and '{fraction}' fraction digits."
    )
    @Positive(message = "Ingredient's price '${validatedValue}' must be positive.")
    private BigDecimal price;

    @Column(name = "CALORIES")
    @Positive(message = "Ingredient's calories '${validatedValue}' must be positive.")
    private Integer calories;

    @Column(name = "PROTEINS")
    @PositiveOrZero(message = "Ingredient's proteins '${validatedValue}' must be positive or '0'.")
    private Integer proteins;

    @Column(name = "FATS")
    @PositiveOrZero(message = "Ingredient's fats '${validatedValue}' must be positive or '0'.")
    private Integer fats;

    @Column(name = "CARBS")
    @PositiveOrZero(message = "Ingredient's carbs '${validatedValue}' must be positive or '0'.")
    private Integer carbs;

    @OneToMany(mappedBy = "ingredient", fetch = FetchType.LAZY)
    private Set<
            @Valid
            @NotNull(message = "Ingredient must have list of recipeIngredients without null elements.")
            RecipeIngredient> recipeIngredients = new HashSet<RecipeIngredient>();

    @ManyToOne
    @JoinColumn(name = "UNIT_OF_MEASURE_ID")
    @Valid
    @NotNull(message = "Ingredient's unitOfMeasure mustn't be null.")
    private UnitOfMeasure unitOfMeasure;

    @ManyToOne
    @JoinColumn(name = "OWNERSHIP_ID")
    @Valid
    @NotNull(message = "Ingredient's ownership mustn't be null.")
    private Ownership ownership;

    public Ingredient(String name, BigDecimal price, Integer calories, Integer proteins, Integer fats, Integer carbs,
                      @Valid UnitOfMeasure unitOfMeasure, @Valid Ownership ownership) {
        this.name = name;
        this.price = price;
        this.calories = calories;
        this.proteins = proteins;
        this.fats = fats;
        this.carbs = carbs;
        this.unitOfMeasure = unitOfMeasure;
        this.ownership = ownership;
    }

    public Ingredient(String name, BigDecimal price) {
        this.name = name;
        this.price = price;
    }

    public Ingredient(Integer calories, Integer proteins, Integer fats, Integer carbs) {
        this.calories = calories;
        this.proteins = proteins;
        this.fats = fats;
        this.carbs = carbs;
    }
    public Ingredient(String name, BigDecimal price, Ownership ownership) {
        this.name = name;
        this.price = price;
        this.ownership = ownership;
    }

    public Ingredient( UnitOfMeasure unitOfMeasure) {
        this.unitOfMeasure = unitOfMeasure;
    }
}
