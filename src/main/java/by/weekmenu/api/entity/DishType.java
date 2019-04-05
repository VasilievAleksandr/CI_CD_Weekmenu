package by.weekmenu.api.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(exclude = {"id", "priority"})
@Entity
@Table(name = "DISH_TYPE")
public class DishType implements Serializable {

    private static final long serialVersionUID = -8986552656545603913L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Byte id;

    @Column(name = "NAME")
    private String name;

    @Column(name = "PRIORITY")
    private Integer priority;
}
