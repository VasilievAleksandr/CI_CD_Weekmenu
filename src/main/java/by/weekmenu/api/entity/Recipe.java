package by.weekmenu.api.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

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
@EqualsAndHashCode(exclude = {"id", "imageLink", "recipeCategories", "recipeSubcategories"})
@Entity
@Table(name = "RECIPE")
public class Recipe implements Serializable {

    private static final long serialVersionUID = -1791714745167452208L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "NAME", unique = true)
    @NotBlank(message = "Recipe must have name.")
    @Size(max = 255, message = "Recipe's name '${validatedValue}' must be '{max}' characters long")
    private String name;

    @Column(name = "COOKING_TIME")
    @PositiveOrZero(message = "Recipe's cookingTime '${validatedValue}' must be positive or '0'.")
    private Short cookingTime;

    @Column(name = "PREPARING_TIME")
    @PositiveOrZero(message = "Recipe's activeTime '${validatedValue}' must be positive or '0'.")
    private Short activeTime;

    @Column(name = "CALORIES")
    @Digits(
            integer = 7,
            fraction = 0,
            message = "Calories '${validatedValue}' must have up to '{integer}' integer digits and '{fraction}' fraction digits."
    )
    @PositiveOrZero(message = "Recipe's calories '${validatedValue}' must be positive or '0'.")
    private BigDecimal calories;

    @Column(name = "PROTEINS")
    @Digits(
            integer = 7,
            fraction = 1,
            message = "Proteins '${validatedValue}' must have up to '{integer}' integer digits and '{fraction}' fraction digits."
    )
    @PositiveOrZero(message = "Recipe's proteins '${validatedValue}' must be positive or '0'.")
    private BigDecimal proteins;

    @Column(name = "FATS")
    @Digits(
            integer = 7,
            fraction = 1,
            message = "Fats '${validatedValue}' must have up to '{integer}' integer digits and '{fraction}' fraction digits."
    )
    @PositiveOrZero(message = "Recipe's fats '${validatedValue}' must be positive or '0'.")
    private BigDecimal fats;

    @Column(name = "CARBS")
    @Digits(
            integer = 7,
            fraction = 1,
            message = "Carbs '${validatedValue}' must have up to '{integer}' integer digits and '{fraction}' fraction digits."
    )
    @PositiveOrZero(message = "Recipe's carbs '${validatedValue}' must be positive or '0'.")
    private BigDecimal carbs;

    @Column(name = "IMAGE_LINK")
    @Size(
            max = 255,
            message = "ImageLink's length of the recipe '${validatedValue}' mustn't be more than '{max}' characters long."
    )
    private String imageLink = "";

    @Column(name = "IS_ARCHIVED")
    private boolean isArchived;

    @Column(name = "PORTIONS")
    @Positive(message = "Recipe's portions '${validatedValue}' must be positive.")
    private Short portions;

    @Column(name = "GRAMS_PER_PORTION")
    @Digits(
            integer = 5,
            fraction = 1,
            message = "Grams per portion '${validatedValue}' must have up to '{integer}' integer digits and '{fraction}' fraction digits."
    )
    @PositiveOrZero(message = "Grams per portion '${validatedValue}' must be positive or '0'.")
    private BigDecimal gramsPerPortion;

    @Column(name = "SOURCE")
    @Size(max = 255, message = "Recipe's source length '${validatedValue}' mustn't be more than '{max}' characters long.")
    private String source;

    @ManyToOne
    @JoinColumn(name = "COOKING_METHOD_ID")
    @Valid
    @NotNull(message = "Recipe's cookingMethod mustn't be null.")
    private CookingMethod cookingMethod;

    @ManyToOne
    @JoinColumn(name = "OWNERSHIP_ID")
    @Valid
    @NotNull(message = "Recipe's ownership mustn't be null.")
    private Ownership ownership;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "RECIPE_RECIPECATEGORY",
            joinColumns = @JoinColumn(name = "RECIPE_ID"),
            inverseJoinColumns = @JoinColumn(name = "RECIPECATEGORY_ID"))
    private Set<RecipeCategory> recipeCategories = new HashSet<>();

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "RECIPE_RECIPESUBCATEGORY",
            joinColumns = @JoinColumn(name = "RECIPE_ID"),
            inverseJoinColumns = @JoinColumn(name = "RECIPESUBCATEGORY_ID"))
    private Set<RecipeSubcategory> recipeSubcategories = new HashSet<>();

    public Recipe(String name, boolean isArchived, CookingMethod cookingMethod, Ownership ownership) {
        this.name = name;
        this.isArchived = isArchived;
        this.cookingMethod = cookingMethod;
        this.ownership = ownership;
    }

    @PrePersist
    @PreUpdate
    private void prepareData(){
        this.cookingTime = cookingTime == null ? 0 : cookingTime;
        this.activeTime = activeTime == null ? 0 : activeTime;
        this.calories = calories == null ? BigDecimal.ZERO : calories;
        this.carbs = carbs == null ? BigDecimal.ZERO : carbs;
        this.fats = fats == null ? BigDecimal.ZERO : fats;
        this.proteins = proteins == null ? BigDecimal.ZERO : proteins;
        this.portions = portions == null ? 1 : portions;
        this.gramsPerPortion = gramsPerPortion == null ? BigDecimal.ZERO : gramsPerPortion;
        this.name = name == null ? null : StringUtils.capitalize(name);
    }

    public void addRecipeCategory(RecipeCategory recipeCategory) {
        recipeCategories.add(recipeCategory);
        recipeCategory.getRecipes().add(this);
    }

    public void addRecipeSubcategory (RecipeSubcategory recipeSubcategory) {
        recipeSubcategories.add(recipeSubcategory);
        recipeSubcategory.getRecipes().add(this);
    }
}
