package by.weekmenu.api.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;


@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(exclude = {"id", "recipePrices", "dailyMenuStatisticsPrices"})
@Entity
@Table(name = "REGION")
public class Region implements Serializable {

    private static final long serialVersionUID = -7333500515825298207L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "NAME", unique = true)
    @NotBlank(message = "Region must have name.")
    @Size(max = 255,
            message = "Region's name '${validatedValue}' mustn't be more than '{max}' characters long."
    )
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "COUNTRY_ID",
            updatable = false,
            insertable = false)
    @Valid
    @NotNull(message = "Region's Country mustn't be null.")
    private Country country;

    @OneToMany(mappedBy = "regionWM", cascade = CascadeType.PERSIST)
    private Set<
            @Valid
            @NotNull(message = "Region must have list of recipePrice without null elements.")
                    RecipePrice> recipePrices = new HashSet<>();

    @OneToMany(mappedBy = "region", cascade = CascadeType.PERSIST)
    private Set<
            @Valid
            @NotNull(message = "Region must have list of dailyMenuStatisticsPrice without null elements.")
                    DailyMenuStatisticsPrice> dailyMenuStatisticsPrices = new HashSet<>();
}
