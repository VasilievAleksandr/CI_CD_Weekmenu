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
public class Ownership implements Serializable {

    private static final long serialVersionUID = 1002642071168789374L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ownershipId;

    @Column
    private String name;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
        name = "Ownership_Ingredient", 
        joinColumns = { @JoinColumn(name = "ownershipId") }, 
        inverseJoinColumns = { @JoinColumn(name = "ingredientId") }
    )
    private Set<Ingredient> ingredients = new HashSet<Ingredient>();
    
    @OneToMany(mappedBy = "ownership", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Recipe> recipe = new HashSet<Recipe>();
    
//    @OneToMany(mappedBy = "ownership", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    private Set<Menu> menu = new HashSet<Menu>();
    
    public Ownership(String name) {
        this.name = name;
    }

}
