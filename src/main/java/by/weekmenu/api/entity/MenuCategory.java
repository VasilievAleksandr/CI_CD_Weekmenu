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
@EqualsAndHashCode(exclude = {"id", "priority", "imageLink"})
@Entity
@Table(name = "MENU_CATEGORY")
public class MenuCategory implements Serializable {

    private static final long serialVersionUID = 6121210274126953841L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Integer id;

    @Column(name = "NAME")
    private String name;

    @Column(name = "PRIORITY")
    private Integer priority;

    @Column(name = "IMAGE_LINK")
    private String imageLink;
}
