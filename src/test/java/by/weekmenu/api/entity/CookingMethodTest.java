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

public class CookingMethodTest {

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

    private Recipe getRecipe() {
        return new Recipe("Жаркое", true, new CookingMethod("Жарка"), new Ownership("Пользователь"));
    }

    @Test
    public void testNameIsNull() {
        CookingMethod cookingMethod = new CookingMethod(null);
        Set<ConstraintViolation<CookingMethod>> violations = validator.validate(cookingMethod);
        assertEquals(violations.size(), 1);
        assertEquals("Cooking Method must have name.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testNameIsBlank() {
        CookingMethod cookingMethod = new CookingMethod("  ");
        Set<ConstraintViolation<CookingMethod>> violations = validator.validate(cookingMethod);
        assertEquals(violations.size(), 1);
        assertEquals("Cooking Method must have name.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testNameIsEmpty() {
        CookingMethod cookingMethod = new CookingMethod("");
        Set<ConstraintViolation<CookingMethod>> violations = validator.validate(cookingMethod);
        assertEquals(violations.size(), 1);
        assertEquals("Cooking Method must have name.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testHasInvalidRecipes() {
        CookingMethod cookingMethod = new CookingMethod("Жарка");
        cookingMethod.getRecipes().add(null);
        Set<ConstraintViolation<CookingMethod>> violations = validator.validate(cookingMethod);
        assertEquals(violations.size(), 1);
        assertEquals("Cooking Method must have list of recipes without null elements",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testCookingMethodIsValid() {
        CookingMethod cookingMethod = new CookingMethod("Жарка");
        cookingMethod.getRecipes().add(getRecipe());
        Set<ConstraintViolation<CookingMethod>> violations = validator.validate(cookingMethod);
        assertEquals(violations.size(), 0);
    }
}