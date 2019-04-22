package by.weekmenu.api.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(exclude = "id")
@Entity
@Table(name = "BANK")
public class Bank implements Serializable {

    private static final long serialVersionUID = -7695653971095472593L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Byte id;

    @Column(name = "NAME", unique = true)
    @NotBlank(message = "Bank must have name.")
    private String name;

    @OneToOne
    @JoinColumn(name = "CURRENCY_ID")
    @NotNull(message = "Bank must have base currency")
    private Currency baseCurrency;

    public Bank(String name, Currency baseCurrency) {
        this.name = name;
        this.baseCurrency = baseCurrency;
    }
}
