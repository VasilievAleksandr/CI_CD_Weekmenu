package by.weekmenu.api.entity;

import org.junit.Before;
import org.junit.Test;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.ConstraintViolation;

import java.math.BigDecimal;
import java.util.Set;

import static org.junit.Assert.*;

public class RecipeIngredientTest {


    private static ValidatorFactory validatorFactory;
    private static Validator validator;

    @Before
    public void setUp() throws Exception {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Test
    public void testRecipeIngredientQtyHasTooManyFractionDigits() {
        RecipeIngredient recipeIngredient = new RecipeIngredient(new BigDecimal("111.123"));
        recipeIngredient.setIngredient(new Ingredient("курица", new Ownership(OwnershipName.USER), new UnitOfMeasure("л", "литр" )));
        recipeIngredient.setRecipe(new Recipe("рецепт", true, new CookingMethod("жарка"),
                new Ownership(OwnershipName.USER)));
        Set<ConstraintViolation<RecipeIngredient>> violations = validator.validate(recipeIngredient);
        assertEquals(violations.size(), 1);
        assertEquals("RecipeIngredient's qty '111.123' must have up to '5' integer digits and '2' fraction digits.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testRecipeIngredientQtyIsTooHigh() {
        RecipeIngredient recipeIngredient = new RecipeIngredient(new BigDecimal("1111111.12"));
        recipeIngredient.setIngredient(new Ingredient("курица", new Ownership(OwnershipName.USER), new UnitOfMeasure("л", "литр" )));
        recipeIngredient.setRecipe(new Recipe("рецепт", true, new CookingMethod("жарка"),
                new Ownership(OwnershipName.USER)));
        Set<ConstraintViolation<RecipeIngredient>> violations = validator.validate(recipeIngredient);
        assertEquals(violations.size(), 1);
        assertEquals("RecipeIngredient's qty '1111111.12' must have up to '5' integer digits and '2' fraction digits.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testRecipeIngredientQtyIsNegative() {
        RecipeIngredient recipeIngredient = new RecipeIngredient(new BigDecimal("-111"));
        recipeIngredient.setIngredient(new Ingredient("курица", new Ownership(OwnershipName.USER), new UnitOfMeasure("л", "литр" )));
        recipeIngredient.setRecipe(new Recipe("рецепт", true, new CookingMethod("жарка"),
                new Ownership(OwnershipName.USER)));
        Set<ConstraintViolation<RecipeIngredient>> violations = validator.validate(recipeIngredient);
        assertEquals(violations.size(), 1);
        assertEquals("RecipeIngredient's qty '-111' must be positive.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testIngredientIsNull() {
        RecipeIngredient recipeIngredient = new RecipeIngredient(new BigDecimal("111"), null,
                new Recipe("рецепт", true, new CookingMethod("жарка"), new Ownership(OwnershipName.USER)));
        Set<ConstraintViolation<RecipeIngredient>> violations = validator.validate(recipeIngredient);
        assertEquals(violations.size(), 1);
        assertEquals("RecipeIngredient's Ingredient mustn't be null.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testIngredientIsInvalid() {
        RecipeIngredient recipeIngredient = new RecipeIngredient(new BigDecimal("111"),
                new Ingredient(null, new Ownership(OwnershipName.USER), new UnitOfMeasure("л", "литр" )),
                new Recipe("рецепт", true, new CookingMethod("жарка"), new Ownership(OwnershipName.USER)));
        Set<ConstraintViolation<RecipeIngredient>> violations = validator.validate(recipeIngredient);
        assertEquals(violations.size(), 1);
        assertEquals("Ingredient must have name.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testRecipeIsNull() {
        RecipeIngredient recipeIngredient = new RecipeIngredient(new BigDecimal("111"),
                new Ingredient("курица", new Ownership(OwnershipName.USER), new UnitOfMeasure("л", "литр" )),
                null);
        Set<ConstraintViolation<RecipeIngredient>> violations = validator.validate(recipeIngredient);
        assertEquals(violations.size(), 1);
        assertEquals("RecipeIngredient's Recipe mustn't be null.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testRecipeIsInvalid() {
        RecipeIngredient recipeIngredient = new RecipeIngredient(new BigDecimal("111"),
                new Ingredient("курица", new Ownership(OwnershipName.USER), new UnitOfMeasure("л", "литр" )),
                new Recipe(null, true, new CookingMethod("жарка"), new Ownership(OwnershipName.USER)));
        Set<ConstraintViolation<RecipeIngredient>> violations = validator.validate(recipeIngredient);
        assertEquals(violations.size(), 1);
        assertEquals("Recipe must have name.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testRecipeIngredientIsValid() {
        RecipeIngredient recipeIngredient = new RecipeIngredient(new BigDecimal("111"),
                new Ingredient("курица", new Ownership(OwnershipName.USER), new UnitOfMeasure("л", "литр" )),
                new Recipe("рецепт", true, new CookingMethod("жарка"), new Ownership(OwnershipName.USER)));
        Set<ConstraintViolation<RecipeIngredient>> violations = validator.validate(recipeIngredient);
        assertEquals(violations.size(), 0);
    }
}
