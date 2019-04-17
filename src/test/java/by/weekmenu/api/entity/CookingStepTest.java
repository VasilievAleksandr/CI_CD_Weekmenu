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

public class CookingStepTest {

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
    public void testPriorityAreNegative() {
        CookingStep cookingStep = new CookingStep(-1, "Описание", "images/dinner.jpg");
        Set<ConstraintViolation<CookingStep>> violations = validator.validate(cookingStep);
        assertEquals(violations.size(), 1);
        assertEquals("Cooking priority '-1' must be positive or '0'.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testPriorityAreZero() {
        CookingStep cookingStep = new CookingStep(0, "Описание", "images/dinner.jpg");
        Set<ConstraintViolation<CookingStep>> violations = validator.validate(cookingStep);
        assertEquals(violations.size(), 1);
        assertEquals("Cooking priority '0' must be positive or '0'.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testDescriptionIsNull() {
        CookingStep cookingStep = new CookingStep(1, null, "images/dinner.jpg");
        Set<ConstraintViolation<CookingStep>> violations = validator.validate(cookingStep);
        assertEquals(violations.size(), 1);
        assertEquals("Cooking Step must have field 'description' filled.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testDescriptionIsBlank() {
        CookingStep cookingStep = new CookingStep(1, "  ", "images/dinner.jpg");
        Set<ConstraintViolation<CookingStep>> violations = validator.validate(cookingStep);
        assertEquals(violations.size(), 1);
        assertEquals("Cooking Step must have field 'description' filled.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testDescriptionIsEmpty() {
        CookingStep cookingStep = new CookingStep(1, "", "");
        Set<ConstraintViolation<CookingStep>> violations = validator.validate(cookingStep);
        assertEquals(violations.size(), 1);
        assertEquals("Cooking Step must have field 'description' filled.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testCookingStepImageLinkIsTooLong() {
        String imageLink = StringUtils.repeat("path_to_the_image", "/", 20);
        CookingStep cookingStep = new CookingStep();
        cookingStep.setPriority(1);
        cookingStep.setDescription("Описание");
        cookingStep.setImageLink(imageLink);
        Set<ConstraintViolation<CookingStep>> violations = validator.validate(cookingStep);
        assertEquals(violations.size(), 1);
        assertEquals("ImageLink's length of the cookingStep '" + imageLink + "' mustn't be more than '255' characters long.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testCookingStepIsValid() {
        CookingStep cookingStep = new CookingStep(1, "Описание", "images/dinner.jpg");
        Set<ConstraintViolation<CookingStep>> violations = validator.validate(cookingStep);
        assertEquals(violations.size(), 0);
    }

}