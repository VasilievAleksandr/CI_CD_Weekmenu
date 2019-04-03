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
public class UnitOfMeasure implements Serializable {

    private static final long serialVersionUID = 1000642071168789374L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @OneToOne(fetch = FetchType.LAZY)
    @PrimaryKeyJoinColumn
    private Ingredient ingredient;

    public UnitOfMeasure(String yourOwnUnitOfMeasure) {
        this.name = yourOwnUnitOfMeasure;
    }

    public UnitOfMeasure(StandartUnits StandartUnits) {
        this.name = StandartUnits.getName();
    }
}

enum StandartUnits {

    kilogramm("кг"),
    gram("г"),
    piece("шт"),
    litr("л"),
    millilitr("мл");

    private final String name;

    StandartUnits(String name) {
        this.name = name;
    }

    String getName() {
        return name;
    }

}
