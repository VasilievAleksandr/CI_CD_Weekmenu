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

public class RecipeCurrencyTest {


    private static ValidatorFactory validatorFactory;
    private static Validator validator;

    @Before
    public void setUp() throws Exception {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Test
    public void testRecipeCurrencyPriceValueHasTooManyFractionDigits() {
        RecipeCurrency recipeCurrency = new RecipeCurrency(new BigDecimal("111.123"));
        recipeCurrency.setRecipe(new Recipe("рецепт", true, new CookingMethod("жарка"),
                new Ownership("пользователь")));
        recipeCurrency.setCurrency(new Currency("руб", "BYN", "$", true));
        Set<ConstraintViolation<RecipeCurrency>> violations = validator.validate(recipeCurrency);
        assertEquals(violations.size(), 1);
        assertEquals("Recipe_Currency's Price_Value '111.123' must have up to '7' integer digits and '2' fraction digits.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testRecipeCurrencyPriceValueIsTooHigh() {
        RecipeCurrency recipeCurrency = new RecipeCurrency(new BigDecimal("1111111111.123"));
        recipeCurrency.setRecipe(new Recipe("рецепт", true, new CookingMethod("жарка"),
                new Ownership("пользователь")));
        recipeCurrency.setCurrency(new Currency("руб", "BYN", "$", true));
        Set<ConstraintViolation<RecipeCurrency>> violations = validator.validate(recipeCurrency);
        assertEquals(violations.size(), 1);
        assertEquals("Recipe_Currency's Price_Value '1111111111.123' must have up to '7' integer digits and '2' fraction digits.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testRecipeCurrencyPriceValueIsNegative() {
        RecipeCurrency recipeCurrency = new RecipeCurrency(new BigDecimal("-111"));
        recipeCurrency.setRecipe(new Recipe("рецепт", true, new CookingMethod("жарка"),
                new Ownership("пользователь")));
        recipeCurrency.setCurrency(new Currency("руб", "BYN", "$", true));
        Set<ConstraintViolation<RecipeCurrency>> violations = validator.validate(recipeCurrency);
        assertEquals(violations.size(), 1);
        assertEquals("Recipe_Currency's Price_Value '-111' must be positive.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testRecipeIsNull() {
        RecipeCurrency recipeCurrency = new RecipeCurrency(new BigDecimal("111"), null,
                new Currency("руб", "BYN", "$", true));
        Set<ConstraintViolation<RecipeCurrency>> violations = validator.validate(recipeCurrency);
        assertEquals(violations.size(), 1);
        assertEquals("Recipe_Currency's Recipe mustn't be null.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testRecipeIsInvalid() {
        RecipeCurrency recipeCurrency = new RecipeCurrency(new BigDecimal("111"),
                new Recipe(null, true, new CookingMethod("жарка"), new Ownership("пользователь")),
                new Currency("руб", "BYN", "$", true));
        Set<ConstraintViolation<RecipeCurrency>> violations = validator.validate(recipeCurrency);
        assertEquals(violations.size(), 1);
        assertEquals("Recipe must have name.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testCurrencyIsNull() {
        RecipeCurrency recipeCurrency = new RecipeCurrency(new BigDecimal("111"),
                new Recipe("рецепт", true, new CookingMethod("жарка"), new Ownership("пользователь")),
                null);
        Set<ConstraintViolation<RecipeCurrency>> violations = validator.validate(recipeCurrency);
        assertEquals(violations.size(), 1);
        assertEquals("Recipe_Currency's Currency mustn't be null.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testRecipeCurrencyIsValid() {
        RecipeCurrency recipeCurrency = new RecipeCurrency(new BigDecimal("111"),
                new Recipe("рецепт", true, new CookingMethod("жарка"), new Ownership("пользователь")),
                new Currency("руб", "BYN", "$", true));
        Set<ConstraintViolation<RecipeCurrency>> violations = validator.validate(recipeCurrency);
        assertEquals(violations.size(), 0);
    }
}
