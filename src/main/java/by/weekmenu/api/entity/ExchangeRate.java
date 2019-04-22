package by.weekmenu.api.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(exclude = "id")
@Entity
@Table(name = "EXCHANGE_RATE")
public class ExchangeRate implements Serializable {

    private static final long serialVersionUID = -4774703478275007558L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "DATE")
    @NotNull(message = "ExchangeRate must have date.")
    private LocalDate date;

    @Column(name = "RATE")
    @NotNull(message = "ExchangeRate must have rate.")
    @Digits(
            integer = 7,
            fraction = 2,
            message = "ExchangeRate's rate '${validatedValue}' must have up to '{integer}' integer digits and '{fraction}' fraction digits."
    )
    @Positive(message = "ExchangeRate's rate '${validatedValue}' must be positive.")
    private BigDecimal rate;

    @ManyToOne
    @JoinColumn(name = "BANK_ID")
    @Valid
    @NotNull(message = "ExchangeRate must have bank.")
    private Bank bank;

    @ManyToOne
    @JoinColumn(name = "CURRENCY_ID")
    @NotNull(message = "ExchangeRate must have currency.")
    @Valid
    private Currency currency;

    public ExchangeRate(LocalDate date, BigDecimal rate, Bank bank, Currency currency) {
        this.date = date;
        this.rate = rate;
        this.bank = bank;
        this.currency = currency;
    }
}
