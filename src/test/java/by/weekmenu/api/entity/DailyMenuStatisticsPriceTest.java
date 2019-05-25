package by.weekmenu.api.entity;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import java.math.BigDecimal;
import java.util.Set;

import static org.junit.Assert.*;

public class DailyMenuStatisticsPriceTest {

    private static ValidatorFactory validatorFactory;
    private static Validator validator;

    @Before
    public void setUp() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @After
    public void tearDown() {
        validatorFactory.close();
    }

    private DailyMenuStatistics getDailyMenuStatistics() {
        DailyMenuStatistics dailyMenuStatistics = new DailyMenuStatistics();
        dailyMenuStatistics.setDayOfWeek(new DayOfWeek(WeekDay.MONDAY));
        dailyMenuStatistics.setMenu(new Menu("Бюджетное", true, new Ownership("Пользователь")));
        return dailyMenuStatistics;
    }

    private Region getRegion() {
        Country country = new Country("Беларусь", "BY", getCurrency());
        return new Region("Минский район", country);
    }

    private Currency getCurrency() {
        return new Currency("руб.", "BYN", true);
    }

    @Test
    public void dailyDailyMenuStatisticsPricePriceValueHasTooManyFractionDigits() {
        DailyMenuStatisticsPrice dailyMenuStatisticsPrice = new DailyMenuStatisticsPrice(getDailyMenuStatistics(),
                getRegion());
        dailyMenuStatisticsPrice.setPriceValue(new BigDecimal("111.123"));
        Set<ConstraintViolation<DailyMenuStatisticsPrice>> violations = validator.validate(dailyMenuStatisticsPrice);
        assertEquals(violations.size(), 1);
        assertEquals("Daily_Menu_Statistics_Price's Price_Value '111.123' must have up to '7' integer digits and '2' fraction digits.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testDailyMenuStatisticsPricePriceValueIsTooHigh() {
        DailyMenuStatisticsPrice dailyMenuStatisticsPrice = new DailyMenuStatisticsPrice(getDailyMenuStatistics(),
                getRegion());
        dailyMenuStatisticsPrice.setPriceValue(new BigDecimal("1111111111.123"));
        Set<ConstraintViolation<DailyMenuStatisticsPrice>> violations = validator.validate(dailyMenuStatisticsPrice);
        assertEquals(violations.size(), 1);
        assertEquals("Daily_Menu_Statistics_Price's Price_Value '1111111111.123' must have up to '7' integer digits and '2' fraction digits.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testDailyMenuStatisticsPricePriceValueIsNegative() {
        DailyMenuStatisticsPrice dailyMenuStatisticsPrice = new DailyMenuStatisticsPrice(getDailyMenuStatistics(),
                getRegion());
        dailyMenuStatisticsPrice.setPriceValue(new BigDecimal("-111"));
        Set<ConstraintViolation<DailyMenuStatisticsPrice>> violations = validator.validate(dailyMenuStatisticsPrice);
        assertEquals(violations.size(), 1);
        assertEquals("Daily_Menu_Statistics_Price's Price_Value '-111' must be positive.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testDailyMenuStatisticsIsNull() {
        DailyMenuStatisticsPrice dailyMenuStatisticsPrice = new DailyMenuStatisticsPrice(null,
                getRegion());
        dailyMenuStatisticsPrice.setPriceValue(new BigDecimal("111.12"));
        Set<ConstraintViolation<DailyMenuStatisticsPrice>> violations = validator.validate(dailyMenuStatisticsPrice);
        assertEquals(violations.size(), 1);
        assertEquals("DailyMenuStatisticsPrice must have dailyMenuStatistics.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testDailyMenuStatisticsIsInvalid() {
        DailyMenuStatisticsPrice dailyMenuStatisticsPrice = new DailyMenuStatisticsPrice(
                new DailyMenuStatistics(null, new Menu("Бюджетное", true, new Ownership("Пользователь"))),
                getRegion()
        );
        dailyMenuStatisticsPrice.setPriceValue(new BigDecimal("111.12"));
        Set<ConstraintViolation<DailyMenuStatisticsPrice>> violations = validator.validate(dailyMenuStatisticsPrice);
        assertEquals(violations.size(), 1);
        assertEquals("DailyMenuStatistics must have have dayOfWeek.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testRegionIsNull() {
        DailyMenuStatisticsPrice dailyMenuStatisticsPrice = new DailyMenuStatisticsPrice(getDailyMenuStatistics(),
                null);
        dailyMenuStatisticsPrice.setPriceValue(new BigDecimal("111.12"));
        Set<ConstraintViolation<DailyMenuStatisticsPrice>> violations = validator.validate(dailyMenuStatisticsPrice);
        assertEquals(violations.size(), 1);
        assertEquals("DailyMenuStatisticsPrice must have region.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testRegionIsInvalid() {
        DailyMenuStatisticsPrice dailyMenuStatisticsPrice = new DailyMenuStatisticsPrice(getDailyMenuStatistics(),
                new Region("Минский район", null));
        dailyMenuStatisticsPrice.setPriceValue(new BigDecimal("111.12"));
        Set<ConstraintViolation<DailyMenuStatisticsPrice>> violations = validator.validate(dailyMenuStatisticsPrice);
        assertEquals(violations.size(), 1);
        assertEquals("Region's Country mustn't be null.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testDailyMenuStatisticsIsValid() {
        DailyMenuStatisticsPrice dailyMenuStatisticsPrice = new DailyMenuStatisticsPrice(getDailyMenuStatistics(),
                getRegion());
        dailyMenuStatisticsPrice.setPriceValue(new BigDecimal("111.12"));
        Set<ConstraintViolation<DailyMenuStatisticsPrice>> violations = validator.validate(dailyMenuStatisticsPrice);
        assertEquals(violations.size(), 0);
    }
}