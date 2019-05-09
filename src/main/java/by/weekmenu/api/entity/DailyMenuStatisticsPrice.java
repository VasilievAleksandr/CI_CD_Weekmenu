package by.weekmenu.api.entity;

import lombok.*;

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
@Table(name = "DAILY_MENU_STATISTICS_CURRENCY")
public class DailyMenuStatisticsPrice implements Serializable {

    private static final long serialVersionUID = 2012039841615767284L;

    @Embeddable
    @Getter
    @NoArgsConstructor
    public static class Id implements Serializable {

        private static final long serialVersionUID = 51064229057120914L;

        @Column(name = "DAILY_MENU_STATISTICS_ID")
        private Long dailyMenuStatisticsId;

        @Column(name = "REGION_ID")
        private Byte regionId;

        public Id(Long dailyMenuStatisticsId, Byte regionId) {
            this.dailyMenuStatisticsId = dailyMenuStatisticsId;
            this.regionId = regionId;
        }

        public boolean equals(Object o) {
            if (o != null && o instanceof Id) {
                Id that = (Id) o;
                return this.dailyMenuStatisticsId.equals(that.dailyMenuStatisticsId) && this.regionId.equals(that.regionId);
            }

            return false;
        }

        public int hashCode() {
            return dailyMenuStatisticsId.hashCode() + regionId.hashCode();
        }
    }

    @EmbeddedId
    private Id id = new Id();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DAILY_MENU_STATISTICS_ID",
            updatable = false,
            insertable = false)
    @Valid
    @NotNull(message = "DailyMenuStatisticsPrice must have dailyMenuStatistics.")
    private DailyMenuStatistics dailyMenuStatistics;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "REGION_ID",
            updatable = false,
            insertable = false)
    @Valid
    @NotNull(message = "DailyMenuStatisticsPrice must have region.")
    private Region region;

    @Column(name = "PRICE_VALUE")
    @Digits(
            integer = 7,
            fraction = 2,
            message = "Daily_Menu_Statistics_Price's Price_Value '${validatedValue}' must have up to '{integer}' integer digits and '{fraction}' fraction digits."
    )
    @Positive(message = "Daily_Menu_Statistics_Price's Price_Value '${validatedValue}' must be positive.")
    private BigDecimal priceValue;

    public DailyMenuStatisticsPrice(DailyMenuStatistics dailyMenuStatistics, Region region) {
        this.dailyMenuStatistics = dailyMenuStatistics;
        this.region = region;
    }
}
