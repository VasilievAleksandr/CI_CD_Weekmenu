package by.weekmenu.api.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table
public class DayOfWeek implements Serializable {

    private static final long serialVersionUID = 8939642071168789374L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Byte id;

    @Column
    private String name;

    @Column
    private String shortName;

    public DayOfWeek(WeekDay weekDay) {
        this.name = weekDay.getFullName();
        this.shortName = weekDay.getName();
    }
}

enum WeekDay {

    Monday("ПН", "Понедельник"),
    Tuesday("ВТ", "Вторник"),
    Wednesday("СР", "Среда"),
    Thursday("ЧТ", "Четверг"),
    Friday("ПТ", "Пятница"),
    Saturday("СБ", "Суббота"),
    Sunday("ВС", "Воскресенье");

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
