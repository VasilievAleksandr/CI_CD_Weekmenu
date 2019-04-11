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
        Set<ConstraintViolation<DishType>> violations =validator.validate(dishType);
        assertEquals(violations.size(), 1);
        assertEquals("DishType must have have name.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testDishTypeNameIsBlank() {
        DishType dishType = new DishType("   ", true);
        Set<ConstraintViolation<DishType>> violations =validator.validate(dishType);
        assertEquals(violations.size(), 1);
        assertEquals("DishType must have have name.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testDishTypeNameIsEmpty() {
        DishType dishType = new DishType("", true);
        Set<ConstraintViolation<DishType>> violations =validator.validate(dishType);
        assertEquals(violations.size(), 1);
        assertEquals("DishType must have have name.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testDishTypeNameIsNotBlank() {
        DishType dishType = new DishType("Обед", true);
        Set<ConstraintViolation<DishType>> violations =validator.validate(dishType);
        assertEquals(violations.size(), 0);
    }

    @Test
    public void testDishTypePriorityIsNegative() {
        DishType dishType = new DishType("Обед", true);
        dishType.setPriority(-100);
        Set<ConstraintViolation<DishType>> violations =validator.validate(dishType);
        assertEquals(violations.size(), 1);
        assertEquals("DishType's priority '-100' must be positive.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testDishTypePriorityIsZero() {
        DishType dishType = new DishType("Обед", true);
        dishType.setPriority(0);
        Set<ConstraintViolation<DishType>> violations =validator.validate(dishType);
        assertEquals(violations.size(), 1);
        assertEquals("DishType's priority '0' must be positive.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testDishTypePriorityIsPositive() {
        DishType dishType = new DishType("Обед", true);
        dishType.setPriority(100);
        Set<ConstraintViolation<DishType>> violations =validator.validate(dishType);
        assertEquals(violations.size(), 0);
    }

    @Test
    public void testDishTypeIsActiveIsNull() {
        DishType dishType = new DishType("Обед", null);
        Set<ConstraintViolation<DishType>> violations =validator.validate(dishType);
        assertEquals(violations.size(), 1);
        assertEquals("DishType must have field 'isActive' defined.",
                violations.iterator().next().getMessage());
    }


    @After
    public void tearDown() {
        validatorFactory.close();
    }
}