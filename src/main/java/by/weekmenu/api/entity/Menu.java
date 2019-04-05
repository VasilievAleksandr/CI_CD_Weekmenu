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
    @Column(name = "MENU_ID")
    private Long id;

    @Column(name = "MENU_CALORIES")
    private Integer calories;

    @Column(name = "MENU_PROTEINS")
    private Integer proteins;

    @Column(name = "MENU_FATS")
    private Integer fats;

    @Column(name = "MENU_CARBS")
    private Integer carbs;

    @Column(name = "MENU_PRICE")
    private BigDecimal price;

    @OneToMany(mappedBy = "menu", cascade = CascadeType.ALL)
    private Set<MenuRecipe> menuRecipes = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "MENU_MENU_CATEGORY_ID")
    @Valid
    private MenuCategory menuCategory;

    @ManyToOne
    @JoinColumn(name = "MENU_OWNERSHIP_ID")
    @Valid
    private Ownership ownership;

    @OneToMany(mappedBy = "menu", cascade = CascadeType.ALL)
    private Set<DailyMenuStatistics> dailyMenuStatistics = new HashSet<>();
}
