package by.weekmenu.api.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(exclude = {"id","dailyMenuStatisticsPrices"})
@Entity
@Table(name = "DAILY_MENU_STATISTICS")
public class DailyMenuStatistics implements Serializable {

    private static final long serialVersionUID = 3756286465717228068L;

    @Embeddable
    public static class Id implements Serializable {

        private static final long serialVersionUID = 5029998730923370407L;

        @Column(name = "DAY_OF_WEEK_ID")
        private Byte dayOfWeekId;

        @Column(name = "MENU_ID")
        private Long menuId;

        Id() {

        }

        public Id(Byte dayOfWeekId, Long menuId) {
            this.dayOfWeekId = dayOfWeekId;
            this.menuId = menuId;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Id id = (Id) o;
            return Objects.equals(dayOfWeekId, id.dayOfWeekId) &&
                    Objects.equals(menuId, id.menuId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(dayOfWeekId, menuId);
        }

        public Byte getDayOfWeekId() {
            return dayOfWeekId;
        }

        public Long getMenuId() {
            return menuId;
        }
    }

    @EmbeddedId
    private Id id = new Id();

    @Column(name = "CALORIES")
    @Positive(message = "DailyMenuStatistics' calories '${validatedValue}' must be positive.")
    private Integer calories;

    @Column(name = "PROTEINS")
    @PositiveOrZero(message = "DailyMenuStatistics' proteins '${validatedValue}' must be positive or '0'.")
    private Integer proteins;

    @Column(name = "FATS")
    @PositiveOrZero(message = "DailyMenuStatistics' fats '${validatedValue}' must be positive or '0'.")
    private Integer fats;

    @Column(name = "CARBS")
    @PositiveOrZero(message = "DailyMenuStatistics' carbs '${validatedValue}' must be positive or '0'.")
    private Integer carbs;

    @OneToMany(mappedBy = "dailyMenuStatistics", cascade = CascadeType.PERSIST)
    private Set<
            @Valid
            @NotNull(message = "DailyMenuStatistics must have list of DailyMenuStatisticsCurrencies without null elements.")
                    DailyMenuStatisticsPrice> dailyMenuStatisticsPrices = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "DAY_OF_WEEK_ID",
            updatable = false,
            insertable = false
    )
    @NotNull(message = "DailyMenuStatistics must have have dayOfWeek.")
    private DayOfWeek dayOfWeek;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "MENU_ID",
            updatable = false,
            insertable = false
    )
    @NotNull(message = "DailyMenuStatistics must have have menu.")
    private Menu menu;

    public DailyMenuStatistics(DayOfWeek dayOfWeek, Menu menu) {
        this.dayOfWeek = dayOfWeek;
        this.menu = menu;
    }
}
