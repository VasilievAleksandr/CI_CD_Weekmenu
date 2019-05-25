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
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

public class RecipeTest {

    private static ValidatorFactory validatorFactory;
    private static Validator validator;

    @Before
    public void setUp() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    private Menu getValidMenu() {
        return new Menu("Бюджетное", true, new Ownership("Пользователь"));
    }

    private DishType getInvalidDishType() {
        return new DishType("Обед", null);
    }

    private DayOfWeek getValidDayOfWeek() {
        return new DayOfWeek("Понедельник", "ПН");
    }

    private Region getRegion() {
        Country country = new Country("Беларусь", "BY", getCurrency());
        return new Region("Минский район", country);
    }

    private Currency getCurrency() {
        return new Currency("руб.", "BYN", true);
    }

    @Test
    public void testNameIsNull() {
        Recipe recipe = new Recipe(null, true, new CookingMethod("Тушение"), new Ownership("Пользователь"));
        Set<ConstraintViolation<Recipe>> violations = validator.validate(recipe);
        assertEquals(violations.size(), 1);
        assertEquals("Recipe must have name.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testNameIsBlank() {
        Recipe recipe = new Recipe("   ", true, new CookingMethod("Тушение"), new Ownership("Пользователь"));
        Set<ConstraintViolation<Recipe>> violations = validator.validate(recipe);
        assertEquals(violations.size(), 1);
        assertEquals("Recipe must have name.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testNameIsEmpty() {
        Recipe recipe = new Recipe("", true, new CookingMethod("Тушение"), new Ownership("Пользователь"));
        Set<ConstraintViolation<Recipe>> violations = validator.validate(recipe);
        assertEquals(violations.size(), 1);
        assertEquals("Recipe must have name.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testNameIsTooLong() {
        Recipe recipe = new Recipe("", true, new CookingMethod("Тушение"), new Ownership("Пользователь"));
        String name = StringUtils.repeat("очень_длинное_название_рецепта", 20);
        recipe.setName(name);
        Set<ConstraintViolation<Recipe>> violations = validator.validate(recipe);
        assertEquals(violations.size(), 1);
        assertEquals("Recipe's name '" + name +"' must be '255' characters long",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testCookingTimeIsNegative() {
        Recipe recipe = new Recipe("Курица с ананасами", true, new CookingMethod("Тушение"), new Ownership("Пользователь"));
        recipe.setCookingTime((new Short("-30")));
        Set<ConstraintViolation<Recipe>> violations = validator.validate(recipe);
        assertEquals(violations.size(), 1);
        assertEquals("Recipe's cookingTime '-30' must be positive or '0'.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testPreparingTimeIsNegative() {
        Recipe recipe = new Recipe("Курица с ананасами", true, new CookingMethod("Тушение"), new Ownership("Пользователь"));
        recipe.setPreparingTime((new Short("-15")));
        Set<ConstraintViolation<Recipe>> violations = validator.validate(recipe);
        assertEquals(violations.size(), 1);
        assertEquals("Recipe's preparingTime '-15' must be positive or '0'.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testCaloriesAreNegative() {
        Recipe recipe = new Recipe("Курица с ананасами", true, new CookingMethod("Тушение"), new Ownership("Пользователь"));
        recipe.setCalories(-100);
        Set<ConstraintViolation<Recipe>> violations = validator.validate(recipe);
        assertEquals(violations.size(), 1);
        assertEquals("Recipe's calories '-100' must be positive.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testCaloriesAreZero() {
        Recipe recipe = new Recipe("Курица с ананасами", true, new CookingMethod("Тушение"), new Ownership("Пользователь"));
        recipe.setCalories(0);
        Set<ConstraintViolation<Recipe>> violations = validator.validate(recipe);
        assertEquals(violations.size(), 1);
        assertEquals("Recipe's calories '0' must be positive.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testProteinsAreNegative() {
        Recipe recipe = new Recipe("Курица с ананасами", true, new CookingMethod("Тушение"), new Ownership("Пользователь"));
        recipe.setProteins(-100);
        Set<ConstraintViolation<Recipe>> violations = validator.validate(recipe);
        assertEquals(violations.size(), 1);
        assertEquals("Recipe's proteins '-100' must be positive or '0'.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testFatsAreNegative() {
        Recipe recipe = new Recipe("Курица с ананасами", true, new CookingMethod("Тушение"), new Ownership("Пользователь"));
        recipe.setFats(-100);
        Set<ConstraintViolation<Recipe>> violations = validator.validate(recipe);
        assertEquals(violations.size(), 1);
        assertEquals("Recipe's fats '-100' must be positive or '0'.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testCarbsAreNegative() {
        Recipe recipe = new Recipe("Курица с ананасами", true, new CookingMethod("Тушение"), new Ownership("Пользователь"));
        recipe.setCarbs(-100);
        Set<ConstraintViolation<Recipe>> violations = validator.validate(recipe);
        assertEquals(violations.size(), 1);
        assertEquals("Recipe's carbs '-100' must be positive or '0'.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testImageLinkIsTooLong() {
        Recipe recipe = new Recipe("Курица с ананасами", true, new CookingMethod("Тушение"), new Ownership("Пользователь"));
        String imageLink = StringUtils.repeat("path_to_the_image", "/", 20);
        recipe.setImageLink(imageLink);
        Set<ConstraintViolation<Recipe>> violations = validator.validate(recipe);
        assertEquals(violations.size(), 1);
        assertEquals("ImageLink's length of the recipe '" + imageLink + "' mustn't be more than '255' characters long.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testIsActiveIsNull() {
        Recipe recipe = new Recipe("Курица с ананасами", null, new CookingMethod("Тушение"), new Ownership("Пользователь"));
        Set<ConstraintViolation<Recipe>> violations = validator.validate(recipe);
        assertEquals(violations.size(), 1);
        assertEquals("Recipe must have field 'isActive' defined.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testHasInvalidRecipePrice() {
        Recipe recipe = new Recipe("Курица с ананасами", true, new CookingMethod("Тушение"), new Ownership("Пользователь"));
        RecipePrice recipeCurrency = new RecipePrice(new BigDecimal("-1.11"), recipe, getRegion());
        recipe.getRecipePrices().add(recipeCurrency);
        recipe.getRecipePrices().add(null);
        Set<ConstraintViolation<Recipe>> violations = validator.validate(recipe);
        List<String> messages = violations.stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.toList());
        assertEquals(violations.size(), 2);
        assertTrue(messages.contains("Recipe must have list of recipePrices without null elements."));
        assertTrue(messages.contains("Recipe_Price's Price_Value '-1.11' must be positive."));
    }

    @Test
    public void testHasInvalidRecipeIngredients() {
        Recipe recipe = new Recipe("Курица с ананасами", true, new CookingMethod("Тушение"), new Ownership("Пользователь"));
        recipe.getRecipeIngredients().add(
                new RecipeIngredient(new BigDecimal("-100"),
                        new Ingredient("курица", new Ownership("пользователь"),
                                new UnitOfMeasure("л","литр")), recipe)
        );
        recipe.getRecipeIngredients().add(null);
        Set<ConstraintViolation<Recipe>> violations = validator.validate(recipe);
        List<String> messages = violations.stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.toList());
        assertEquals(violations.size(), 2);
        assertTrue(messages.contains("RecipeIngredient's qty '-100' must be positive."));
        assertTrue(messages.contains("Recipe must have list of recipeIngredients without null elements."));
    }

    @Test
    public void testHasInvalidMenuRecipes() {
        Recipe recipe = new Recipe("Курица с ананасами", true, new CookingMethod("Тушение"), new Ownership("Пользователь"));
        recipe.getMenuRecipes().add(new MenuRecipe(getValidMenu(), null, getInvalidDishType(), getValidDayOfWeek()));
        recipe.getMenuRecipes().add(null);
        Set<ConstraintViolation<Recipe>> violations = validator.validate(recipe);
        List<String> messages = violations.stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.toList());
        assertEquals(violations.size(), 3);
        assertTrue(messages.contains("MenuRecipe must have recipe."));
        assertTrue(messages.contains("DishType must have field 'isActive' defined."));
        assertTrue(messages.contains("Recipe must have list of menuRecipes without null elements."));
    }

    @Test
    public void testCookingMethodIsInvalid() {
        Recipe recipe = new Recipe("Курица с ананасами", true, new CookingMethod(null), new Ownership("Пользователь"));
        Set<ConstraintViolation<Recipe>> violations = validator.validate(recipe);
        assertEquals(violations.size(), 1);
        assertEquals("Cooking Method must have name.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testCookingMethodIsNull() {
        Recipe recipe = new Recipe("Курица с ананасами", true, null, new Ownership("Пользователь"));
        Set<ConstraintViolation<Recipe>> violations = validator.validate(recipe);
        assertEquals(violations.size(), 1);
        assertEquals("Recipe's cookingMethod mustn't be null.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testHasInvalidCookingSteps() {
        Recipe recipe = new Recipe("Курица с ананасами", true, new CookingMethod("Тушение"), new Ownership("Пользователь"));
        recipe.getCookingSteps().add(new CookingStep(100, null, "path/to/image"));
        recipe.getMenuRecipes().add(null);
        Set<ConstraintViolation<Recipe>> violations = validator.validate(recipe);
        List<String> messages = violations.stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.toList());
        assertEquals(violations.size(), 2);
        assertTrue(messages.contains("Cooking Step must have field 'description' filled."));
        assertTrue(messages.contains("Recipe must have list of menuRecipes without null elements."));
    }

    @Test
    public void testOwnershipIsInvalid() {
        Recipe recipe = new Recipe("Курица с ананасами", true, new CookingMethod("Тушение"), new Ownership("   "));
        Set<ConstraintViolation<Recipe>> violations = validator.validate(recipe);
        assertEquals(violations.size(), 1);
        assertEquals("Ownership must have name.",
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
        Recipe recipe = new Recipe("Курица с ананасами", true, new CookingMethod("Тушение"), new Ownership("Пользователь"));
        recipe.setCarbs(100);
        recipe.setProteins(100);
        recipe.setCalories(300);
        recipe.setFats(100);
        recipe.setCookingTime(new Short("30"));
        recipe.setPreparingTime(new Short("15"));
        recipe.setImageLink("images/recipe.jpg");
        recipe.getRecipeIngredients().add(
                new RecipeIngredient(new BigDecimal("100"),
                        new Ingredient("курица", new Ownership("пользователь"),
                                new UnitOfMeasure("л","литр")), recipe)
        );
        recipe.getRecipePrices().add(new RecipePrice(new BigDecimal("1.11"),
                new Recipe("Курица с ананасами", true, new CookingMethod("Тушение"), new Ownership("Пользователь")),
                getRegion()));
        recipe.getCookingSteps().add(new CookingStep(100, "Нарезать курицу", "images/choped_chicken.jpg"));
        Set<ConstraintViolation<Recipe>> violations = validator.validate(recipe);
        assertEquals(violations.size(), 0);
    }

    @After
    public void tearDown() {
        validatorFactory.close();
    }
}