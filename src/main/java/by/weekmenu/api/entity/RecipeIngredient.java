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
public class RecipeIngredient implements Serializable {

    private static final long serialVersionUID = 1005642071168789374L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer recipeIngredientId;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
            name = "RecipeIngredient_Ingredient",
            joinColumns = {
                @JoinColumn(name = "recipeIngredientId")},
            inverseJoinColumns = {
                @JoinColumn(name = "ingredientId")}
    )
    private Set<Ingredient> ingredients = new HashSet<Ingredient>();

    @OneToOne(fetch = FetchType.LAZY)
    @PrimaryKeyJoinColumn
    private Recipe recipe;

    @Column
    private long kty;

    
}
