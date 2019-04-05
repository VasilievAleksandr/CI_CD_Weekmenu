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
@EqualsAndHashCode(exclude = {"id"})
@Entity
@Table(name = "MENU")
public class Menu implements Serializable {

    private static final long serialVersionUID = 4017330385973945351L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "CALORIES")
    private Integer calories;

    @Column(name = "PROTEINS")
    private Integer proteins;

    @Column(name = "FATS")
    private Integer fats;

    @Column(name = "CARBS")
    private Integer carbs;

    @Column(name = "PRICE")
    private BigDecimal price;

    @OneToMany(mappedBy = "menu", cascade = CascadeType.ALL)
    private Set<MenuRecipe> menuRecipes = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "MENU_CATEGORY_ID")
    @Valid
    private MenuCategory menuCategory;

    @ManyToOne
    @JoinColumn(name = "OWNERSHIP_ID")
    @Valid
    private Ownership ownership;

    @OneToMany(mappedBy = "menu", cascade = CascadeType.ALL)
    private Set<DailyMenuStatistics> dailyMenuStatistics = new HashSet<>();
}
