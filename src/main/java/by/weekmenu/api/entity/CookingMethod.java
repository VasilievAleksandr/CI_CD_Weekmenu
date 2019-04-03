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
public class CookingMethod implements Serializable {

    private static final long serialVersionUID = 1003642071168789374L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer cookingMethodId;
    
    @OneToOne(fetch = FetchType.LAZY)
    @PrimaryKeyJoinColumn
    private Recipe recipe;
    
    @Column
    private String name;

    public CookingMethod(String name) {
        this.name = name;
    }

}
