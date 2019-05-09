package by.weekmenu.api.entity;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

import static org.junit.Assert.assertEquals;

public class DailyMenuStatisticsTest {

    private static ValidatorFactory validatorFactory;
    private static Validator validator;

    @Before
    public void setUp() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    private Region getRegion() {
        Country country = new Country("Беларусь", "BY", getCurrency());
        country.getRegions().add(new Region("Минская область", country));

        return new Region("Минский район", country);
    }

    private Currency getCurrency() {
        return new Currency("руб.", "BYN", true);
    }

    @Test
    public void testCaloriesAreNegative() {
        DailyMenuStatistics dailyMenuStatistics = new DailyMenuStatistics(new DayOfWeek(), new Menu());
        dailyMenuStatistics.setCalories(-100);
        Set<ConstraintViolation<DailyMenuStatistics>> violations = validator.validate(dailyMenuStatistics);
        assertEquals(violations.size(), 1);
        assertEquals("DailyMenuStatistics' calories '-100' must be positive.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testCaloriesAreZero() {
        DailyMenuStatistics dailyMenuStatistics = new DailyMenuStatistics(new DayOfWeek(), new Menu());
        dailyMenuStatistics.setCalories(0);
        Set<ConstraintViolation<DailyMenuStatistics>> violations = validator.validate(dailyMenuStatistics);
        assertEquals(violations.size(), 1);
        assertEquals("DailyMenuStatistics' calories '0' must be positive.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testProteinsAreNegative() {
        DailyMenuStatistics dailyMenuStatistics = new DailyMenuStatistics(new DayOfWeek(), new Menu());
        dailyMenuStatistics.setProteins(-100);
        Set<ConstraintViolation<DailyMenuStatistics>> violations = validator.validate(dailyMenuStatistics);
        assertEquals(violations.size(), 1);
        assertEquals("DailyMenuStatistics' proteins '-100' must be positive or '0'.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testFatsAreNegative() {
        DailyMenuStatistics dailyMenuStatistics = new DailyMenuStatistics(new DayOfWeek(), new Menu());
        dailyMenuStatistics.setFats(-100);
        Set<ConstraintViolation<DailyMenuStatistics>> violations = validator.validate(dailyMenuStatistics);
        assertEquals(violations.size(), 1);
        assertEquals("DailyMenuStatistics' fats '-100' must be positive or '0'.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testCarbsAreNegative() {
        DailyMenuStatistics dailyMenuStatistics = new DailyMenuStatistics(new DayOfWeek(), new Menu());
        dailyMenuStatistics.setCarbs(-100);
        Set<ConstraintViolation<DailyMenuStatistics>> violations = validator.validate(dailyMenuStatistics);
        assertEquals(violations.size(), 1);
        assertEquals("DailyMenuStatistics' carbs '-100' must be positive or '0'.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testHasInvalidDailyMenuStatisticsPrice() {
        DailyMenuStatistics dailyMenuStatistics = new DailyMenuStatistics(new DayOfWeek(), new Menu());
        dailyMenuStatistics.getDailyMenuStatisticsPrices().add(null);
        Set<ConstraintViolation<DailyMenuStatistics>> violations = validator.validate(dailyMenuStatistics);
        assertEquals(violations.size(), 1);
        assertEquals("DailyMenuStatistics must have list of DailyMenuStatisticsCurrencies without null elements.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testHasNoDayOfWeek() {
        DailyMenuStatistics dailyMenuStatistics = new DailyMenuStatistics();
        dailyMenuStatistics.setMenu(new Menu());
        Set<ConstraintViolation<DailyMenuStatistics>> violations = validator.validate(dailyMenuStatistics);
        assertEquals(violations.size(), 1);
        assertEquals("DailyMenuStatistics must have have dayOfWeek.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testHasNoMenu() {
        DailyMenuStatistics dailyMenuStatistics = new DailyMenuStatistics();
        dailyMenuStatistics.setDayOfWeek(new DayOfWeek(WeekDay.MONDAY));
        Set<ConstraintViolation<DailyMenuStatistics>> violations = validator.validate(dailyMenuStatistics);
        assertEquals(violations.size(), 1);
        assertEquals("DailyMenuStatistics must have have menu.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testIsValid() {
        DailyMenuStatistics dailyMenuStatistics = new DailyMenuStatistics(new DayOfWeek(WeekDay.MONDAY), new Menu());
        dailyMenuStatistics.setCarbs(100);
        dailyMenuStatistics.setFats(200);
        dailyMenuStatistics.setProteins(0);
        dailyMenuStatistics.setCalories(400);
        dailyMenuStatistics.getDailyMenuStatisticsPrices()
                .add(new DailyMenuStatisticsPrice(new DailyMenuStatistics(new DayOfWeek(WeekDay.MONDAY), new Menu())
                        , getRegion()));
        Set<ConstraintViolation<DailyMenuStatistics>> violations = validator.validate(dailyMenuStatistics);
        assertEquals(violations.size(), 0);
    }

    @After
    public void tearDown() {
        validatorFactory.close();
    }

}