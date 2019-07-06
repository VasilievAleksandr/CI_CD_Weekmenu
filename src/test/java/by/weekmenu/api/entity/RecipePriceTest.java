package by.weekmenu.api.entity;

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

public class RecipePriceTest {

    private static ValidatorFactory validatorFactory;
    private static Validator validator;

    @Before
    public void setUp() throws Exception {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @After
    public void tearDown() {
        validatorFactory.close();
    }

    private Region getRegion() {
        Country country = new Country("Беларусь", "BY", getCurrency());
        return new Region("Минский район", country);
    }

    private Currency getCurrency() {
        return new Currency("руб.", "BYN", true);
    }

    @Test
    public void testRecipePricePriceValueHasTooManyFractionDigits() {
        RecipePrice recipePrice = new RecipePrice(new BigDecimal("111.123"));
        recipePrice.setRecipe(new Recipe("рецепт", true, new CookingMethod("жарка"),
                new Ownership(OwnershipName.USER)));
        recipePrice.setRegion(getRegion());
        Set<ConstraintViolation<RecipePrice>> violations = validator.validate(recipePrice);
        assertEquals(violations.size(), 1);
        assertEquals("Recipe_Price's Price_Value '111.123' must have up to '7' integer digits and '2' fraction digits.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testRecipePricePriceValueIsTooHigh() {
        RecipePrice recipePrice = new RecipePrice(new BigDecimal("1111111111.123"));
        recipePrice.setRecipe(new Recipe("рецепт", true, new CookingMethod("жарка"),
                new Ownership(OwnershipName.USER)));
        recipePrice.setRegion(getRegion());
        Set<ConstraintViolation<RecipePrice>> violations = validator.validate(recipePrice);
        assertEquals(violations.size(), 1);
        assertEquals("Recipe_Price's Price_Value '1111111111.123' must have up to '7' integer digits and '2' fraction digits.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testRecipePricePriceValueIsNegative() {
        RecipePrice recipePrice = new RecipePrice(new BigDecimal("-111"));
        recipePrice.setRecipe(new Recipe("рецепт", true, new CookingMethod("жарка"),
                new Ownership(OwnershipName.USER)));
        recipePrice.setRegion(getRegion());
        Set<ConstraintViolation<RecipePrice>> violations = validator.validate(recipePrice);
        assertEquals(violations.size(), 1);
        assertEquals("Recipe_Price's Price_Value '-111' must be positive.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testRecipeIsNull() {
        RecipePrice recipePrice = new RecipePrice(new BigDecimal("111"), null,
                getRegion());
        Set<ConstraintViolation<RecipePrice>> violations = validator.validate(recipePrice);
        assertEquals(violations.size(), 1);
        assertEquals("Recipe_Price's Recipe mustn't be null.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testRecipeIsInvalid() {
        RecipePrice recipePrice = new RecipePrice(new BigDecimal("111"),
                new Recipe(null, true, new CookingMethod("жарка"), new Ownership(OwnershipName.USER)),
                getRegion());
        Set<ConstraintViolation<RecipePrice>> violations = validator.validate(recipePrice);
        assertEquals(violations.size(), 1);
        assertEquals("Recipe must have name.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testRegionIsNull() {
        RecipePrice recipePrice = new RecipePrice(new BigDecimal("111"),
                new Recipe("рецепт", true, new CookingMethod("жарка"), new Ownership(OwnershipName.USER)),
                null);
        Set<ConstraintViolation<RecipePrice>> violations = validator.validate(recipePrice);
        assertEquals(violations.size(), 1);
        assertEquals("Recipe_Price's Currency mustn't be null.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testRecipePriceIsValid() {
        RecipePrice recipePrice = new RecipePrice(new BigDecimal("111"),
                new Recipe("рецепт", true, new CookingMethod("жарка"), new Ownership(OwnershipName.USER)),
                getRegion());
        Set<ConstraintViolation<RecipePrice>> violations = validator.validate(recipePrice);
        assertEquals(violations.size(), 0);
    }
}
