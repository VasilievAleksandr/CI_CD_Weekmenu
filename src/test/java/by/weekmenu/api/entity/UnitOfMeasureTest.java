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

public class UnitOfMeasureTest {

    private static ValidatorFactory validatorFactory;
    private static Validator validator;

    @Before
    public void setUp() throws Exception {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Test
    public void testUnitOfMeasureNameIsNull() {
        UnitOfMeasure unitOfMeasure = new UnitOfMeasure(null);
        Set<ConstraintViolation<UnitOfMeasure>> violations = validator.validate(unitOfMeasure);
        assertEquals(violations.size(), 1);
        assertEquals("UnitOfMeasure must have name.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testUnitOfMeasureNameIsBlank() {
        UnitOfMeasure unitOfMeasure = new UnitOfMeasure("   ");
        Set<ConstraintViolation<UnitOfMeasure>> violations = validator.validate(unitOfMeasure);
        assertEquals(violations.size(), 1);
        assertEquals("UnitOfMeasure must have name.",
                violations.iterator().next().getMessage());
    }


    @Test
    public void testUnitOfMeasureNameIsEmpty() {
        UnitOfMeasure unitOfMeasure = new UnitOfMeasure("");
        Set<ConstraintViolation<UnitOfMeasure>> violations = validator.validate(unitOfMeasure);
        assertEquals(violations.size(), 1);
        assertEquals("UnitOfMeasure must have name.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testUnitOfMeasureIsValid() {
        UnitOfMeasure unitOfMeasure = new UnitOfMeasure("литр");
        Set<ConstraintViolation<UnitOfMeasure>> violations = validator.validate(unitOfMeasure);
        assertEquals(violations.size(), 0);
    }

    @After
    public void tearDown() {
        validatorFactory.close();
    }
}
