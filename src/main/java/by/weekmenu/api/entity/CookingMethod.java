package by.weekmenu.api.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(exclude = {"id", "recipes"})
@Entity
@Table(name = "COOKING_METHOD")
public class CookingMethod implements Serializable {

    private static final long serialVersionUID = 1003642071168789374L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Integer id;

    @Column(name = "NAME")
    @NotBlank(message = "Cooking Method must have name.")
    private String name;

    @OneToMany(mappedBy = "cookingMethod", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<
            @Valid
            @NotNull(message = "Cooking Method must have list of recipes without null elements")
            Recipe> recipes = new HashSet<Recipe>();

    public CookingMethod(String name) {
        this.name = name;
    }
}
