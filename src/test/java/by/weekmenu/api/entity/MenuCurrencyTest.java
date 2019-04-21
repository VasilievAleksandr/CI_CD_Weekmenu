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

public class MenuCurrencyTest {

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

    private Menu getMenu() {
        return new Menu("Бюджетное", true, new Ownership("Пользователь"));
    }

    private Currency getCurrency() {
        return new Currency("руб", "BYN", "$", true);
    }

    @Test
    public void dailyMenuCurrencyPriceValueHasTooManyFractionDigits() {
        MenuCurrency menuCurrency = new MenuCurrency(getMenu(), getCurrency());
        menuCurrency.setPriceValue(new BigDecimal("111.123"));
        Set<ConstraintViolation<MenuCurrency>> violations = validator.validate(menuCurrency);
        assertEquals(violations.size(), 1);
        assertEquals("Menu_Currency's Price_Value '111.123' must have up to '7' integer digits and '2' fraction digits.",
                violations.iterator().next().getMessage());
    }


    @Test
    public void testMenuCurrencyPriceValueIsTooHigh() {
        MenuCurrency menuCurrency = new MenuCurrency(getMenu(), getCurrency());
        menuCurrency.setPriceValue(new BigDecimal("1111111111.123"));
        Set<ConstraintViolation<MenuCurrency>> violations = validator.validate(menuCurrency);
        assertEquals(violations.size(), 1);
        assertEquals("Menu_Currency's Price_Value '1111111111.123' must have up to '7' integer digits and '2' fraction digits.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testMenuCurrencyPriceValueIsNegative() {
        MenuCurrency menuCurrency = new MenuCurrency(getMenu(), getCurrency());
        menuCurrency.setPriceValue(new BigDecimal("-111"));
        Set<ConstraintViolation<MenuCurrency>> violations = validator.validate(menuCurrency);
        assertEquals(violations.size(), 1);
        assertEquals("Menu_Currency's Price_Value '-111' must be positive.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testMenuIsNull() {
        MenuCurrency menuCurrency = new MenuCurrency(null, getCurrency());
        menuCurrency.setPriceValue(new BigDecimal("123.12"));
        Set<ConstraintViolation<MenuCurrency>> violations = validator.validate(menuCurrency);
        assertEquals(violations.size(), 1);
        assertEquals("MenuCurrency must have menu.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testMenuIsInvalid() {
        MenuCurrency menuCurrency = new MenuCurrency(
                new Menu("Бюджетное", true, null),
                getCurrency()
        );
        menuCurrency.setPriceValue(new BigDecimal("123.12"));
        Set<ConstraintViolation<MenuCurrency>> violations = validator.validate(menuCurrency);
        assertEquals(violations.size(), 1);
        assertEquals("Menu's ownership mustn't be null.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testCurrencyIsNull() {
        MenuCurrency menuCurrency = new MenuCurrency(getMenu(), null);
        menuCurrency.setPriceValue(new BigDecimal("111.12"));
        Set<ConstraintViolation<MenuCurrency>> violations = validator.validate(menuCurrency);
        assertEquals(violations.size(), 1);
        assertEquals("MenuCurrency must have currency.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testCurrencyIsInvalid() {
        MenuCurrency menuCurrency = new MenuCurrency(
                getMenu(),
                new Currency(null, "BYN", "$", true)
        );
        menuCurrency.setPriceValue(new BigDecimal("111.12"));
        Set<ConstraintViolation<MenuCurrency>> violations = validator.validate(menuCurrency);
        assertEquals(violations.size(), 1);
        assertEquals("Currency must have name.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testDailyMenuStatisticsIsValid() {
        MenuCurrency menuCurrency = new MenuCurrency(getMenu(), getCurrency());
        menuCurrency.setPriceValue(new BigDecimal("111.12"));
        Set<ConstraintViolation<MenuCurrency>> violations = validator.validate(menuCurrency);
        assertEquals(violations.size(), 0);
    }
}