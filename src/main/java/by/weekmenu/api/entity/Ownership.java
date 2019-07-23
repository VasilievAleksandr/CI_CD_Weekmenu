package by.weekmenu.api.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;


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
    @Column(name = "ID")
    private Long id;

    @Column (name = "NAME", unique = true)
    @NotNull(message = "Ownership must have name.")
    private String name;

    public Ownership(OwnershipName name) {
        this.name = name.name();
    }
}
