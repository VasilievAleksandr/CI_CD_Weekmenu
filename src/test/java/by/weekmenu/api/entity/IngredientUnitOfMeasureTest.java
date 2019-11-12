package by.weekmenu.api.entity;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import java.math.BigDecimal;
import java.util.Set;

import static org.junit.Assert.*;

public class IngredientUnitOfMeasureTest {

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
    public void testEquivalentHasTooManyFractionDigits() {
        IngredientUnitOfMeasure ingredientUnitOfMeasure = new IngredientUnitOfMeasure();
        ingredientUnitOfMeasure.setEquivalent(new BigDecimal("12.1234567"));
        Set<ConstraintViolation<IngredientUnitOfMeasure>> violations = validator.validate(ingredientUnitOfMeasure);
        assertEquals(violations.size(), 1);
        assertEquals("Equivalent '12.1234567' must have up to '7' integer digits and '6' fraction digits.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testEquivalentHasTooHighValue() {
        IngredientUnitOfMeasure ingredientUnitOfMeasure = new IngredientUnitOfMeasure();
        ingredientUnitOfMeasure.setEquivalent(new BigDecimal("12345678.12"));
        Set<ConstraintViolation<IngredientUnitOfMeasure>> violations = validator.validate(ingredientUnitOfMeasure);
        assertEquals(violations.size(), 1);
        assertEquals("Equivalent '12345678.12' must have up to '7' integer digits and '6' fraction digits.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testEquivalentIsNegative() {
        IngredientUnitOfMeasure ingredientUnitOfMeasure = new IngredientUnitOfMeasure();
        ingredientUnitOfMeasure.setEquivalent(new BigDecimal("-12.12"));
        Set<ConstraintViolation<IngredientUnitOfMeasure>> violations = validator.validate(ingredientUnitOfMeasure);
        assertEquals(violations.size(), 1);
        assertEquals("Equivalent '-12.12' must be positive.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testEquivalentIsNull() {
        IngredientUnitOfMeasure ingredientUnitOfMeasure = new IngredientUnitOfMeasure();
        ingredientUnitOfMeasure.setEquivalent(null);
        Set<ConstraintViolation<IngredientUnitOfMeasure>> violations = validator.validate(ingredientUnitOfMeasure);
        assertEquals(violations.size(), 1);
        assertEquals("IngredientUnitOfMeasure must have equivalent",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testIngredientUnitOfMeasureIsValid() {
        IngredientUnitOfMeasure ingredientUnitOfMeasure = new IngredientUnitOfMeasure();
        ingredientUnitOfMeasure.setEquivalent(new BigDecimal("12.12"));
        Set<ConstraintViolation<IngredientUnitOfMeasure>> violations = validator.validate(ingredientUnitOfMeasure);
        assertEquals(violations.size(), 0);
    }
}