package by.weekmenu.api.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import lombok.EqualsAndHashCode;

@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(exclude = {"id", "recipe"})
@Entity
@Table(name = "COOKING_METHOD")
public class CookingMethod implements Serializable {

    private static final long serialVersionUID = 1003642071168789374L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "COOKING_METHOD_ID")
    private Integer id;

    @Column(name = "COOKING_METHOD_NAME")
    private String name;

//  Может здесь тоже связь нужна только со стороны recipe?  
    @OneToMany(mappedBy = "cookingMethod", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Recipe> recipe = new HashSet<Recipe>();

    public CookingMethod(String name) {
        this.name = name;
    }
}
