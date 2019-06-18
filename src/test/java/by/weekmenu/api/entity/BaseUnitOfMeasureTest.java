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

public class BaseUnitOfMeasureTest {

    private static ValidatorFactory validatorFactory;
    private static Validator validator;

    @Before
    public void setUp() throws Exception {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Test
    public void testUnitOfMeasureShortNameIsNull() {
        BaseUnitOfMeasure baseUnitOfMeasure = new BaseUnitOfMeasure(null, "килограмм");
        Set<ConstraintViolation<BaseUnitOfMeasure>> violations = validator.validate(baseUnitOfMeasure);
        assertEquals(violations.size(), 1);
        assertEquals("BaseUnitOfMeasure must have short name.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testUnitOfMeasureShortNameIsBlank() {
        BaseUnitOfMeasure baseUnitOfMeasure = new BaseUnitOfMeasure("   ", "килограмм");
        Set<ConstraintViolation<BaseUnitOfMeasure>> violations = validator.validate(baseUnitOfMeasure);
        assertEquals(violations.size(), 1);
        assertEquals("BaseUnitOfMeasure must have short name.",
                violations.iterator().next().getMessage());
    }


    @Test
    public void testUnitOfMeasureShortNameIsEmpty() {
        BaseUnitOfMeasure baseUnitOfMeasure = new BaseUnitOfMeasure("", "килограмм");
        Set<ConstraintViolation<BaseUnitOfMeasure>> violations = validator.validate(baseUnitOfMeasure);
        assertEquals(violations.size(), 1);
        assertEquals("BaseUnitOfMeasure must have short name.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testUnitOfMeasureShortNameIsTooLong() {
        String name = StringUtils.repeat("name", "/", 60);
        BaseUnitOfMeasure baseUnitOfMeasure = new BaseUnitOfMeasure(name, "килограмм");
        Set<ConstraintViolation<BaseUnitOfMeasure>> violations = validator.validate(baseUnitOfMeasure);
        assertEquals(violations.size(), 1);
        assertEquals("BaseUnitOfMeasure's short name '" + name + "' mustn't be more than '255' characters long.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testUnitOfMeasureFullNameIsNull() {
        BaseUnitOfMeasure baseUnitOfMeasure = new BaseUnitOfMeasure("кг", null);
        Set<ConstraintViolation<BaseUnitOfMeasure>> violations = validator.validate(baseUnitOfMeasure);
        assertEquals(violations.size(), 1);
        assertEquals("BaseUnitOfMeasure must have full name.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testUnitOfMeasureFullNameIsBlank() {
        BaseUnitOfMeasure baseUnitOfMeasure = new BaseUnitOfMeasure("кг", "   ");
        Set<ConstraintViolation<BaseUnitOfMeasure>> violations = validator.validate(baseUnitOfMeasure);
        assertEquals(violations.size(), 1);
        assertEquals("BaseUnitOfMeasure must have full name.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testUnitOfMeasureFullNameIsEmpty() {
        BaseUnitOfMeasure baseUnitOfMeasure = new BaseUnitOfMeasure("кг", "");
        Set<ConstraintViolation<BaseUnitOfMeasure>> violations = validator.validate(baseUnitOfMeasure);
        assertEquals(violations.size(), 1);
        assertEquals("BaseUnitOfMeasure must have full name.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testUnitOfMeasureFullNameIsTooLong() {
        String fullName = StringUtils.repeat("fullName", "/", 60);
        BaseUnitOfMeasure baseUnitOfMeasure = new BaseUnitOfMeasure("кг", fullName);
        Set<ConstraintViolation<BaseUnitOfMeasure>> violations = validator.validate(baseUnitOfMeasure);
        assertEquals(violations.size(), 1);
        assertEquals("BaseUnitOfMeasure's full name '" + fullName + "' mustn't be more than '255' characters long.",
                violations.iterator().next().getMessage());
    }

        @Test
    public void testUnitOfMeasureIsValid() {
        BaseUnitOfMeasure baseUnitOfMeasure = new BaseUnitOfMeasure("кг", "килограмм");
        Set<ConstraintViolation<BaseUnitOfMeasure>> violations = validator.validate(baseUnitOfMeasure);
        assertEquals(violations.size(), 0);
    }

    @After
    public void tearDown() {
        validatorFactory.close();
    }
}
