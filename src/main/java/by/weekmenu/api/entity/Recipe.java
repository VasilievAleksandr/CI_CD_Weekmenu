package by.weekmenu.api.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table
public class Recipe implements Serializable {

    private static final long serialVersionUID = 1006642071168789374L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer recipeId;

    @Column
    private String name;

    @Column
    private int price;

    @Column
    private int cookingTime;

    @Column
    private int preparingTime;

    @Column
    private int calories;

    @Column
    private int proteins;

    @Column
    private int fats;

    @Column
    private int carbohydrates;

    @Column
    private String imageLink;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "recipe", cascade = CascadeType.ALL)
    private RecipeIngredient recipeIngredient;
    
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "recipe", cascade = CascadeType.ALL)
    private CookingMethod cookingMethod;

    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<CookingStep> CookingStep = new HashSet<CookingStep>();
    
    @ManyToOne
    @JoinColumn(name = "ownershipId", nullable = false)
    private Ownership ownership;

//    @ManyToOne
//    @JoinColumn(name = "menuId", nullable = false)
//    private Menu menu;
    
    public Recipe(String name) {
        this.name = name;
    }

}
