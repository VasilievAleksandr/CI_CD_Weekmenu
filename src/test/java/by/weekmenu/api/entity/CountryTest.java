package by.weekmenu.api.entity;

import org.apache.commons.lang3.StringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

import static org.junit.Assert.assertEquals;

public class CountryTest {

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

    @Test
    public void testNameIsNull() {
        Country country = new Country(null, "BY", getCurrency());
        Set<ConstraintViolation<Country>> violations = validator.validate(country);
        assertEquals(violations.size(), 1);
        assertEquals("Country must have name.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testNameIsBlank() {
        Country country = new Country("  ", "BY", getCurrency());
        Set<ConstraintViolation<Country>> violations = validator.validate(country);
        assertEquals(violations.size(), 1);
        assertEquals("Country must have name.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testNameIsEmpty() {
        Country country = new Country("", "BY", getCurrency());
        Set<ConstraintViolation<Country>> violations = validator.validate(country);
        assertEquals(violations.size(), 1);
        assertEquals("Country must have name.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testAlphaCode2IsNull() {
        Country country = new Country("Беларусь", null, getCurrency());
        Set<ConstraintViolation<Country>> violations = validator.validate(country);
        assertEquals(violations.size(), 1);
        assertEquals("Country must have alphaCode2.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testAlphaCode2IsBlank() {
        Country country = new Country("Беларусь", "  ", getCurrency());
        Set<ConstraintViolation<Country>> violations = validator.validate(country);
        assertEquals(violations.size(), 1);
        assertEquals("Country must have alphaCode2.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testAlphaCode2IsEmpty() {
        Country country = new Country("Беларусь", "", getCurrency());
        Set<ConstraintViolation<Country>> violations = validator.validate(country);
        assertEquals(violations.size(), 1);
        assertEquals("Country must have alphaCode2.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testNameIsTooLong() {
        String name = StringUtils.repeat("очень_длинное_название_имени", 20);
        Country country = new Country(name, "BY", getCurrency());
        Set<ConstraintViolation<Country>> violations = validator.validate(country);
        assertEquals(violations.size(), 1);
        assertEquals("Country's name '" + name + "' mustn't be more than '255' characters long.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testAlphaCode2IsTooLong() {
        String name = StringUtils.repeat("очень_длинное_название_альфакода", 2);
        Country country = new Country("Беларусь", name, getCurrency());
        Set<ConstraintViolation<Country>> violations = validator.validate(country);
        assertEquals(violations.size(), 1);
        assertEquals("Country's alphaCode2 '" + name + "' mustn't be more than '2' characters long.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testHasNoCurrency() {
        Country country = new Country("Беларусь", "BY", null);
        Set<ConstraintViolation<Country>> violations = validator.validate(country);
        assertEquals(violations.size(), 1);
        assertEquals("Currency's Country mustn't be null.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testHasInvalidRecipes() {
        Country country = new Country("Беларусь", "BY", getCurrency());
        country.getRegions().add(null);
        Set<ConstraintViolation<Country>> violations = validator.validate(country);
        assertEquals(violations.size(), 1);
        assertEquals("Country must have list of regions without null elements.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testIsValid() {
        Country country = new Country("Беларусь", "BY", getCurrency());
        country.getRegions().add(new Region("Минская область", country));
        Set<ConstraintViolation<Country>> violations = validator.validate(country);
        assertEquals(violations.size(), 0);
    }

    private Currency getCurrency() {
        return new Currency("руб.", "BYN", true);
    }
}