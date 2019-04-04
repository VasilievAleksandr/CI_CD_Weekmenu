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
@Table(name = "DISH_TYPE")
public class DishType implements Serializable {

    private static final long serialVersionUID = -8986552656545603913L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "DISH_TYPE_ID")
    private Byte id;

    @Column(name = "DISH_TYPE_NAME")
    private String name;

    @Column(name = "DISH_TYPE_PRIORITY")
    private Byte priority;
}
