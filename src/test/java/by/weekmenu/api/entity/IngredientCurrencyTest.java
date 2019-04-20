package by.weekmenu.api.entity;

import org.junit.Before;
import org.junit.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.math.BigDecimal;
import java.util.Set;

import static org.junit.Assert.*;

public class IngredientCurrencyTest {

    private static ValidatorFactory validatorFactory;
    private static Validator validator;

    @Before
    public void setUp() throws Exception {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Test
    public void testIngredientCurrencyPriceValueHasTooManyFractionDigits() {
        IngredientCurrency ingredientCurrency = new IngredientCurrency(new BigDecimal("111.123"));
        ingredientCurrency.setIngredient(new Ingredient("курица", new Ownership("пользователь"), new UnitOfMeasure("литр")));
        ingredientCurrency.setCurrency(new Currency("руб", "BYN", "$", true));
        Set<ConstraintViolation<IngredientCurrency>> violations = validator.validate(ingredientCurrency);
        assertEquals(violations.size(), 1);
        assertEquals("Ingredient_Currency's Price_Value '111.123' must have up to '7' integer digits and '2' fraction digits.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testIngredientCurrencyPriceValueIsTooHigh() {
        IngredientCurrency ingredientCurrency = new IngredientCurrency(new BigDecimal("1111111111.123"));
        ingredientCurrency.setIngredient(new Ingredient("курица", new Ownership("пользователь"), new UnitOfMeasure("литр")));
        ingredientCurrency.setCurrency(new Currency("руб", "BYN", "$", true));
        Set<ConstraintViolation<IngredientCurrency>> violations = validator.validate(ingredientCurrency);
        assertEquals(violations.size(), 1);
        assertEquals("Ingredient_Currency's Price_Value '1111111111.123' must have up to '7' integer digits and '2' fraction digits.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testIngredientCurrencyPriceValueIsNegative() {
        IngredientCurrency ingredientCurrency = new IngredientCurrency(new BigDecimal("-111"));
        ingredientCurrency.setIngredient(new Ingredient("курица", new Ownership("пользователь"), new UnitOfMeasure("литр")));
        ingredientCurrency.setCurrency(new Currency("руб", "BYN", "$", true));
        Set<ConstraintViolation<IngredientCurrency>> violations = validator.validate(ingredientCurrency);
        assertEquals(violations.size(), 1);
        assertEquals("Ingredient_Currency's Price_Value '-111' must be positive.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testIngredientIsNull() {
        IngredientCurrency ingredientCurrency = new IngredientCurrency(new BigDecimal("111"), null,
                new Currency("руб", "BYN", "$", true));
        Set<ConstraintViolation<IngredientCurrency>> violations = validator.validate(ingredientCurrency);
        assertEquals(violations.size(), 1);
        assertEquals("Ingredient_Currency's Ingredient mustn't be null.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testIngredientIsInvalid() {
        IngredientCurrency ingredientCurrency = new IngredientCurrency(new BigDecimal("111"),
                new Ingredient(null, new Ownership("пользователь"), new UnitOfMeasure("литр")),
                new Currency("руб", "BYN", "$", true));
        Set<ConstraintViolation<IngredientCurrency>> violations = validator.validate(ingredientCurrency);
        assertEquals(violations.size(), 1);
        assertEquals("Ingredient must have name.",
                violations.iterator().next().getMessage());
    }

   @Test
    public void testCurrencyIsNull() {
       IngredientCurrency ingredientCurrency = new IngredientCurrency(new BigDecimal("111"),
               new Ingredient("курица", new Ownership("пользователь"), new UnitOfMeasure("литр")),
               null);
        Set<ConstraintViolation<IngredientCurrency>> violations = validator.validate(ingredientCurrency);
        assertEquals(violations.size(), 1);
        assertEquals("Ingredient_Currency's Currency mustn't be null.",
                violations.iterator().next().getMessage());
    }
}
