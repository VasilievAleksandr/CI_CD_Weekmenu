package by.weekmenu.api.entity;

import lombok.EqualsAndHashCode;
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
@EqualsAndHashCode(exclude = "id")
@Entity
@Table(name = "DAY_OF_WEEK")
public class DayOfWeek implements Serializable {

    private static final long serialVersionUID = 8939642071168789374L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Byte id;

    @Column(name = "NAME")
    private String name;

    @Column(name = "SHORT_NAME")
    private String shortName;

    @OneToMany(mappedBy = "dayOfWeek", cascade = CascadeType.ALL)
    private Set<DailyMenuStatistics> dailyMenuStatistics = new HashSet<>();

    public DayOfWeek(WeekDay weekDay) {
        this.name = weekDay.getFullName();
        this.shortName = weekDay.getName();
    }
}

enum WeekDay {

    MONDAY("ПН", "Понедельник"),
    TUESDAY("ВТ", "Вторник"),
    WEDNESDAY("СР", "Среда"),
    THURSDAY("ЧТ", "Четверг"),
    FRIDAY("ПТ", "Пятница"),
    SATURDAY("СБ", "Суббота"),
    SUNDAY("ВС", "Воскресенье");

    private final String name;
    private final String fullName;

    WeekDay(String name, String fullName) {
        this.name = name;
        this.fullName = fullName;
    }

    String getName() {
        return name;
    }

    String getFullName() {
        return fullName;
    }
}
