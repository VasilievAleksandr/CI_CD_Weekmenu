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
    public void testOwnershipIsValid() {
        Ownership ownership = new Ownership(OwnershipName.USER);
        Set<ConstraintViolation<Ownership>> violations = validator.validate(ownership);
        assertEquals(violations.size(), 0);
    }
}