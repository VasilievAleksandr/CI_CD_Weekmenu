package by.weekmenu.api.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

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

    @Column(name = "PRICE")
    private BigDecimal price;

    @Column(name = "CALORIES")
    private Integer calories;

    @Column(name = "PROTEINS")
    private Integer proteins;

    @Column(name = "FATS")
    private Integer fats;

    @Column(name = "CARBS")
    private Integer carbs;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "DAY_OF_WEEK_ID",
            updatable = false,
            insertable = false
    )
    private DayOfWeek dayOfWeek;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "MENU_ID",
            updatable = false,
            insertable = false
    )
    private Menu menu;
}
