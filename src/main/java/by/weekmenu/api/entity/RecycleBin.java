package by.weekmenu.api.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(exclude = {"id"})
@Entity
@Table(name = "RECYCLE_BIN")
public class RecycleBin implements Serializable {

    private static final long serialVersionUID = -5182856416666586866L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "ELEMENT_NAME")
    @NotBlank(message = "Recycle Bin must have element name.")
    @Size(max = 255, message = "Element name '${validatedValue}' must be '{max}' characters long")
    private String elementName;

    @Column(name = "ENTITY_NAME", unique = true)
    @NotBlank(message = "Recycle Bin must have element name.")
    @Size(max = 255, message = "Element name '${validatedValue}' must be '{max}' characters long")
    private String entityName;

    @Column(name = "TIME")
    @NotNull(message = "Recycle Bin must have delete date.")
    private LocalDateTime deleteDate;
}
