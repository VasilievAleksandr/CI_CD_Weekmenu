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

public class DishTypeTest {

    private static ValidatorFactory validatorFactory;
    private static Validator validator;

    @Before
    public void setUp() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Test
    public void testDishTypeNameIsNull() {
        DishType dishType = new DishType(null, true);
        Set<ConstraintViolation<DishType>> violations = validator.validate(dishType);
        assertEquals(violations.size(), 1);
        assertEquals("DishType must have name.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testDishTypeNameIsBlank() {
        DishType dishType = new DishType("   ", true);
        Set<ConstraintViolation<DishType>> violations = validator.validate(dishType);
        assertEquals(violations.size(), 1);
        assertEquals("DishType must have name.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testDishTypeNameIsEmpty() {
        DishType dishType = new DishType("", true);
        Set<ConstraintViolation<DishType>> violations = validator.validate(dishType);
        assertEquals(violations.size(), 1);
        assertEquals("DishType must have name.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testNameIsTooLong() {
        DishType dishType = new DishType("", true);
        String name = StringUtils.repeat("очень_длинное_название_блюда", 20);
        dishType.setName(name);
        Set<ConstraintViolation<DishType>> violations = validator.validate(dishType);
        assertEquals(violations.size(), 1);
        assertEquals("DishType's name '" + name +"' must be '255' characters long",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testDishTypePriorityIsNegative() {
        DishType dishType = new DishType("Обед", true);
        dishType.setPriority(-100);
        Set<ConstraintViolation<DishType>> violations = validator.validate(dishType);
        assertEquals(violations.size(), 1);
        assertEquals("DishType's priority '-100' must be positive.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testDishTypePriorityIsZero() {
        DishType dishType = new DishType("Обед", true);
        dishType.setPriority(0);
        Set<ConstraintViolation<DishType>> violations = validator.validate(dishType);
        assertEquals(violations.size(), 1);
        assertEquals("DishType's priority '0' must be positive.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testDishTypeIsActiveIsNull() {
        DishType dishType = new DishType("Обед", null);
        Set<ConstraintViolation<DishType>> violations = validator.validate(dishType);
        assertEquals(violations.size(), 1);
        assertEquals("DishType must have field 'isActive' defined.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testDishTypeIsValid() {
        DishType dishType = new DishType("Обед", true);
        dishType.setPriority(200);
        Set<ConstraintViolation<DishType>> violations = validator.validate(dishType);
        assertEquals(violations.size(), 0);
    }

    @After
    public void tearDown() {
        validatorFactory.close();
    }
}