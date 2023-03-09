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

import static org.junit.Assert.*;

public class RegionTest {

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
        Region region = new Region(null, getCountry());
        Set<ConstraintViolation<Region>> violations = validator.validate(region);
        assertEquals(violations.size(), 1);
        assertEquals("Region must have name.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testNameIsBlank() {
        Region region = new Region("  ", getCountry());
        Set<ConstraintViolation<Region>> violations = validator.validate(region);
        assertEquals(violations.size(), 1);
        assertEquals("Region must have name.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testNameIsEmpty() {
        Region region = new Region("", getCountry());
        Set<ConstraintViolation<Region>> violations = validator.validate(region);
        assertEquals(violations.size(), 1);
        assertEquals("Region must have name.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testNameIsTooLong() {
        String name = StringUtils.repeat("очень_длинное_название_имени", 20);
        Region region = new Region(name, getCountry());
        Set<ConstraintViolation<Region>> violations = validator.validate(region);
        assertEquals(violations.size(), 1);
        assertEquals("Region's name '" + name + "' mustn't be more than '255' characters long.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testHasNoCountry() {
        Region region = new Region("Беларусь", null);
        Set<ConstraintViolation<Region>> violations = validator.validate(region);
        assertEquals(violations.size(), 1);
        assertEquals("Region's Country mustn't be null.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testIsValid() {
        Region region = new Region("Беларусь", getCountry());
        Set<ConstraintViolation<Region>> violations = validator.validate(region);
        assertEquals(violations.size(), 0);
    }

    private Country getCountry() {
        return new Country("Беларусь", "BY", new Currency("руб.", "BYN", false));
    }
}