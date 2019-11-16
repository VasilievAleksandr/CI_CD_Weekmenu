package by.weekmenu.api.entity;

import org.apache.commons.lang3.StringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

import static org.junit.Assert.assertEquals;

public class IngredientCategoryTest {

    private static ValidatorFactory validatorFactory;
    private static Validator validator;

    @Before
    public void setUp() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Test
    public void testIngredientCategoryNameIsNull() {
        IngredientCategory ingredientCategory = new IngredientCategory(null, true);
        Set<ConstraintViolation<IngredientCategory>> violations = validator.validate(ingredientCategory);
        assertEquals(violations.size(), 1);
        assertEquals("IngredientCategory must have name.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testIngredientCategoryNameIsBlank() {
        IngredientCategory ingredientCategory = new IngredientCategory("   ", true);
        Set<ConstraintViolation<IngredientCategory>> violations = validator.validate(ingredientCategory);
        assertEquals(violations.size(), 1);
        assertEquals("IngredientCategory must have name.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testMenuCategoryNameIsEmpty() {
        IngredientCategory ingredientCategory = new IngredientCategory("", true);
        Set<ConstraintViolation<IngredientCategory>> violations = validator.validate(ingredientCategory);
        assertEquals(violations.size(), 1);
        assertEquals("IngredientCategory must have name.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testNameIsTooLong() {
        IngredientCategory ingredientCategory = new IngredientCategory("", true);
        String name = StringUtils.repeat("очень_длинное_название_категории", 20);
        ingredientCategory.setName(name);
        Set<ConstraintViolation<IngredientCategory>> violations = validator.validate(ingredientCategory);
        assertEquals(violations.size(), 1);
        assertEquals("IngredientCategory's name '" + name + "' must be '255' characters long",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testIngredientCategoryPriorityIsNegative() {
        IngredientCategory ingredientCategory = new IngredientCategory("Milk", true);
        ingredientCategory.setPriority(-100);
        Set<ConstraintViolation<IngredientCategory>> violations = validator.validate(ingredientCategory);
        assertEquals(violations.size(), 1);
        assertEquals("IngredientCategory's priority '-100' must be positive.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testIngredientCategoryPriorityIsZero() {
        IngredientCategory ingredientCategory = new IngredientCategory("Milk", true);
        ingredientCategory.setPriority(0);
        Set<ConstraintViolation<IngredientCategory>> violations = validator.validate(ingredientCategory);
        assertEquals(violations.size(), 1);
        assertEquals("IngredientCategory's priority '0' must be positive.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testIngredientCategoryImageLinkIsTooLong() {
        IngredientCategory ingredientCategory = new IngredientCategory("Milk", true);
        String imageLink = StringUtils.repeat("path_to_the_image", "/", 20);
        ingredientCategory.setImageLink(imageLink);
        Set<ConstraintViolation<IngredientCategory>> violations = validator.validate(ingredientCategory);
        assertEquals(violations.size(), 1);
        assertEquals("ImageLink's length of the ingredientCategory '" + imageLink + "' mustn't be more than '255' characters long.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testIngredientCategoryIsValid() {
        IngredientCategory ingredientCategory = new IngredientCategory("Milk", false);
        ingredientCategory.setImageLink("/images/milk.jpg");
        ingredientCategory.setPriority(1);
        Set<ConstraintViolation<IngredientCategory>> violations = validator.validate(ingredientCategory);
        assertEquals(violations.size(), 0);
    }

    @After
    public void tearDown() {
        validatorFactory.close();
    }
}