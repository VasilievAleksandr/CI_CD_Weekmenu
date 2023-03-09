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

public class RecipeSubcategoryTest {

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
        RecipeSubcategory recipeSubcategory = new RecipeSubcategory(null);
        Set<ConstraintViolation<RecipeSubcategory>> violations = validator.validate(recipeSubcategory);
        assertEquals(violations.size(), 1);
        assertEquals("RecipeSubcategory must have name.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testNameIsBlank() {
        RecipeSubcategory recipeSubcategory = new RecipeSubcategory("  ");
        Set<ConstraintViolation<RecipeSubcategory>> violations = validator.validate(recipeSubcategory);
        assertEquals(violations.size(), 1);
        assertEquals("RecipeSubcategory must have name.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testNameIsEmpty() {
        RecipeSubcategory recipeSubcategory = new RecipeSubcategory("");
        Set<ConstraintViolation<RecipeSubcategory>> violations = validator.validate(recipeSubcategory);
        assertEquals(violations.size(), 1);
        assertEquals("RecipeSubcategory must have name.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testIsValid() {
        RecipeSubcategory recipeSubcategory = new RecipeSubcategory("Курица тушеная");
        Set<ConstraintViolation<RecipeSubcategory>> violations = validator.validate(recipeSubcategory);
        assertEquals(violations.size(), 0);
    }
}
