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


    private static Validator validator;

    @Before
    public void setUp() {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Test
    public void testRecipeIngredientQtyHasTooManyFractionDigits() {
        Ingredient ingredient = new Ingredient("курица", new Ownership(OwnershipName.USER));
        ingredient.setIngredientCategory(new IngredientCategory("milk", false));
        RecipeIngredient recipeIngredient = new RecipeIngredient(new BigDecimal("111.123"));
        recipeIngredient.setIngredient(ingredient);
        recipeIngredient.setRecipe(new Recipe("рецепт", true, new CookingMethod("жарка"),
                new Ownership(OwnershipName.USER)));
        recipeIngredient.setUnitOfMeasure(new UnitOfMeasure("Гр", "Грамм"));
        Set<ConstraintViolation<RecipeIngredient>> violations = validator.validate(recipeIngredient);
        assertEquals(violations.size(), 1);
        assertEquals("RecipeIngredient's qty '111.123' must have up to '5' integer digits and '2' fraction digits.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testRecipeIngredientQtyIsTooHigh() {
        Ingredient ingredient = new Ingredient("курица", new Ownership(OwnershipName.USER));
        ingredient.setIngredientCategory(new IngredientCategory("milk", false));
        RecipeIngredient recipeIngredient = new RecipeIngredient(new BigDecimal("1111111.12"));
        recipeIngredient.setIngredient(ingredient);
        recipeIngredient.setRecipe(new Recipe("рецепт", true, new CookingMethod("жарка"),
                new Ownership(OwnershipName.USER)));
        recipeIngredient.setUnitOfMeasure(new UnitOfMeasure("Гр", "Грамм"));
        Set<ConstraintViolation<RecipeIngredient>> violations = validator.validate(recipeIngredient);
        assertEquals(violations.size(), 1);
        assertEquals("RecipeIngredient's qty '1111111.12' must have up to '5' integer digits and '2' fraction digits.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testRecipeIngredientQtyIsNegative() {
        Ingredient ingredient = new Ingredient("курица", new Ownership(OwnershipName.USER));
        ingredient.setIngredientCategory(new IngredientCategory("milk", false));
        RecipeIngredient recipeIngredient = new RecipeIngredient(new BigDecimal("-111"));
        recipeIngredient.setIngredient(ingredient);
        recipeIngredient.setRecipe(new Recipe("рецепт", true, new CookingMethod("жарка"),
                new Ownership(OwnershipName.USER)));
        recipeIngredient.setUnitOfMeasure(new UnitOfMeasure("Гр", "Грамм"));
        Set<ConstraintViolation<RecipeIngredient>> violations = validator.validate(recipeIngredient);
        assertEquals(violations.size(), 1);
        assertEquals("RecipeIngredient's qty '-111' must be positive.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testIngredientIsNull() {
        RecipeIngredient recipeIngredient = new RecipeIngredient(new BigDecimal("111"), null,
                new Recipe("рецепт", true, new CookingMethod("жарка"), new Ownership(OwnershipName.USER)));
        recipeIngredient.setUnitOfMeasure(new UnitOfMeasure("Гр", "Грамм"));
        Set<ConstraintViolation<RecipeIngredient>> violations = validator.validate(recipeIngredient);
        assertEquals(violations.size(), 1);
        assertEquals("RecipeIngredient's Ingredient mustn't be null.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testIngredientIsInvalid() {
        Ingredient ingredient = new Ingredient(" ", new Ownership(OwnershipName.USER));
        ingredient.setIngredientCategory(new IngredientCategory("milk", false));
        RecipeIngredient recipeIngredient = new RecipeIngredient(new BigDecimal("111"), ingredient,
                new Recipe("рецепт", true, new CookingMethod("жарка"), new Ownership(OwnershipName.USER)));
        recipeIngredient.setUnitOfMeasure(new UnitOfMeasure("Гр", "Грамм"));
        Set<ConstraintViolation<RecipeIngredient>> violations = validator.validate(recipeIngredient);
        assertEquals(violations.size(), 1);
        assertEquals("Ingredient must have name.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testRecipeIsNull() {
        Ingredient ingredient = new Ingredient("курица", new Ownership(OwnershipName.USER));
        ingredient.setIngredientCategory(new IngredientCategory("milk", false));
        RecipeIngredient recipeIngredient = new RecipeIngredient(new BigDecimal("111"), ingredient,null);
        recipeIngredient.setUnitOfMeasure(new UnitOfMeasure("Гр", "Грамм"));
        Set<ConstraintViolation<RecipeIngredient>> violations = validator.validate(recipeIngredient);
        assertEquals(violations.size(), 1);
        assertEquals("RecipeIngredient's Recipe mustn't be null.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testRecipeIsInvalid() {
        Ingredient ingredient = new Ingredient("курица", new Ownership(OwnershipName.USER));
        ingredient.setIngredientCategory(new IngredientCategory("milk", false));
        RecipeIngredient recipeIngredient = new RecipeIngredient(new BigDecimal("111"), ingredient,
                new Recipe(null, true, new CookingMethod("жарка"), new Ownership(OwnershipName.USER)));
        recipeIngredient.setUnitOfMeasure(new UnitOfMeasure("Гр", "Грамм"));
        Set<ConstraintViolation<RecipeIngredient>> violations = validator.validate(recipeIngredient);
        assertEquals(violations.size(), 1);
        assertEquals("Recipe must have name.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testUnitOfMeasureIsNull() {
        RecipeIngredient recipeIngredient = new RecipeIngredient();
        Ingredient ingredient = new Ingredient("курица", new Ownership(OwnershipName.USER));
        ingredient.setIngredientCategory(new IngredientCategory("milk", false));
        recipeIngredient.setIngredient(ingredient);
        recipeIngredient.setRecipe(new Recipe("рецепт", true, new CookingMethod("жарка"),
                new Ownership(OwnershipName.USER)));
        recipeIngredient.setUnitOfMeasure(null);
        Set<ConstraintViolation<RecipeIngredient>> violations = validator.validate(recipeIngredient);
        assertEquals(violations.size(), 1);
        assertEquals("RecipeIngredient must have UnitOfMeasure",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testUnitOfMeasureIsInvalid() {
        RecipeIngredient recipeIngredient = new RecipeIngredient();
        Ingredient ingredient = new Ingredient("курица", new Ownership(OwnershipName.USER));
        ingredient.setIngredientCategory(new IngredientCategory("milk", false));
        recipeIngredient.setIngredient(ingredient);
        recipeIngredient.setRecipe(new Recipe("рецепт", true, new CookingMethod("жарка"),
                new Ownership(OwnershipName.USER)));
        recipeIngredient.setUnitOfMeasure(new UnitOfMeasure("Гр", null));
        Set<ConstraintViolation<RecipeIngredient>> violations = validator.validate(recipeIngredient);
        assertEquals(violations.size(), 1);
        assertEquals("UnitOfMeasure must have full name.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testRecipeIngredientIsValid() {
        Ingredient ingredient = new Ingredient("курица", new Ownership(OwnershipName.USER));
        ingredient.setIngredientCategory(new IngredientCategory("milk", false));
        RecipeIngredient recipeIngredient = new RecipeIngredient(new BigDecimal("111"), ingredient,
                new Recipe("рецепт", true, new CookingMethod("жарка"), new Ownership(OwnershipName.USER)));
        recipeIngredient.setUnitOfMeasure(new UnitOfMeasure("Гр", "Грамм"));
        Set<ConstraintViolation<RecipeIngredient>> violations = validator.validate(recipeIngredient);
        assertEquals(violations.size(), 0);
    }
}
