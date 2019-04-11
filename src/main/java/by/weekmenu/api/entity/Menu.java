package by.weekmenu.api.entity;


import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
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

    @Column(name = "NAME", unique = true)
    @NotBlank(message = "Menu must have have name.")
    private String name;

    @Column(name = "CALORIES")
    @Positive(message = "Menu's calories '${validatedValue}' must be positive.")
    private Integer calories;

    @Column(name = "PROTEINS")
    @Positive(message = "Menu's proteins '${validatedValue}' must be positive.")
    private Integer proteins;

    @Column(name = "FATS")
    @Positive(message = "Menu's fats '${validatedValue}' must be positive.")
    private Integer fats;

    @Column(name = "CARBS")
    @Positive(message = "Menu's carbs '${validatedValue}' must be positive.")
    private Integer carbs;

    @Column(name = "PRICE")
    @Digits(
            integer = 3,
            fraction = 2,
            message = "Menu's price '${validatedValue}' must have up to '{integer}' integer digits and '{fraction}' fraction digits."
    )
    @Positive(message = "Menu's price '${validatedValue}' must be positive.")
    private BigDecimal price;

    @Column(name = "IS_ACTIVE")
    @NotNull(message = "Menu must have field 'isActive' defined.")
    private Boolean isActive;

    @OneToMany(mappedBy = "menu", cascade = CascadeType.ALL)
    private Set<
            @Valid
            MenuRecipe> menuRecipes = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "MENU_CATEGORY_ID")
    @Valid
    private MenuCategory menuCategory;

    @ManyToOne
    @JoinColumn(name = "OWNERSHIP_ID")
    @Valid
    @NotNull(message = "Menu's ownership mustn't be null.")
    private Ownership ownership;

    @OneToMany(mappedBy = "menu", cascade = CascadeType.ALL)
    private Set<
            @Valid
            @NotNull(message = "Menu must have list of dailyMenuStatistics without null elements.")
            DailyMenuStatistics> dailyMenuStatistics = new HashSet<>();
}
