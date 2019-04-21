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

public class DailyMenuStatisticsCurrencyTest {

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

    @Test
    public void dailyDailyMenuStatisticsCurrencyPriceValueHasTooManyFractionDigits() {
        DailyMenuStatisticsCurrency dailyMenuStatisticsCurrency = new DailyMenuStatisticsCurrency(getDailyMenuStatistics(),
                new Currency("руб", "BYN", "$", true));
        dailyMenuStatisticsCurrency.setPriceValue(new BigDecimal("111.123"));
        Set<ConstraintViolation<DailyMenuStatisticsCurrency>> violations = validator.validate(dailyMenuStatisticsCurrency);
        assertEquals(violations.size(), 1);
        assertEquals("Daily_Menu_Statistics_Currency's Price_Value '111.123' must have up to '7' integer digits and '2' fraction digits.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testDailyMenuStatisticsCurrencyPriceValueIsTooHigh() {
        DailyMenuStatisticsCurrency dailyMenuStatisticsCurrency = new DailyMenuStatisticsCurrency(getDailyMenuStatistics(),
                new Currency("руб", "BYN", "$", true));
        dailyMenuStatisticsCurrency.setPriceValue(new BigDecimal("1111111111.123"));
        Set<ConstraintViolation<DailyMenuStatisticsCurrency>> violations = validator.validate(dailyMenuStatisticsCurrency);
        assertEquals(violations.size(), 1);
        assertEquals("Daily_Menu_Statistics_Currency's Price_Value '1111111111.123' must have up to '7' integer digits and '2' fraction digits.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testDailyMenuStatisticsCurrencyPriceValueIsNegative() {
        DailyMenuStatisticsCurrency dailyMenuStatisticsCurrency = new DailyMenuStatisticsCurrency(getDailyMenuStatistics(),
                new Currency("руб", "BYN", "$", true));
        dailyMenuStatisticsCurrency.setPriceValue(new BigDecimal("-111"));
        Set<ConstraintViolation<DailyMenuStatisticsCurrency>> violations = validator.validate(dailyMenuStatisticsCurrency);
        assertEquals(violations.size(), 1);
        assertEquals("Daily_Menu_Statistics_Currency's Price_Value '-111' must be positive.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testDailyMenuStatisticsIsNull() {
        DailyMenuStatisticsCurrency dailyMenuStatisticsCurrency = new DailyMenuStatisticsCurrency(null,
                new Currency("руб", "BYN", "$", true));
        dailyMenuStatisticsCurrency.setPriceValue(new BigDecimal("111.12"));
        Set<ConstraintViolation<DailyMenuStatisticsCurrency>> violations = validator.validate(dailyMenuStatisticsCurrency);
        assertEquals(violations.size(), 1);
        assertEquals("DailyMenuStatisticsCurrency must have dailyMenuStatistics.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testDailyMenuStatisticsIsInvalid() {
        DailyMenuStatisticsCurrency dailyMenuStatisticsCurrency = new DailyMenuStatisticsCurrency(
                new DailyMenuStatistics(null, new Menu("Бюджетное", true, new Ownership("Пользователь"))),
                new Currency("руб", "BYN", "$", true)
        );
        dailyMenuStatisticsCurrency.setPriceValue(new BigDecimal("111.12"));
        Set<ConstraintViolation<DailyMenuStatisticsCurrency>> violations = validator.validate(dailyMenuStatisticsCurrency);
        assertEquals(violations.size(), 1);
        assertEquals("DailyMenuStatistics must have have dayOfWeek.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testCurrencyIsNull() {
        DailyMenuStatisticsCurrency dailyMenuStatisticsCurrency = new DailyMenuStatisticsCurrency(getDailyMenuStatistics(),
                null);
        dailyMenuStatisticsCurrency.setPriceValue(new BigDecimal("111.12"));
        Set<ConstraintViolation<DailyMenuStatisticsCurrency>> violations = validator.validate(dailyMenuStatisticsCurrency);
        assertEquals(violations.size(), 1);
        assertEquals("DailyMenuStatisticsCurrency must have currency.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testCurrencyIsInvalid() {
        DailyMenuStatisticsCurrency dailyMenuStatisticsCurrency = new DailyMenuStatisticsCurrency(getDailyMenuStatistics(),
                new Currency(null, "BYN", "$", true));
        dailyMenuStatisticsCurrency.setPriceValue(new BigDecimal("111.12"));
        Set<ConstraintViolation<DailyMenuStatisticsCurrency>> violations = validator.validate(dailyMenuStatisticsCurrency);
        assertEquals(violations.size(), 1);
        assertEquals("Currency must have name.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testDailyMenuStatisticsIsValid() {
        DailyMenuStatisticsCurrency dailyMenuStatisticsCurrency = new DailyMenuStatisticsCurrency(getDailyMenuStatistics(),
                new Currency("руб", "BYN", "$", true));
        dailyMenuStatisticsCurrency.setPriceValue(new BigDecimal("111.12"));
        Set<ConstraintViolation<DailyMenuStatisticsCurrency>> violations = validator.validate(dailyMenuStatisticsCurrency);
        assertEquals(violations.size(), 0);
    }
}