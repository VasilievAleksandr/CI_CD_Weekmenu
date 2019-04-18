package by.weekmenu.api.entity;

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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class MenuTest {

    private static ValidatorFactory validatorFactory;
    private static Validator validator;

    @Before
    public void setUp() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    private Recipe getValidRecipe() {
        return new Recipe("Курица с ананасами", true, new CookingMethod("Тушение"), new Ownership("Пользователь"));
    }

    private DishType getValidDishType() {
        return new DishType("Обед", true);
    }

    private DishType getInvalidDishType() {
        return new DishType("Обед", null);
    }

    private DayOfWeek getValidDayOfWeek() {
        return new DayOfWeek("Понедельник", "ПН");
    }

    @Test
    public void testNameIsNull() {
        Menu menu = new Menu(null, true, new Ownership("Пользователь"));
        Set<ConstraintViolation<Menu>> violations = validator.validate(menu);
        assertEquals(violations.size(), 1);
        assertEquals("Menu must have name.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testNameIsBlank() {
        Menu menu = new Menu("   ", true, new Ownership("Пользователь"));
        Set<ConstraintViolation<Menu>> violations = validator.validate(menu);
        assertEquals(violations.size(), 1);
        assertEquals("Menu must have name.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testNameIsEmpty() {
        Menu menu = new Menu("", true, new Ownership("Пользователь"));
        Set<ConstraintViolation<Menu>> violations = validator.validate(menu);
        assertEquals(violations.size(), 1);
        assertEquals("Menu must have name.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testCaloriesAreNegative() {
        Menu menu = new Menu("Бюджетное", true, new Ownership("Пользователь"));
        menu.setCalories(-100);
        Set<ConstraintViolation<Menu>> violations = validator.validate(menu);
        assertEquals(violations.size(), 1);
        assertEquals("Menu's calories '-100' must be positive.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testCaloriesAreZero() {
        Menu menu = new Menu("Бюджетное", true, new Ownership("Пользователь"));
        menu.setCalories(0);
        Set<ConstraintViolation<Menu>> violations = validator.validate(menu);
        assertEquals(violations.size(), 1);
        assertEquals("Menu's calories '0' must be positive.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testProteinsAreNegative() {
        Menu menu = new Menu("Бюджетное", true, new Ownership("Пользователь"));
        menu.setProteins(-150);
        Set<ConstraintViolation<Menu>> violations = validator.validate(menu);
        assertEquals(violations.size(), 1);
        assertEquals("Menu's proteins '-150' must be positive or '0'.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testFatsAreNegative() {
        Menu menu = new Menu("Бюджетное", true, new Ownership("Пользователь"));
        menu.setFats(-150);
        Set<ConstraintViolation<Menu>> violations = validator.validate(menu);
        assertEquals(violations.size(), 1);
        assertEquals("Menu's fats '-150' must be positive or '0'.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testCarbsAreNegative() {
        Menu menu = new Menu("Бюджетное", true, new Ownership("Пользователь"));
        menu.setCarbs(-150);
        Set<ConstraintViolation<Menu>> violations = validator.validate(menu);
        assertEquals(violations.size(), 1);
        assertEquals("Menu's carbs '-150' must be positive or '0'.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testPriceHasTooManyFractionDigits() {
        Menu menu = new Menu("Бюджетное", true, new Ownership("Пользователь"));
        menu.setPrice(new BigDecimal("20.123"));
        Set<ConstraintViolation<Menu>> violations = validator.validate(menu);
        assertEquals(violations.size(), 1);
        assertEquals("Menu's price '20.123' must have up to '3' integer digits and '2' fraction digits.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testPriceIsTooHigh() {
        Menu menu = new Menu("Бюджетное", true, new Ownership("Пользователь"));
        menu.setPrice(new BigDecimal("1234.12"));
        Set<ConstraintViolation<Menu>> violations = validator.validate(menu);
        assertEquals(violations.size(), 1);
        assertEquals("Menu's price '1234.12' must have up to '3' integer digits and '2' fraction digits.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testPriceIsNegative() {
        Menu menu = new Menu("Бюджетное", true, new Ownership("Пользователь"));
        menu.setPrice(new BigDecimal("-12.1"));
        Set<ConstraintViolation<Menu>> violations = validator.validate(menu);
        assertEquals(violations.size(), 1);
        assertEquals("Menu's price '-12.1' must be positive.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testIsActiveIsNull() {
        Menu menu = new Menu("Бюджетное", null, new Ownership("Пользователь"));
        Set<ConstraintViolation<Menu>> violations = validator.validate(menu);
        assertEquals(violations.size(), 1);
        assertEquals("Menu must have field 'isActive' defined.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testHasInvalidMenuRecipes() {
        Menu menu = new Menu("Бюджетное", true, new Ownership("Пользователь"));
        menu.getMenuRecipes().add(new MenuRecipe(getValidRecipe(), getInvalidDishType(), getValidDayOfWeek()));
        menu.getMenuRecipes().add(null);
        Set<ConstraintViolation<Menu>> violations = validator.validate(menu);
        List<String> messages = violations.stream()
                .map((ConstraintViolation<Menu> violation) -> violation.getMessage())
                .collect(Collectors.toList());
        assertEquals(violations.size(), 3);
        assertTrue(messages.contains("MenuRecipe must have menu."));
        assertTrue(messages.contains("DishType must have field 'isActive' defined."));
        assertTrue(messages.contains("Menu must have list of menuRecipes without null elements."));
    }

    @Test
    public void testHasInvalidMenuCategory() {
        Menu menu = new Menu("Бюджетное", true, new Ownership("Пользователь"));
        menu.setMenuCategory(new MenuCategory("", true));
        Set<ConstraintViolation<Menu>> violations = validator.validate(menu);
        assertEquals(violations.size(), 1);
        assertEquals("MenuCategory must have name.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testOwnershipIsNull() {
        Menu menu = new Menu("Бюджетное", true, null);
        Set<ConstraintViolation<Menu>> violations = validator.validate(menu);
        assertEquals(violations.size(), 1);
        assertEquals("Menu's ownership mustn't be null.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testOwnershipIsInvalid() {
        Menu menu = new Menu("Бюджетное", true, new Ownership(null));
        Set<ConstraintViolation<Menu>> violations = validator.validate(menu);
        assertEquals(violations.size(), 1);
        assertEquals("Ownership must have name.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testHasInvalidDailyMenuStatistics() {
        Menu menu = new Menu("Бюджетное", true, new Ownership("Пользователь"));
        menu.getDailyMenuStatistics().add(new DailyMenuStatistics(null, menu));
        menu.getDailyMenuStatistics().add(null);
        Set<ConstraintViolation<Menu>> violations = validator.validate(menu);
        List<String> messages = violations.stream()
                .map((ConstraintViolation<Menu> violation) -> violation.getMessage())
                .collect(Collectors.toList());
        assertEquals(violations.size(), 2);
        assertTrue(messages.contains("DailyMenuStatistics must have have dayOfWeek."));
        assertTrue(messages.contains("Menu must have list of dailyMenuStatistics without null elements."));
    }

    @Test
    public void testMenuIsValid() {
        Menu menu = new Menu("Бюджетное", true, new Ownership("Пользователь"));
        menu.setMenuCategory(new MenuCategory("Вегетарианское", true));
        menu.getMenuRecipes().add(new MenuRecipe(menu, getValidRecipe(), getValidDishType(), getValidDayOfWeek()));
        menu.setCarbs(100);
        menu.setFats(100);
        menu.setProteins(150);
        menu.setCalories(400);
        menu.setPrice(new BigDecimal("123.12"));
        menu.getDailyMenuStatistics().add(new DailyMenuStatistics(new DayOfWeek("Понедельник", "ПН"), menu));
        Set<ConstraintViolation<Menu>> violations = validator.validate(menu);
        assertEquals(violations.size(), 0);
    }

    @After
    public void tearDown() {
        validatorFactory.close();
    }
}