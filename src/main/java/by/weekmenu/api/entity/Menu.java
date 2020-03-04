package by.weekmenu.api.entity;


import lombok.*;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;

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
    @NotBlank(message = "Menu must have name.")
    @Size(max = 255, message = "Menu's name '${validatedValue}' must be '{max}' characters long")
    private String name;

    @Column(name = "CALORIES")
    @Digits(
            integer = 7,
            fraction = 1,
            message = "Calories '${validatedValue}' must have up to '{integer}' integer digits and '{fraction}' fraction digits."
    )
    @PositiveOrZero(message = "Menu's calories '${validatedValue}' must be positive or '0'.")
    private BigDecimal calories;

    @Column(name = "PROTEINS")
    @Digits(
            integer = 7,
            fraction = 1,
            message = "Proteins '${validatedValue}' must have up to '{integer}' integer digits and '{fraction}' fraction digits."
    )
    @PositiveOrZero(message = "Menu's proteins '${validatedValue}' must be positive or '0'.")
    private BigDecimal proteins;

    @Column(name = "FATS")
    @Digits(
            integer = 7,
            fraction = 1,
            message = "Fats '${validatedValue}' must have up to '{integer}' integer digits and '{fraction}' fraction digits."
    )
    @PositiveOrZero(message = "Menu's fats '${validatedValue}' must be positive or '0'.")
    private BigDecimal fats;

    @Column(name = "CARBS")
    @Digits(
            integer = 7,
            fraction = 1,
            message = "Carbs '${validatedValue}' must have up to '{integer}' integer digits and '{fraction}' fraction digits."
    )
    @PositiveOrZero(message = "Menu's carbs '${validatedValue}' must be positive or '0'.")
    private BigDecimal carbs;

    @Column(name = "IS_ACTIVE")
    @NotNull(message = "Menu must have field 'isActive' defined.")
    private Boolean isActive;

    @Column(name = "IS_ARCHIVED")
    private boolean isArchived;

    @Column(name = "AUTHOR_NAME")
    @Size(max = 255, message = "Author name's length of the menu '${validatedValue}' mustn't be more than '{max}' characters long.")
    private String authorName;

    @Column(name = "AUTHOR_IMAGE_LINK")
    @Size(max = 255, message = "Author image link's length of the menu '${validatedValue}' mustn't be more than '{max}' characters long.")
    private String authorImageLink;

    @Column(name = "MENU_DESCRIPTION")
    @Size(max = 1000, message = "Menu description's length '${validatedValue}' mustn't be more than '{max}' characters long.")
    private String menuDescription;

    @ManyToOne
    @JoinColumn(name = "MENU_CATEGORY_ID")
    @Valid
    private MenuCategory menuCategory;

    @ManyToOne
    @JoinColumn(name = "OWNERSHIP_ID")
    @Valid
    @NotNull(message = "Menu's ownership mustn't be null.")
    private Ownership ownership;

    private Integer weekNumber;

    public Menu(String name, Boolean isActive, Ownership ownership) {
        this.name = name;
        this.isActive = isActive;
        this.ownership = ownership;
    }

    @PrePersist
    @PreUpdate
    private void prepareData(){
        this.calories = calories == null ? BigDecimal.ZERO : calories;
        this.carbs = carbs == null ? BigDecimal.ZERO : carbs;
        this.fats = fats == null ? BigDecimal.ZERO : fats;
        this.proteins = proteins == null ? BigDecimal.ZERO : proteins;
        this.authorName = authorName == null ? "" : authorName;
        this.authorImageLink = authorImageLink == null ? "" : authorImageLink;
        this.menuDescription = menuDescription == null ? "" : menuDescription;
        this.name = name == null ? null : StringUtils.capitalize(name);
    }
}
