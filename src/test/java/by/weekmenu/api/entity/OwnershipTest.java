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

public class OwnershipTest {

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
        Ownership ownership = new Ownership(null);
        Set<ConstraintViolation<Ownership>> violations = validator.validate(ownership);
        assertEquals(violations.size(), 1);
        assertEquals("Ownership must have name.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testNameIsBlank() {
        Ownership ownership = new Ownership("  ");
        Set<ConstraintViolation<Ownership>> violations = validator.validate(ownership);
        assertEquals(violations.size(), 1);
        assertEquals("Ownership must have name.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testNameIsEmpty() {
        Ownership ownership = new Ownership("");
        Set<ConstraintViolation<Ownership>> violations = validator.validate(ownership);
        assertEquals(violations.size(), 1);
        assertEquals("Ownership must have name.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testNameIsTooLong() {
        Ownership ownership = new Ownership();
        String name = StringUtils.repeat("очень_длинное_название_имени", 20);
        ownership.setName(name);
        Set<ConstraintViolation<Ownership>> violations = validator.validate(ownership);
        assertEquals(violations.size(), 1);
        assertEquals("Ownership's name '" + name +"' mustn't be more than '255' characters long.",
                violations.iterator().next().getMessage());
    }
    @Test
    public void testOwnershipIsValid() {
        Ownership ownership = new Ownership("Пользователь");
        Set<ConstraintViolation<Ownership>> violations = validator.validate(ownership);
        assertEquals(violations.size(), 0);
    }
}