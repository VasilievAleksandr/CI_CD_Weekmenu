package by.weekmenu.api.entity;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import java.util.Set;

import static org.junit.Assert.*;

public class DayOfWeekTest {

    private static ValidatorFactory validatorFactory;
    private static Validator validator;

    @Before
    public void setUp() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Test
    public void testNameIsNull() {
        DayOfWeek dayOfWeek = new DayOfWeek(null, "ПН");
        Set<ConstraintViolation<DayOfWeek>> violations =validator.validate(dayOfWeek);
        assertEquals(violations.size(), 1);
        assertEquals("DayOfWeek must have name.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testNameIsBlank() {
        DayOfWeek dayOfWeek = new DayOfWeek("   ", "ПН");
        Set<ConstraintViolation<DayOfWeek>> violations =validator.validate(dayOfWeek);
        assertEquals(violations.size(), 1);
        assertEquals("DayOfWeek must have name.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testNameIsEmpty() {
        DayOfWeek dayOfWeek = new DayOfWeek("", "ПН");
        Set<ConstraintViolation<DayOfWeek>> violations =validator.validate(dayOfWeek);
        assertEquals(violations.size(), 1);
        assertEquals("DayOfWeek must have name.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testShortNameIsNull() {
        DayOfWeek dayOfWeek = new DayOfWeek("Понедельник", null);
        Set<ConstraintViolation<DayOfWeek>> violations =validator.validate(dayOfWeek);
        assertEquals(violations.size(), 1);
        assertEquals("DayOfWeek must have shortName.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testShortNameIsBlank() {
        DayOfWeek dayOfWeek = new DayOfWeek("Понедельник", "   ");
        Set<ConstraintViolation<DayOfWeek>> violations =validator.validate(dayOfWeek);
        assertEquals(violations.size(), 1);
        assertEquals("DayOfWeek must have shortName.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testShortNameIsEmpty() {
        DayOfWeek dayOfWeek = new DayOfWeek("Понедельник", "");
        Set<ConstraintViolation<DayOfWeek>> violations =validator.validate(dayOfWeek);
        assertEquals(violations.size(), 1);
        assertEquals("DayOfWeek must have shortName.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testIsValid() {
        DayOfWeek dayOfWeek = new DayOfWeek("Понедельник", "ПН");
        Set<ConstraintViolation<DayOfWeek>> violations =validator.validate(dayOfWeek);
        assertEquals(violations.size(), 0);
    }

    @After
    public void tearDown() {
        validatorFactory.close();
    }
}