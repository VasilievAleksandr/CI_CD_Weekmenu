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

public class RecipeTest {

    private static ValidatorFactory validatorFactory;
    private static Validator validator;

    @Before
    public void setUp() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Test
    public void testNameIsNull() {
        Recipe recipe = new Recipe(null, true, new CookingMethod("Тушение"), new Ownership(OwnershipName.USER));
        Set<ConstraintViolation<Recipe>> violations = validator.validate(recipe);
        assertEquals(violations.size(), 1);
        assertEquals("Recipe must have name.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testNameIsBlank() {
        Recipe recipe = new Recipe("   ", true, new CookingMethod("Тушение"), new Ownership(OwnershipName.USER));
        Set<ConstraintViolation<Recipe>> violations = validator.validate(recipe);
        assertEquals(violations.size(), 1);
        assertEquals("Recipe must have name.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testNameIsEmpty() {
        Recipe recipe = new Recipe("", true, new CookingMethod("Тушение"), new Ownership(OwnershipName.USER));
        Set<ConstraintViolation<Recipe>> violations = validator.validate(recipe);
        assertEquals(violations.size(), 1);
        assertEquals("Recipe must have name.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testNameIsTooLong() {
        Recipe recipe = new Recipe("", true, new CookingMethod("Тушение"), new Ownership(OwnershipName.USER));
        String name = StringUtils.repeat("очень_длинное_название_рецепта", 20);
        recipe.setName(name);
        Set<ConstraintViolation<Recipe>> violations = validator.validate(recipe);
        assertEquals(violations.size(), 1);
        assertEquals("Recipe's name '" + name +"' must be '255' characters long",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testCookingTimeIsNegative() {
        Recipe recipe = new Recipe("Курица с ананасами", true, new CookingMethod("Тушение"), new Ownership(OwnershipName.USER));
        recipe.setCookingTime((new Short("-30")));
        Set<ConstraintViolation<Recipe>> violations = validator.validate(recipe);
        assertEquals(violations.size(), 1);
        assertEquals("Recipe's cookingTime '-30' must be positive or '0'.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testPreparingTimeIsNegative() {
        Recipe recipe = new Recipe("Курица с ананасами", true, new CookingMethod("Тушение"), new Ownership(OwnershipName.USER));
        recipe.setPreparingTime((new Short("-15")));
        Set<ConstraintViolation<Recipe>> violations = validator.validate(recipe);
        assertEquals(violations.size(), 1);
        assertEquals("Recipe's preparingTime '-15' must be positive or '0'.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testCaloriesAreNegative() {
        Recipe recipe = new Recipe("Курица с ананасами", true, new CookingMethod("Тушение"), new Ownership(OwnershipName.USER));
        recipe.setCalories(new BigDecimal(-100));
        Set<ConstraintViolation<Recipe>> violations = validator.validate(recipe);
        assertEquals(violations.size(), 1);
        assertEquals("Recipe's calories '-100' must be positive or '0'.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testCaloriesHasTooManyFractionDigits() {
        Recipe recipe = new Recipe("Курица с ананасами", true, new CookingMethod("Тушение"), new Ownership(OwnershipName.USER));
        recipe.setCalories(new BigDecimal("123.12"));
        Set<ConstraintViolation<Recipe>> violations = validator.validate(recipe);
        assertEquals(violations.size(), 1);
        assertEquals("Calories '123.12' must have up to '7' integer digits and '1' fraction digits.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testCaloriesValueIsTooHigh() {
        Recipe recipe = new Recipe("Курица с ананасами", true, new CookingMethod("Тушение"), new Ownership(OwnershipName.USER));
        recipe.setCalories(new BigDecimal("12345678.1"));
        Set<ConstraintViolation<Recipe>> violations = validator.validate(recipe);
        assertEquals(violations.size(), 1);
        assertEquals("Calories '12345678.1' must have up to '7' integer digits and '1' fraction digits.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testProteinsAreNegative() {
        Recipe recipe = new Recipe("Курица с ананасами", true, new CookingMethod("Тушение"), new Ownership(OwnershipName.USER));
        recipe.setProteins(new BigDecimal("-100"));
        Set<ConstraintViolation<Recipe>> violations = validator.validate(recipe);
        assertEquals(violations.size(), 1);
        assertEquals("Recipe's proteins '-100' must be positive or '0'.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testProteinsHasTooManyFractionDigits() {
        Recipe recipe = new Recipe("Курица с ананасами", true, new CookingMethod("Тушение"), new Ownership(OwnershipName.USER));
        recipe.setProteins(new BigDecimal("123.12"));
        Set<ConstraintViolation<Recipe>> violations = validator.validate(recipe);
        assertEquals(violations.size(), 1);
        assertEquals("Proteins '123.12' must have up to '7' integer digits and '1' fraction digits.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testProteinsValueIsTooHigh() {
        Recipe recipe = new Recipe("Курица с ананасами", true, new CookingMethod("Тушение"), new Ownership(OwnershipName.USER));
        recipe.setProteins(new BigDecimal("12345678.1"));
        Set<ConstraintViolation<Recipe>> violations = validator.validate(recipe);
        assertEquals(violations.size(), 1);
        assertEquals("Proteins '12345678.1' must have up to '7' integer digits and '1' fraction digits.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testFatsAreNegative() {
        Recipe recipe = new Recipe("Курица с ананасами", true, new CookingMethod("Тушение"), new Ownership(OwnershipName.USER));
        recipe.setFats(new BigDecimal("-100"));
        Set<ConstraintViolation<Recipe>> violations = validator.validate(recipe);
        assertEquals(violations.size(), 1);
        assertEquals("Recipe's fats '-100' must be positive or '0'.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testFatsHasTooManyFractionDigits() {
        Recipe recipe = new Recipe("Курица с ананасами", true, new CookingMethod("Тушение"), new Ownership(OwnershipName.USER));
        recipe.setFats(new BigDecimal("123.12"));
        Set<ConstraintViolation<Recipe>> violations = validator.validate(recipe);
        assertEquals(violations.size(), 1);
        assertEquals("Fats '123.12' must have up to '7' integer digits and '1' fraction digits.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testFatsValueIsTooHigh() {
        Recipe recipe = new Recipe("Курица с ананасами", true, new CookingMethod("Тушение"), new Ownership(OwnershipName.USER));
        recipe.setFats(new BigDecimal("12345678.1"));
        Set<ConstraintViolation<Recipe>> violations = validator.validate(recipe);
        assertEquals(violations.size(), 1);
        assertEquals("Fats '12345678.1' must have up to '7' integer digits and '1' fraction digits.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testCarbsAreNegative() {
        Recipe recipe = new Recipe("Курица с ананасами", true, new CookingMethod("Тушение"), new Ownership(OwnershipName.USER));
        recipe.setCarbs(new BigDecimal("-100"));
        Set<ConstraintViolation<Recipe>> violations = validator.validate(recipe);
        assertEquals(violations.size(), 1);
        assertEquals("Recipe's carbs '-100' must be positive or '0'.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testCarbsHasTooManyFractionDigits() {
        Recipe recipe = new Recipe("Курица с ананасами", true, new CookingMethod("Тушение"), new Ownership(OwnershipName.USER));
        recipe.setCarbs(new BigDecimal("123.12"));
        Set<ConstraintViolation<Recipe>> violations = validator.validate(recipe);
        assertEquals(violations.size(), 1);
        assertEquals("Carbs '123.12' must have up to '7' integer digits and '1' fraction digits.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testCarbsValueIsTooHigh() {
        Recipe recipe = new Recipe("Курица с ананасами", true, new CookingMethod("Тушение"), new Ownership(OwnershipName.USER));
        recipe.setCarbs(new BigDecimal("12345678.1"));
        Set<ConstraintViolation<Recipe>> violations = validator.validate(recipe);
        assertEquals(violations.size(), 1);
        assertEquals("Carbs '12345678.1' must have up to '7' integer digits and '1' fraction digits.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testImageLinkIsTooLong() {
        Recipe recipe = new Recipe("Курица с ананасами", true, new CookingMethod("Тушение"), new Ownership(OwnershipName.USER));
        String imageLink = StringUtils.repeat("path_to_the_image", "/", 20);
        recipe.setImageLink(imageLink);
        Set<ConstraintViolation<Recipe>> violations = validator.validate(recipe);
        assertEquals(violations.size(), 1);
        assertEquals("ImageLink's length of the recipe '" + imageLink + "' mustn't be more than '255' characters long.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testPortionsAreNegative() {
        Recipe recipe = new Recipe("Курица с ананасами", true, new CookingMethod("Тушение"), new Ownership(OwnershipName.USER));
        recipe.setPortions((short)-1);
        Set<ConstraintViolation<Recipe>> violations = validator.validate(recipe);
        assertEquals(violations.size(), 1);
        assertEquals("Recipe's portions '-1' must be positive.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testPortionsAreZero() {
        Recipe recipe = new Recipe("Курица с ананасами", true, new CookingMethod("Тушение"), new Ownership(OwnershipName.USER));
        recipe.setPortions((short)0);
        Set<ConstraintViolation<Recipe>> violations = validator.validate(recipe);
        assertEquals(violations.size(), 1);
        assertEquals("Recipe's portions '0' must be positive.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testSourceIsTooLong() {
        Recipe recipe = new Recipe("Курица с ананасами", true, new CookingMethod("Тушение"), new Ownership(OwnershipName.USER));
        String source = StringUtils.repeat("source_name", "/", 30);
        recipe.setSource(source);
        Set<ConstraintViolation<Recipe>> violations = validator.validate(recipe);
        assertEquals(violations.size(), 1);
        assertEquals("Recipe's source length '" + source + "' mustn't be more than '255' characters long.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testCookingMethodIsInvalid() {
        Recipe recipe = new Recipe("Курица с ананасами", true, new CookingMethod(null), new Ownership(OwnershipName.USER));
        Set<ConstraintViolation<Recipe>> violations = validator.validate(recipe);
        assertEquals(violations.size(), 1);
        assertEquals("Cooking Method must have name.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testCookingMethodIsNull() {
        Recipe recipe = new Recipe("Курица с ананасами", true, null, new Ownership(OwnershipName.USER));
        Set<ConstraintViolation<Recipe>> violations = validator.validate(recipe);
        assertEquals(violations.size(), 1);
        assertEquals("Recipe's cookingMethod mustn't be null.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testOwnershipIsNull() {
        Recipe recipe = new Recipe("Курица с ананасами", true, new CookingMethod("Тушение"), null);
        Set<ConstraintViolation<Recipe>> violations = validator.validate(recipe);
        assertEquals(violations.size(), 1);
        assertEquals("Recipe's ownership mustn't be null.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testRecipeIsValid() {
        Recipe recipe = new Recipe("Курица с ананасами", false, new CookingMethod("Тушение"), new Ownership(OwnershipName.USER));
        recipe.setCarbs(new BigDecimal("100"));
        recipe.setProteins(new BigDecimal("100"));
        recipe.setCalories(new BigDecimal("300"));
        recipe.setFats(new BigDecimal("100"));
        recipe.setCookingTime(new Short("30"));
        recipe.setPreparingTime(new Short("15"));
        recipe.setImageLink("images/recipe.jpg");
        recipe.setPortions((short)2);
        recipe.setSource("http://bestrecipes.com/best-recipe");
        Set<ConstraintViolation<Recipe>> violations = validator.validate(recipe);
        assertEquals(violations.size(), 0);
    }

    @After
    public void tearDown() {
        validatorFactory.close();
    }
}