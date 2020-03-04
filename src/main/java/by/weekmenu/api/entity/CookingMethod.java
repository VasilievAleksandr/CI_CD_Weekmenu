package by.weekmenu.api.entity;


import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;

@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(exclude = {"id"})
@Entity
@Table(name = "COOKING_METHOD")
public class CookingMethod implements Serializable {

    private static final long serialVersionUID = 1003642071168789374L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Integer id;

    @Column(name = "NAME")
    @NotBlank(message = "Cooking Method must have name.")
    @Size(max = 255,
            message = "CookingMethod's name '${validatedValue}' mustn't be more than '{max}' characters long."
    )
    private String name;

    @Column(name = "IS_ARCHIVED")
    private boolean isArchived;

    @Column(name = "PRIORITY")
    private Integer priority;

    public CookingMethod(String name) {
        this.name = name;
    }

    public CookingMethod(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    @PrePersist
    @PreUpdate
    private void nameFirstCapitalLetter(){
        this.name = name == null ? null : StringUtils.capitalize(name);
    }
}
