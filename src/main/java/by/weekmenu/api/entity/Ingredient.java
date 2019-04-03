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
public class Ingredient implements Serializable {

    private static final long serialVersionUID = 1001642071168789374L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer ingredientId;

    @Column
    private String name;

    @Column
    private int price;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "ingredient", cascade = CascadeType.ALL)
    private UnitOfMeasure unitOfMeasure;

    @ManyToMany(fetch = FetchType.LAZY,mappedBy = "ingredients", cascade = CascadeType.ALL)
    private Set<RecipeIngredient> recipeIngredient = new HashSet<RecipeIngredient>();
       
    @ManyToMany(fetch = FetchType.LAZY,mappedBy = "ingredients", cascade = CascadeType.ALL)
    private Set<Ownership> ownership = new HashSet<Ownership>();

    public Ingredient(String name, int price) {
        this.name = name;
        this.price = price;
    }

}
