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

public class MealTypeTest {

    private static ValidatorFactory validatorFactory;
    private static Validator validator;

    @Before
    public void setUp() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Test
    public void testMealTypeNameIsNull() {
        MealType mealType = new MealType(null, 1);
        Set<ConstraintViolation<MealType>> violations = validator.validate(mealType);
        assertEquals(violations.size(), 1);
        assertEquals("MealType must have name.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testMealTypeNameIsBlank() {
        MealType mealType = new MealType("   ", 1);
        Set<ConstraintViolation<MealType>> violations = validator.validate(mealType);
        assertEquals(violations.size(), 1);
        assertEquals("MealType must have name.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testMealTypeNameIsEmpty() {
        MealType mealType = new MealType("", 1);
        Set<ConstraintViolation<MealType>> violations = validator.validate(mealType);
        assertEquals(violations.size(), 1);
        assertEquals("MealType must have name.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testNameIsTooLong() {
        MealType mealType = new MealType("", 1);
        String name = StringUtils.repeat("очень_длинное_название_блюда", 20);
        mealType.setName(name);
        Set<ConstraintViolation<MealType>> violations = validator.validate(mealType);
        assertEquals(violations.size(), 1);
        assertEquals("MealType's name '" + name + "' must be '255' characters long",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testMealTypePriorityIsNegative() {
        MealType mealType = new MealType("Обед", -1);
        Set<ConstraintViolation<MealType>> violations = validator.validate(mealType);
        assertEquals(violations.size(), 1);
        assertEquals("MealType's priority '-1' must be positive.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testMealTypePriorityIsZero() {
        MealType mealType = new MealType("Обед", 0);
        Set<ConstraintViolation<MealType>> violations = validator.validate(mealType);
        assertEquals(violations.size(), 1);
        assertEquals("MealType's priority '0' must be positive.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testMealTypePriorityIsNull() {
        MealType mealType = new MealType("Завтрак", null);
        Set<ConstraintViolation<MealType>> violations = validator.validate(mealType);
        assertEquals(violations.size(), 1);
        assertEquals("MealType must have priority.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testMealTypeIsValid() {
        MealType mealType = new MealType("Обед", 1);
        Set<ConstraintViolation<MealType>> violations = validator.validate(mealType);
        assertEquals(violations.size(), 0);
    }

    @After
    public void tearDown() {
        validatorFactory.close();
    }
}