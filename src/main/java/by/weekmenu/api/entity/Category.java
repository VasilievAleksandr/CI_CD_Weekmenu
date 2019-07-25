package by.weekmenu.api.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(exclude = "id")
@Entity
@Table(name = "CATEGORY")
public class Category implements Serializable {

    private static final long serialVersionUID = 5158608755557090853L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "NAME", unique = true)
    @NotBlank(message = "Category must have name.")
    @Size(max = 255,
            message = "Category's name '${validatedValue}' mustn't be more than '{max}' characters long."
    )
    private String name;

    @Column(name = "CATEGORY_DESCRIPTION")
    @Size(max = 1200,
            message = "Category's description '${validatedValue}' mustn't be more than '{max}' characters long."
    )
    private String categoryDescription;

    public Category(String name, String categoryDescription) {
        this.name = name;
        this.categoryDescription = categoryDescription;
    }
}
