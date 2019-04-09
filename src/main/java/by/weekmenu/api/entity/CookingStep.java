package by.weekmenu.api.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import javax.validation.Valid;
import lombok.EqualsAndHashCode;

@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(exclude = {"id", "imageLink", "recipe"})
@Entity
@Table(name = "COOKING_STEP")
public class CookingStep implements Serializable {

    private static final long serialVersionUID = 1004642071168789374L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Integer id;

    @Column(name = "PRIORITY")
    private Integer priority;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "IMAGELINK")
    private String imageLink;

    public CookingStep(Integer priority, String description, String imageLink) {
        this.priority = priority;
        this.description = description;
        this.imageLink = imageLink;
    }
}
