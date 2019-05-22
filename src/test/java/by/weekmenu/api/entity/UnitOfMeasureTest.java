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
        UnitOfMeasure unitOfMeasure = new UnitOfMeasure(null, "килограмм");
        Set<ConstraintViolation<UnitOfMeasure>> violations = validator.validate(unitOfMeasure);
        assertEquals(violations.size(), 1);
        assertEquals("UnitOfMeasure must have name.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testUnitOfMeasureNameIsBlank() {
        UnitOfMeasure unitOfMeasure = new UnitOfMeasure("   ", "килограмм");
        Set<ConstraintViolation<UnitOfMeasure>> violations = validator.validate(unitOfMeasure);
        assertEquals(violations.size(), 1);
        assertEquals("UnitOfMeasure must have name.",
                violations.iterator().next().getMessage());
    }


    @Test
    public void testUnitOfMeasureNameIsEmpty() {
        UnitOfMeasure unitOfMeasure = new UnitOfMeasure("", "килограмм");
        Set<ConstraintViolation<UnitOfMeasure>> violations = validator.validate(unitOfMeasure);
        assertEquals(violations.size(), 1);
        assertEquals("UnitOfMeasure must have name.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testUnitOfMeasureNameIsTooLong() {
        String name = StringUtils.repeat("name", "/", 60);
        UnitOfMeasure unitOfMeasure = new UnitOfMeasure(name, "килограмм");
        Set<ConstraintViolation<UnitOfMeasure>> violations = validator.validate(unitOfMeasure);
        assertEquals(violations.size(), 1);
        assertEquals("UnitOfMeasure's name '" + name + "' mustn't be more than '255' characters long.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testUnitOfMeasureFullNameIsNull() {
        UnitOfMeasure unitOfMeasure = new UnitOfMeasure("кг", null);
        Set<ConstraintViolation<UnitOfMeasure>> violations = validator.validate(unitOfMeasure);
        assertEquals(violations.size(), 1);
        assertEquals("UnitOfMeasure must have full name.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testUnitOfMeasureFullNameIsBlank() {
        UnitOfMeasure unitOfMeasure = new UnitOfMeasure("кг", "   ");
        Set<ConstraintViolation<UnitOfMeasure>> violations = validator.validate(unitOfMeasure);
        assertEquals(violations.size(), 1);
        assertEquals("UnitOfMeasure must have full name.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testUnitOfMeasureFullNameIsEmpty() {
        UnitOfMeasure unitOfMeasure = new UnitOfMeasure("кг", "");
        Set<ConstraintViolation<UnitOfMeasure>> violations = validator.validate(unitOfMeasure);
        assertEquals(violations.size(), 1);
        assertEquals("UnitOfMeasure must have full name.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testUnitOfMeasureFullNameIsTooLong() {
        String fullName = StringUtils.repeat("fullName", "/", 60);
        UnitOfMeasure unitOfMeasure = new UnitOfMeasure("кг", fullName);
        Set<ConstraintViolation<UnitOfMeasure>> violations = validator.validate(unitOfMeasure);
        assertEquals(violations.size(), 1);
        assertEquals("UnitOfMeasure's full name '" + fullName + "' mustn't be more than '255' characters long.",
                violations.iterator().next().getMessage());
    }

        @Test
    public void testUnitOfMeasureIsValid() {
        UnitOfMeasure unitOfMeasure = new UnitOfMeasure("кг", "килограмм");
        Set<ConstraintViolation<UnitOfMeasure>> violations = validator.validate(unitOfMeasure);
        assertEquals(violations.size(), 0);
    }

    @After
    public void tearDown() {
        validatorFactory.close();
    }
}
