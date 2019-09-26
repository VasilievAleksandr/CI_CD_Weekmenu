package by.weekmenu.api.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.io.Serializable;

@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(exclude = {"id", "priority", "imageLink"})
@Entity
@Table(name = "MENU_CATEGORY")
public class MenuCategory implements Serializable {

    private static final long serialVersionUID = 6121210274126953841L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Integer id;

    @Column(name = "NAME", unique = true)
    @NotBlank(message = "MenuCategory must have name.")
    @Size(max = 255, message = "MenuCategory's name '${validatedValue}' must be '{max}' characters long")
    private String name;

    @Column(name = "PRIORITY")
    @Positive(message = "MenuCategory's priority '${validatedValue}' must be positive.")
    private Integer priority;

    @Column(name = "IMAGE_LINK")
    @Size(
            max = 255,
            message = "ImageLink's length of the menuCategory '${validatedValue}' mustn't be more than '{max}' characters long."
    )
    private String imageLink;

    @Column(name = "IS_ARCHIVED")
    private boolean isArchived;

    public MenuCategory(String name, boolean isArchived) {
        this.name = name;
        this.isArchived = isArchived;
    }

    public MenuCategory(Integer id, String name, boolean isArchived) {
        this.id = id;
        this.name = name;
        this.isArchived = isArchived;
    }
}
