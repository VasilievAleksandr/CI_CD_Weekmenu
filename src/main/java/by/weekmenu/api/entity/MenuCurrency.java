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

@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(exclude = "id")
@Entity
@Table(name = "MENU_CURRENCY")
public class MenuCurrency implements Serializable {

    private static final long serialVersionUID = -5276633775282339542L;

    @Embeddable
    @Getter
    @NoArgsConstructor
    public static class Id implements Serializable {

        private static final long serialVersionUID = 5836478275051504090L;

        @Column(name = "MENU__ID")
        private Long menuId;

        @Column(name = "CURRENCY_ID")
        private Byte currencyId;

        public Id(Long menuId, Byte currencyId) {
            this.menuId = menuId;
            this.currencyId = currencyId;
        }

        public boolean equals(Object o) {
            if (o != null && o instanceof Id) {
                Id that = (Id) o;
                return this.menuId.equals(that.menuId) && this.currencyId.equals(that.currencyId);
            }

            return false;
        }

        public int hashCode() {
            return menuId.hashCode() + currencyId.hashCode();
        }
    }

    @EmbeddedId
    private Id id = new Id();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MENU_ID")
    @Valid
    @NotNull(message = "MenuCurrency must have menu.")
    private Menu menu;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CURRENCY_ID")
    @Valid
    @NotNull(message = "MenuCurrency must have currency.")
    private Currency currency;

    @Column(name = "PRICE_VALUE")
    @Digits(
            integer = 7,
            fraction = 2,
            message = "Menu_Currency's Price_Value '${validatedValue}' must have up to '{integer}' integer digits and '{fraction}' fraction digits."
    )
    @Positive(message = "Menu_Currency's Price_Value '${validatedValue}' must be positive.")
    private BigDecimal priceValue;

    public MenuCurrency(Menu menu, Currency currency) {
        this.menu = menu;
        this.currency = currency;
    }
}
