package by.weekmenu.api.entity;

import org.apache.commons.lang3.StringUtils;
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

public class IngredientTest {

    private static ValidatorFactory validatorFactory;
    private static Validator validator;

    @Before
    public void setUp() throws Exception {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    private Ingredient createIngredient(String name) {
        Ingredient ingredient = new Ingredient(name, new Ownership(OwnershipName.USER));
        ingredient.setIngredientCategory(new IngredientCategory("Milk", false));
        return ingredient;
    }

    private Ingredient createIngredientWithProperties(String calories, String proteins, String fats, String carbs) {
        Ingredient ingredient = new Ingredient(new BigDecimal(calories), new BigDecimal(proteins), new BigDecimal(fats), new BigDecimal(carbs));
        ingredient.setName("молоко");
        ingredient.setOwnership(new Ownership(OwnershipName.USER));
        ingredient.setIngredientCategory(new IngredientCategory("Milk", false));
        return ingredient;
    }

    @Test
    public void testIngredientNameIsTooLong() {
        String name = StringUtils.repeat("name", "/", 60);
        Ingredient ingredient = createIngredient(null);
        ingredient.setName(name);
        Set<ConstraintViolation<Ingredient>> violations = validator.validate(ingredient);
        assertEquals(violations.size(), 1);
        assertEquals("Ingredient's name '" + name + "' mustn't be more than '255' characters long.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testIngredientNameIsNull() {
        Ingredient ingredient = createIngredient(null);
        Set<ConstraintViolation<Ingredient>> violations = validator.validate(ingredient);
        assertEquals(violations.size(), 1);
        assertEquals("Ingredient must have name.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testIngredientNameIsBlank() {
        Ingredient ingredient = createIngredient("   ");
        Set<ConstraintViolation<Ingredient>> violations = validator.validate(ingredient);
        assertEquals(violations.size(), 1);
        assertEquals("Ingredient must have name.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testIngredientNameIsEmpty() {
        Ingredient ingredient = createIngredient("");
        Set<ConstraintViolation<Ingredient>> violations = validator.validate(ingredient);
        assertEquals(violations.size(), 1);
        assertEquals("Ingredient must have name.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testIngredientCaloriesAreNegative() {
        Ingredient ingredient = createIngredientWithProperties("-100", "100", "100", "100");
        Set<ConstraintViolation<Ingredient>> violations = validator.validate(ingredient);
        assertEquals(violations.size(), 1);
        assertEquals("Ingredient's calories '-100' must be positive or '0'.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testIngredientCaloriesHasTooManyFractionDigits() {
        Ingredient ingredient = createIngredientWithProperties("123.123", "100", "100", "100");
        Set<ConstraintViolation<Ingredient>> violations = validator.validate(ingredient);
        assertEquals(violations.size(), 1);
        assertEquals("Calories '123.123' must have up to '7' integer digits and '1' fraction digits.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testIngredientCaloriesValueIsTooHigh() {
        Ingredient ingredient = createIngredientWithProperties("12345678.1", "100", "100", "100");
        Set<ConstraintViolation<Ingredient>> violations = validator.validate(ingredient);
        assertEquals(violations.size(), 1);
        assertEquals("Calories '12345678.1' must have up to '7' integer digits and '1' fraction digits.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testIngredientProteinsAreNegative() {
        Ingredient ingredient = createIngredientWithProperties("100", "-100", "100", "100");
        Set<ConstraintViolation<Ingredient>> violations = validator.validate(ingredient);
        assertEquals(violations.size(), 1);
        assertEquals("Ingredient's proteins '-100' must be positive or '0'.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testIngredientProteinsHasTooManyFractionDigits() {
        Ingredient ingredient = createIngredientWithProperties("100", "90.123", "100", "100");
        Set<ConstraintViolation<Ingredient>> violations = validator.validate(ingredient);
        assertEquals(violations.size(), 1);
        assertEquals("Proteins '90.123' must have up to '3' integer digits and '1' fraction digits.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testIngredientProteinsValueIsTooHigh() {
        Ingredient ingredient = createIngredientWithProperties("100", "123.1", "100", "100");
        Set<ConstraintViolation<Ingredient>> violations = validator.validate(ingredient);
        assertEquals(violations.size(), 1);
        assertEquals("Ingredient's proteins '123.1' must be lower than '100'.",
                violations.iterator().next().getMessage());

    }

    @Test
    public void testIngredientFatsAreNegative() {
        Ingredient ingredient = createIngredientWithProperties("100", "100", "-100", "100");
        Set<ConstraintViolation<Ingredient>> violations = validator.validate(ingredient);
        assertEquals(violations.size(), 1);
        assertEquals("Ingredient's fats '-100' must be positive or '0'.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testIngredientFatsHasTooManyFractionDigits() {
        Ingredient ingredient = createIngredientWithProperties("100", "100", "90.123", "100");
        Set<ConstraintViolation<Ingredient>> violations = validator.validate(ingredient);
        assertEquals(violations.size(), 1);
        assertEquals("Fats '90.123' must have up to '3' integer digits and '1' fraction digits.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testIngredientFatsValueIsTooHigh() {
        Ingredient ingredient = createIngredientWithProperties("100", "100", "123.1", "100");
        Set<ConstraintViolation<Ingredient>> violations = validator.validate(ingredient);
        assertEquals(violations.size(), 1);
        assertEquals("Ingredient's fats '123.1' must be lower than '100'.",
                violations.iterator().next().getMessage());

    }

    @Test
    public void testIngredientCarbsAreNegative() {
        Ingredient ingredient = createIngredientWithProperties("100", "100", "100", "-100");
        Set<ConstraintViolation<Ingredient>> violations = validator.validate(ingredient);
        assertEquals(violations.size(), 1);
        assertEquals("Ingredient's carbs '-100' must be positive or '0'.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testIngredientCarbsHasTooManyFractionDigits() {
        Ingredient ingredient = createIngredientWithProperties("100", "100", "100", "90.123");
        Set<ConstraintViolation<Ingredient>> violations = validator.validate(ingredient);
        assertEquals(violations.size(), 1);
        assertEquals("Carbs '90.123' must have up to '3' integer digits and '1' fraction digits.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testIngredientCarbsValueIsTooHigh() {
        Ingredient ingredient = createIngredientWithProperties("100", "100", "100", "123.1");
        Set<ConstraintViolation<Ingredient>> violations = validator.validate(ingredient);
        assertEquals(violations.size(), 1);
        assertEquals("Ingredient's carbs '123.1' must be lower than '100'.",
                violations.iterator().next().getMessage());

    }

    @Test
    public void testOwnershipIsNull() {
        Ingredient ingredient = new Ingredient("курица", null);
        ingredient.setIngredientCategory(new IngredientCategory("Milk", false));
        Set<ConstraintViolation<Ingredient>> violations = validator.validate(ingredient);
        assertEquals(violations.size(), 1);
        assertEquals("Ingredient's ownership mustn't be null.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testIngredientCategoryIsNull() {
        Ingredient ingredient = new Ingredient("курица",  new Ownership(OwnershipName.USER));
        Set<ConstraintViolation<Ingredient>> violations = validator.validate(ingredient);
        assertEquals(violations.size(), 1);
        assertEquals("Ingredient's category mustn't be null.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testIngredientIsValid() {
        Ingredient ingredient = new Ingredient("молоко", new BigDecimal("100"), new BigDecimal("100"),
                new BigDecimal("100"), new BigDecimal("100"), new Ownership(OwnershipName.USER));
        ingredient.setIngredientCategory(new IngredientCategory("Milk", false));
        Set<ConstraintViolation<Ingredient>> violations = validator.validate(ingredient);
        assertEquals(violations.size(), 0);
    }

    @After
    public void tearDown() {
        validatorFactory.close();
    }
}
