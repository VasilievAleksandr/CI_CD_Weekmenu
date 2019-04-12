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
import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(exclude = {"id", "imageLink", "menuRecipes"})
@Entity
@Table(name = "RECIPE")
public class Recipe implements Serializable {

    private static final long serialVersionUID = -1791714745167452208L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "NAME", unique = true)
    @NotBlank(message = "Recipe must have have name.")
    private String name;

    @Column(name = "PRICE")
    @Digits(
            integer = 3,
            fraction = 2,
            message = "Recipe's price '${validatedValue}' must have up to '{integer}' integer digits and '{fraction}' fraction digits."
    )
    @Positive(message = "Recipe's price '${validatedValue}' must be positive.")
    private BigDecimal price;

    @Column(name = "COOKING_TIME")
    @Positive(message = "Recipe's cookingTime '${validatedValue}' must be positive.")
    private Short cookingTime;

    @Column(name = "PREPARING_TIME")
    @Positive(message = "Recipe's preparingTime '${validatedValue}' must be positive.")
    private Short preparingTime;

    @Column(name = "CALORIES")
    @Positive(message = "Recipe's calories '${validatedValue}' must be positive.")
    private Integer calories;

    @Column(name = "PROTEINS")
    @Positive(message = "Recipe's proteins '${validatedValue}' must be positive.")
    private Integer proteins;

    @Column(name = "FATS")
    @Positive(message = "Recipe's fats '${validatedValue}' must be positive.")
    private Integer fats;

    @Column(name = "CARBS")
    @Positive(message = "Recipe's carbs '${validatedValue}' must be positive.")
    private Integer carbs;

    @Column(name = "IMAGE_LINK")
    @Size(
            max = 255,
            message = "ImageLink's length of the recipe '${validatedValue}' mustn't be more than '{max}' characters long."
    )
    private String imageLink;

    @Column(name = "IS_ACTIVE")
    @NotNull(message = "Recipe must have field 'isActive' defined.")
    private Boolean isActive;

    @OneToMany(mappedBy = "recipe")
    private Set<
            @Valid
            @NotNull(message = "Recipe must have list of recipeIngredients without null elements.")
            RecipeIngredient> recipeIngredients = new HashSet<>();

    @OneToMany(mappedBy = "recipe")
    private Set<
            @Valid
//            @NotNull(message = "Recipe must have list of menuRecipes without null elements.")
            MenuRecipe> menuRecipes = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "COOKING_METHOD_ID")
    @Valid
    @NotNull(message = "Recipe's cookingMethod mustn't be null.")
    private CookingMethod cookingMethod;

    @OneToMany(mappedBy = "recipe")
    private Set<
            @Valid
            @NotNull(message = "Recipe must have list of cookingSteps without null elements.")
            CookingStep> cookingSteps = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "OWNERSHIP_ID")
    @Valid
    @NotNull(message = "Recipe's ownership mustn't be null.")
    private Ownership ownership;

    public Recipe(String name, Boolean isActive, CookingMethod cookingMethod, Ownership ownership) {
        this.name = name;
        this.isActive = isActive;
        this.cookingMethod = cookingMethod;
        this.ownership = ownership;
    }
}
