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

public class IngredientPriceTest {

    private static ValidatorFactory validatorFactory;
    private static Validator validator;

    @Before
    public void setUp() throws Exception {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @After
    public void tearDown() {
        validatorFactory.close();
    }

    private Region getRegion() {
        Country country = new Country("Беларусь", "BY", getCurrency());
        return new Region("Минский район", country);
    }

    private Currency getCurrency() {
        return new Currency("руб.", "BYN", true);
    }

    @Test
    public void testIngredientPricePriceValueHasTooManyFractionDigits() {
        IngredientPrice ingredientPrice = new IngredientPrice(new BigDecimal("111.123"));

        ingredientPrice.setIngredient(new Ingredient("курица", new Ownership("пользователь"), new UnitOfMeasure("литр")));
        ingredientPrice.setRegion(getRegion());
        Set<ConstraintViolation<IngredientPrice>> violations = validator.validate(ingredientPrice);
        assertEquals(violations.size(), 1);
        assertEquals("Ingredient_Price's Price_Value '111.123' must have up to '7' integer digits and '2' fraction digits.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testIngredientPricePriceValueIsTooHigh() {
        IngredientPrice ingredientPrice = new IngredientPrice(new BigDecimal("1111111111.123"));
        ingredientPrice.setIngredient(new Ingredient("курица", new Ownership("пользователь"), new UnitOfMeasure("литр")));
        ingredientPrice.setRegion(getRegion());
        Set<ConstraintViolation<IngredientPrice>> violations = validator.validate(ingredientPrice);
        assertEquals(violations.size(), 1);
        assertEquals("Ingredient_Price's Price_Value '1111111111.123' must have up to '7' integer digits and '2' fraction digits.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testIngredientPricePriceValueIsNegative() {
        IngredientPrice ingredientPrice = new IngredientPrice(new BigDecimal("-111"));
        ingredientPrice.setIngredient(new Ingredient("курица", new Ownership("пользователь"), new UnitOfMeasure("литр")));
        ingredientPrice.setRegion(getRegion());
        Set<ConstraintViolation<IngredientPrice>> violations = validator.validate(ingredientPrice);
        assertEquals(violations.size(), 1);
        assertEquals("Ingredient_Price's Price_Value '-111' must be positive.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testIngredientIsNull() {
        IngredientPrice ingredientPrice = new IngredientPrice(new BigDecimal("111"), null,
                getRegion());
        Set<ConstraintViolation<IngredientPrice>> violations = validator.validate(ingredientPrice);
        assertEquals(violations.size(), 1);
        assertEquals("Ingredient_Price's Ingredient mustn't be null.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testIngredientIsInvalid() {
        IngredientPrice ingredientPrice = new IngredientPrice(new BigDecimal("111"),
                new Ingredient(null, new Ownership("пользователь"), new UnitOfMeasure("литр")),
                getRegion());
        Set<ConstraintViolation<IngredientPrice>> violations = validator.validate(ingredientPrice);
        assertEquals(violations.size(), 1);
        assertEquals("Ingredient must have name.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testRegionIsNull() {
        IngredientPrice ingredientPrice = new IngredientPrice(new BigDecimal("111"),
                new Ingredient("курица", new Ownership("пользователь"), new UnitOfMeasure("литр")),
                null);
        Set<ConstraintViolation<IngredientPrice>> violations = validator.validate(ingredientPrice);
        assertEquals(violations.size(), 1);
        assertEquals("Ingredient_Price's Region mustn't be null.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testIngredientCurrencyIsValid() {
        IngredientPrice ingredientPrice = new IngredientPrice(new BigDecimal("111"),
                new Ingredient("курица", new Ownership("пользователь"), new UnitOfMeasure("литр")),
                getRegion());
        Set<ConstraintViolation<IngredientPrice>> violations = validator.validate(ingredientPrice);
        assertEquals(violations.size(), 0);
    }

}
