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
    @Column(name = "ID")
    private Long id;

    @Column(name = "NAME", unique = true)
    private String name;

    @Column(name = "PRICE")
    private BigDecimal price;

    @Column(name = "COOKING_TIME")
    private Short cookingTime;

    @Column(name = "PREPARING_TIME")
    private Short preparingTime;

    @Column(name = "CALORIES")
    private Integer calories;

    @Column(name = "PROTEINS")
    private Integer proteins;

    @Column(name = "FATS")
    private Integer fats;

    @Column(name = "CARBS")
    private Integer carbs;

    @Column(name = "IMAGE_LINK")
    private String imageLink;

    @Column(name = "IS_ACTIVE")
    private Boolean isActive;

    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL)
    private Set<RecipeIngredient> recipeIngredients = new HashSet<>();

    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL)
    private Set<MenuRecipe> menuRecipes = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "COOKING_METHOD_ID")
    @Valid
    private CookingMethod cookingMethod;

    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL)
    private Set<CookingStep> cookingSteps = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "OWNERSHIP_ID")
    @Valid
    private Ownership ownership;
}
