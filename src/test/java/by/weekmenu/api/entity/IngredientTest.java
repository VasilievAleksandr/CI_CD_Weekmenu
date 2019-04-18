package by.weekmenu.api.entity;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

public class IngredientTest {

    private static ValidatorFactory validatorFactory;
    private static Validator validator;

    @Before
    public void setUp() throws Exception {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    private Recipe getValidRecipe() {
        return new Recipe("Курица с ананасами", true, new CookingMethod("Тушение"),
                new Ownership("Пользователь"));
    }

    private RecipeIngredient getValidRecipeIngredient() {
        return new RecipeIngredient(new BigDecimal(100), getValidRecipe());
    }

    private RecipeIngredient getInvalidRecipeIngredient() {
        return new RecipeIngredient(null, getValidRecipe());
    }

    @Test
    public void testIngredientNameIsNull() {
        Ingredient ingredient = new Ingredient(null, new BigDecimal(100));
        Set<ConstraintViolation<Ingredient>> violations = validator.validate(ingredient);
        assertEquals(violations.size(), 1);
        assertEquals("Ingredient must have name.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testIngredientNameIsBlank() {
        Ingredient ingredient = new Ingredient("   ", new BigDecimal(100));
        Set<ConstraintViolation<Ingredient>> violations = validator.validate(ingredient);
        assertEquals(violations.size(), 1);
        assertEquals("Ingredient must have name.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testIngredientNameIsEmpty() {
        Ingredient ingredient = new Ingredient("", new BigDecimal(100));
        Set<ConstraintViolation<Ingredient>> violations = validator.validate(ingredient);
        assertEquals(violations.size(), 1);
        assertEquals("Ingredient must have name.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testIngredientPriceHasTooManyFractionDigits() {
        Ingredient ingredient = new Ingredient("", new BigDecimal(111.123));
        Set<ConstraintViolation<Ingredient>> violations = validator.validate(ingredient);
        assertEquals(violations.size(), 1);
        assertEquals("Ingredient's price '111.123' must have up to '7' integer digits and '2' fraction digits.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testIngredientPriceIsTooHigh() {
        Ingredient ingredient = new Ingredient("", new BigDecimal(12345678.12));
        Set<ConstraintViolation<Ingredient>> violations = validator.validate(ingredient);
        assertEquals(violations.size(), 1);
        assertEquals("Ingredient's price '12345678.12' must have up to '7' integer digits and '2' fraction digits.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testIngredientPriceIsNegative() {
        Ingredient ingredient = new Ingredient("", new BigDecimal(-111.12));
        Set<ConstraintViolation<Ingredient>> violations = validator.validate(ingredient);
        assertEquals(violations.size(), 1);
        assertEquals("Ingredient's price '-111.12' must be positive.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testIngredientCaloriesAreNegative() {
        Ingredient ingredient = new Ingredient(-100, 100, 100, 100);
        Set<ConstraintViolation<Ingredient>> violations = validator.validate(ingredient);
        assertEquals(violations.size(), 1);
        assertEquals("Ingredient's calories '-100' must be positive.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testIngredientCaloriesAreZero() {
        Ingredient ingredient = new Ingredient(0, 100, 100, 100);
        Set<ConstraintViolation<Ingredient>> violations = validator.validate(ingredient);
        assertEquals(violations.size(), 1);
        assertEquals("Ingredient's calories '0' must be positive.",
                violations.iterator().next().getMessage());
    }


    @Test
    public void testIngredientProteinsAreNegative() {
        Ingredient ingredient = new Ingredient(100, -100, 100, 100);
        Set<ConstraintViolation<Ingredient>> violations = validator.validate(ingredient);
        assertEquals(violations.size(), 1);
        assertEquals("Ingredient's proteins '-100' must be positive or '0'.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testIngredientFatsAreNegative() {
        Ingredient ingredient = new Ingredient(100, 100, -100, 100);
        Set<ConstraintViolation<Ingredient>> violations = validator.validate(ingredient);
        assertEquals(violations.size(), 1);
        assertEquals("Ingredient's fats '-100' must be positive or '0'.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testIngredientCarbsAreNegative() {
        Ingredient ingredient = new Ingredient(100, 100, 100, -100);
        Set<ConstraintViolation<Ingredient>> violations = validator.validate(ingredient);
        assertEquals(violations.size(), 1);
        assertEquals("Ingredient's carbs '-100' must be positive or '0'.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testHasInvalidRecipeIngredients() {
        Ingredient ingredient = new Ingredient("молоко", new BigDecimal(100));
        ingredient.getRecipeIngredients().add(getValidRecipeIngredient());
        ingredient.getRecipeIngredients().add(getInvalidRecipeIngredient());
        ingredient.getRecipeIngredients().add(null);
        Set<ConstraintViolation<Ingredient>> violations = validator.validate(ingredient);
        List<String> messages = violations.stream()
                .map((ConstraintViolation<Ingredient> violation) -> violation.getMessage())
                .collect(Collectors.toList());
        assertEquals(violations.size(), 3);
        assertTrue(messages.contains("RecipeIngredient must have name."));
        assertTrue(messages.contains("Ingredient must have list of recipeIngredients without null elements."));
    }

    @Test
    public void testUnitOfMeasureIsNull() {
        Ingredient ingredient = new Ingredient(null);
        Set<ConstraintViolation<Ingredient>> violations = validator.validate(ingredient);
        assertEquals(violations.size(), 1);
        assertEquals("Ingredient's unitOfMeasure mustn't be null.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testUnitOfMeasureIsInvalid() {
        Ingredient ingredient = new Ingredient(new UnitOfMeasure(null));
        Set<ConstraintViolation<Ingredient>> violations = validator.validate(ingredient);
        assertEquals(violations.size(), 1);
        assertEquals("UnitOfMeasure must have name.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testOwnershipIsNull() {
        Ingredient ingredient = new Ingredient("молоко", new BigDecimal(100), null);
        Set<ConstraintViolation<Ingredient>> violations = validator.validate(ingredient);
        assertEquals(violations.size(), 1);
        assertEquals("Ingredient's ownership mustn't be null.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testOwnershipIsInvalid() {
        Ingredient ingredient = new Ingredient("молоко", new BigDecimal(100), new Ownership(null));
        Set<ConstraintViolation<Ingredient>> violations = validator.validate(ingredient);
        assertEquals(violations.size(), 1);
        assertEquals("Ownership must have name.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testIngredientIsValid() {
        Ingredient ingredient = new Ingredient("молоко", new BigDecimal(100), 100, 100, 100, 100,
                new UnitOfMeasure("литр"), new Ownership("Пользователь"));
        ingredient.getRecipeIngredients().add(getValidRecipeIngredient());
        Set<ConstraintViolation<Ingredient>> violations = validator.validate(ingredient);
        assertEquals(violations.size(), 0);
    }

    @After
    public void tearDown() {
        validatorFactory.close();
    }
}