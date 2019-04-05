package by.weekmenu.api.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.Valid;
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
    @Column(name = "RECIPE_ID")
    private Long id;

    @Column(name = "RECIPE_NAME")
    private String name;

    @Column(name = "RECIPE_PRICE")
    private BigDecimal price;

    @Column(name = "RECIPE_COOKING_TIME")
    private Short cookingTime;

    @Column(name = "RECIPE_PREPARING_TIME")
    private Short preparingTime;

    @Column(name = "RECIPE_CALORIES")
    private Integer calories;

    @Column(name = "RECIPE_PROTEINS")
    private Integer proteins;

    @Column(name = "RECIPE_FATS")
    private Integer fats;

    @Column(name = "RECIPE_CARBS")
    private Integer carbs;

    @Column(name = "RECIPE_IMAGE_LINK")
    private String imageLink;

    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL)
    private Set<RecipeIngredient> recipeIngredients = new HashSet<>();

    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL)
    private Set<MenuRecipe> menuRecipes = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "RECIPE_COOKING_METHOD_ID")
    @Valid
    private CookingMethod cookingMethod;

    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL)
    private Set<CookingStep> cookingSteps = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "RECIPE_OWNERSHIP_ID")
    @Valid
    private Ownership ownership;
}
