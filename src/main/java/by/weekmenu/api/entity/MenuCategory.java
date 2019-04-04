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
@Table(name = "MENU_CATEGORY")
public class MenuCategory implements Serializable {

    private static final long serialVersionUID = 6121210274126953841L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MENU_CATEGORY_ID")
    private Integer id;

    @Column(name = "MENU_CATEGORY_NAME")
    private String name;

    @Column(name = "MENU_CATEGORY_PRIORITY")
    private Integer priority;

    @Column(name = "MENU_CATEGORY_IMAGE_LINK")
    private String imageLink;
}
