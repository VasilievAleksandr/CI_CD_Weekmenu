package by.weekmenu.api.entity;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

import static org.junit.Assert.assertEquals;

public class RecipeCategoryTest {

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
    public void testNameIsNull() {
        RecipeCategory recipeCategory = new RecipeCategory(null);
        Set<ConstraintViolation<RecipeCategory>> violations = validator.validate(recipeCategory);
        assertEquals(violations.size(), 1);
        assertEquals("RecipeCategory must have name.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testNameIsBlank() {
        RecipeCategory recipeCategory = new RecipeCategory("  ");
        Set<ConstraintViolation<RecipeCategory>> violations = validator.validate(recipeCategory);
        assertEquals(violations.size(), 1);
        assertEquals("RecipeCategory must have name.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testNameIsEmpty() {
        RecipeCategory recipeCategory = new RecipeCategory("");
        Set<ConstraintViolation<RecipeCategory>> violations = validator.validate(recipeCategory);
        assertEquals(violations.size(), 1);
        assertEquals("RecipeCategory must have name.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testIsValid() {
        RecipeCategory recipeCategory = new RecipeCategory("Курица");
        Set<ConstraintViolation<RecipeCategory>> violations = validator.validate(recipeCategory);
        assertEquals(violations.size(), 0);
    }
}
