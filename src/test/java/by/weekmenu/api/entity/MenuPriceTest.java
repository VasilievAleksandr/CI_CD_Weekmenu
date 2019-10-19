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

public class MenuPriceTest {

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
        return new Menu("Бюджетное", true, new Ownership(OwnershipName.USER));
    }

    private Currency getCurrency() {
        return new Currency("руб", "BYN", false);
    }

    private Region getRegion() {
        Country country = new Country("Беларусь", "BY", getCurrency());
        return new Region("Минский район", country);
    }

    @Test
    public void menuPricePriceValueHasTooManyFractionDigits() {
        MenuPrice menuPrice = new MenuPrice(getMenu(), getRegion());
        menuPrice.setPriceValue(new BigDecimal("111.123"));
        Set<ConstraintViolation<MenuPrice>> violations = validator.validate(menuPrice);
        assertEquals(violations.size(), 1);
        assertEquals("Menu_Price's Price_Value '111.123' must have up to '7' integer digits and '2' fraction digits.",
                violations.iterator().next().getMessage());
    }


    @Test
    public void testMenuPricePriceValueIsTooHigh() {
        MenuPrice menuPrice = new MenuPrice(getMenu(), getRegion());
        menuPrice.setPriceValue(new BigDecimal("1111111111.123"));
        Set<ConstraintViolation<MenuPrice>> violations = validator.validate(menuPrice);
        assertEquals(violations.size(), 1);
        assertEquals("Menu_Price's Price_Value '1111111111.123' must have up to '7' integer digits and '2' fraction digits.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testMenuPricePriceValueIsNegative() {
        MenuPrice menuPrice = new MenuPrice(getMenu(), getRegion());
        menuPrice.setPriceValue(new BigDecimal("-111"));
        Set<ConstraintViolation<MenuPrice>> violations = validator.validate(menuPrice);
        assertEquals(violations.size(), 1);
        assertEquals("Menu_Price's Price_Value '-111' must be positive.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testMenuIsNull() {
        MenuPrice menuPrice = new MenuPrice(null, getRegion());
        menuPrice.setPriceValue(new BigDecimal("123.12"));
        Set<ConstraintViolation<MenuPrice>> violations = validator.validate(menuPrice);
        assertEquals(violations.size(), 1);
        assertEquals("MenuPrice must have menu.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testMenuIsInvalid() {
        MenuPrice menuPrice = new MenuPrice(
                new Menu("Бюджетное", true, null),
                getRegion()
        );
        menuPrice.setPriceValue(new BigDecimal("123.12"));
        Set<ConstraintViolation<MenuPrice>> violations = validator.validate(menuPrice);
        assertEquals(violations.size(), 1);
        assertEquals("Menu's ownership mustn't be null.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testRegionIsNull() {
        MenuPrice menuPrice = new MenuPrice(getMenu(), null);
        menuPrice.setPriceValue(new BigDecimal("111.12"));
        Set<ConstraintViolation<MenuPrice>> violations = validator.validate(menuPrice);
        assertEquals(violations.size(), 1);
        assertEquals("MenuPrice must have region.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testRegionIsInvalid() {
        MenuPrice menuCurrency = new MenuPrice(
                getMenu(),
                new Region("Минский район", null)
        );
        menuCurrency.setPriceValue(new BigDecimal("111.12"));
        Set<ConstraintViolation<MenuPrice>> violations = validator.validate(menuCurrency);
        assertEquals(violations.size(), 1);
        assertEquals("Region's Country mustn't be null.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testMenuPriceIsValid() {
        MenuPrice menuCurrency = new MenuPrice(getMenu(), getRegion());
        menuCurrency.setPriceValue(new BigDecimal("111.12"));
        Set<ConstraintViolation<MenuPrice>> violations = validator.validate(menuCurrency);
        assertEquals(violations.size(), 0);
    }
}