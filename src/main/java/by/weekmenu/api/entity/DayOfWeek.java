package by.weekmenu.api.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;

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
    private Short id;

    @Column(name = "NAME", unique = true)
    @NotBlank(message = "DayOfWeek must have name.")
    private String name;

    @Column(name = "SHORT_NAME", unique = true)
    @NotBlank(message = "DayOfWeek must have shortName.")
    private String shortName;

    public DayOfWeek(WeekDay weekDay) {
        this.name = weekDay.getFullName();
        this.shortName = weekDay.getShortName();
    }

    public DayOfWeek(String name, String shortName) {
        this.name = name;
        this.shortName = shortName;
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

    private final String shortName;
    private final String fullName;

    WeekDay(String shortName, String fullName) {
        this.shortName = shortName;
        this.fullName = fullName;
    }

    String getShortName() {
        return shortName;
    }

    String getFullName() {
        return fullName;
    }
}
