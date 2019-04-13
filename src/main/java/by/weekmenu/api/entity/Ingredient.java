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
import lombok.EqualsAndHashCode;

@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(exclude = {"id", "recipeIngredient"})
@Entity
@Table (name = "INGREDIENT")
public class Ingredient implements Serializable {

    private static final long serialVersionUID = 1001642071168789374L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Integer id;

    @Column(name = "NAME")
    private String name;

    @Column(name = "PRICE")
    private BigDecimal price;

    @OneToMany(mappedBy = "ingredient", fetch = FetchType.LAZY)
    private Set<RecipeIngredient> recipeIngredient = new HashSet<RecipeIngredient>();

    @ManyToOne
    @JoinColumn(name = "UNIT_OF_MEASURE_ID")
    @Valid
    private UnitOfMeasure unitOfMeasure;

    @ManyToOne
    @JoinColumn(name = "OWNERSHIP_ID")
    @Valid
    private Ownership ownership;
}
