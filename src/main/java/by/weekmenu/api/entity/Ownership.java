package by.weekmenu.api.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import lombok.EqualsAndHashCode;

@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(exclude = {"id"})
@Entity
@Table (name = "OWNERSHIP")
public class Ownership implements Serializable {

    private static final long serialVersionUID = 1002642071168789374L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "OWNERSHIP_ID")
    private Long id;

    @Column (name = "OWNERSHIP_NAME")
    private String name;

    public Ownership(String name) {
        this.name = name;
    }
}
