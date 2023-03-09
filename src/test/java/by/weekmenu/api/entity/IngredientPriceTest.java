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
    public void setUp() {
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
        return new Currency("руб.", "BYN", false);
    }

    private Ingredient getIngredient() {
        Ingredient ingredient = new Ingredient("Курица", new Ownership(OwnershipName.USER));
        ingredient.setIngredientCategory(new IngredientCategory("Milk", false));
        return ingredient;
    }

    private UnitOfMeasure getUnitOfMeasure() {
        return new UnitOfMeasure("Кг", "Килограмм");
    }

    @Test
    public void testIngredientPricePriceValueHasTooManyFractionDigits() {
        IngredientPrice ingredientPrice = new IngredientPrice(getIngredient(), getRegion(), getUnitOfMeasure(),
                new BigDecimal("1"), new BigDecimal("111.123"));
        Set<ConstraintViolation<IngredientPrice>> violations = validator.validate(ingredientPrice);
        assertEquals(violations.size(), 1);
        assertEquals("Price_Value '111.123' must have up to '7' integer digits and '2' fraction digits.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testIngredientPricePriceValueIsTooHigh() {
        IngredientPrice ingredientPrice = new IngredientPrice(getIngredient(), getRegion(), getUnitOfMeasure(),
                new BigDecimal("1"), new BigDecimal("12345678.12"));
        Set<ConstraintViolation<IngredientPrice>> violations = validator.validate(ingredientPrice);
        assertEquals(violations.size(), 1);
        assertEquals("Price_Value '12345678.12' must have up to '7' integer digits and '2' fraction digits.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testIngredientPricePriceValueIsNegative() {
        IngredientPrice ingredientPrice = new IngredientPrice(getIngredient(), getRegion(), getUnitOfMeasure(),
                new BigDecimal("1"), new BigDecimal("-111"));
        Set<ConstraintViolation<IngredientPrice>> violations = validator.validate(ingredientPrice);
        assertEquals(violations.size(), 1);
        assertEquals("Price_Value '-111' must be positive.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testIngredientPricePriceValueIsNull() {
        IngredientPrice ingredientPrice = new IngredientPrice(getIngredient(), getRegion(), getUnitOfMeasure(),
                new BigDecimal("1"), null);
        Set<ConstraintViolation<IngredientPrice>> violations = validator.validate(ingredientPrice);
        assertEquals(violations.size(), 1);
        assertEquals("IngredientPrice must have priceValue",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testIngredientPriceQuantityValueIsTooHigh() {
        IngredientPrice ingredientPrice = new IngredientPrice(getIngredient(), getRegion(), getUnitOfMeasure(),
                new BigDecimal("12345678.1"), new BigDecimal("111.12"));
        Set<ConstraintViolation<IngredientPrice>> violations = validator.validate(ingredientPrice);
        assertEquals(violations.size(), 1);
        assertEquals("Quantity '12345678.1' must have up to '7' integer digits and '1' fraction digits.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testIngredientPriceQuantityIsNegative() {
        IngredientPrice ingredientPrice = new IngredientPrice(getIngredient(), getRegion(), getUnitOfMeasure(),
                new BigDecimal("-111"), new BigDecimal("111.12"));
        Set<ConstraintViolation<IngredientPrice>> violations = validator.validate(ingredientPrice);
        assertEquals(violations.size(), 1);
        assertEquals("Quantity '-111' must be positive.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testIngredientPriceQuantityHasTooManyFractionDigits() {
        IngredientPrice ingredientPrice = new IngredientPrice(getIngredient(), getRegion(), getUnitOfMeasure(),
                new BigDecimal("1.12"), new BigDecimal("111.12"));
        Set<ConstraintViolation<IngredientPrice>> violations = validator.validate(ingredientPrice);
        assertEquals(violations.size(), 1);
        assertEquals("Quantity '1.12' must have up to '7' integer digits and '1' fraction digits.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testIngredientPriceQuantityIsNull() {
        IngredientPrice ingredientPrice = new IngredientPrice(getIngredient(), getRegion(), getUnitOfMeasure(),
                null, new BigDecimal("111.12"));
        Set<ConstraintViolation<IngredientPrice>> violations = validator.validate(ingredientPrice);
        assertEquals(violations.size(), 1);
        assertEquals("IngredientPrice must have quantity.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testIngredientIsNull() {
        IngredientPrice ingredientPrice = new IngredientPrice(null, getRegion(), getUnitOfMeasure(),
                new BigDecimal("1"), new BigDecimal("123.12"));
        Set<ConstraintViolation<IngredientPrice>> violations = validator.validate(ingredientPrice);
        assertEquals(violations.size(), 1);
        assertEquals("Ingredient_Price's Ingredient mustn't be null.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testRegionIsNull() {
        IngredientPrice ingredientPrice = new IngredientPrice(getIngredient(), null, getUnitOfMeasure(),
                new BigDecimal("1"), new BigDecimal("123.12"));
        Set<ConstraintViolation<IngredientPrice>> violations = validator.validate(ingredientPrice);
        assertEquals(violations.size(), 1);
        assertEquals("Ingredient_Price's Region mustn't be null.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testUnitOfMeasureIsNull() {
        IngredientPrice ingredientPrice = new IngredientPrice(getIngredient(), getRegion(), null,
                new BigDecimal("1"), new BigDecimal("123.12"));
        Set<ConstraintViolation<IngredientPrice>> violations = validator.validate(ingredientPrice);
        assertEquals(violations.size(), 1);
        assertEquals("IngredientPrice must have UnitOfMeasure",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testIngredientPriceIsValid() {
        IngredientPrice ingredientPrice = new IngredientPrice(getIngredient(), getRegion(), getUnitOfMeasure(),
                new BigDecimal("1"), new BigDecimal("123.12"));
        Set<ConstraintViolation<IngredientPrice>> violations = validator.validate(ingredientPrice);
        assertEquals(violations.size(), 0);
    }

}
