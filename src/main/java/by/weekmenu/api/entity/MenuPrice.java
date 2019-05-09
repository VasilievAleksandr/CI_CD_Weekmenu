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
@Table(name = "MENU_PRICE")
public class MenuPrice implements Serializable {

    private static final long serialVersionUID = -5276633775282339542L;

    @Embeddable
    @Getter
    @NoArgsConstructor
    public static class Id implements Serializable {

        private static final long serialVersionUID = 5836478275051504090L;

        @Column(name = "MENU__ID")
        private Long menuId;

        @Column(name = "REGION_ID")
        private Byte regionId;

        public Id(Long menuId, Byte regionId) {
            this.menuId = menuId;
            this.regionId = regionId;
        }

        public boolean equals(Object o) {
            if (o != null && o instanceof Id) {
                Id that = (Id) o;
                return this.menuId.equals(that.menuId) && this.regionId.equals(that.regionId);
            }

            return false;
        }

        public int hashCode() {
            return menuId.hashCode() + regionId.hashCode();
        }
    }

    @EmbeddedId
    private Id id = new Id();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MENU_ID",
            updatable = false,
            insertable = false)
    @Valid
    @NotNull(message = "MenuPrice must have menu.")
    private Menu menu;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "REGION_ID",
            updatable = false,
            insertable = false)
    @Valid
    @NotNull(message = "MenuPrice must have region.")
    private Region region;

    @Column(name = "PRICE_VALUE")
    @Digits(
            integer = 7,
            fraction = 2,
            message = "Menu_Price's Price_Value '${validatedValue}' must have up to '{integer}' integer digits and '{fraction}' fraction digits."
    )
    @Positive(message = "Menu_Price's Price_Value '${validatedValue}' must be positive.")
    private BigDecimal priceValue;

    public MenuPrice(Menu menu, Region region) {
        this.menu = menu;
        this.region = region;
    }
}
