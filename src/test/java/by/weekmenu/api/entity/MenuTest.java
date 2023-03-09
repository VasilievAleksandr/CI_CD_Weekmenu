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

import static org.junit.Assert.assertEquals;

public class MenuTest {

    private static ValidatorFactory validatorFactory;
    private static Validator validator;

    @Before
    public void setUp() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    private Recipe getValidRecipe() {
        return new Recipe("Курица с ананасами", true, new CookingMethod("Тушение"), new Ownership(OwnershipName.USER));
    }

    private MealType getValidMealType() {
        return new MealType("Обед", 20);
    }

    private java.time.DayOfWeek getValidDayOfWeek() {
        return java.time.DayOfWeek.MONDAY;
    }

    private Region getRegion() {
        Country country = new Country("Беларусь", "BY", getCurrency());
        return new Region("Минский район", country);
    }

    private Currency getCurrency() {
        return new Currency("руб.", "BYN", false);
    }

    @Test
    public void testNameIsNull() {
        Menu menu = new Menu(null, true, new Ownership(OwnershipName.USER));
        Set<ConstraintViolation<Menu>> violations = validator.validate(menu);
        assertEquals(violations.size(), 1);
        assertEquals("Menu must have name.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testNameIsBlank() {
        Menu menu = new Menu("   ", true, new Ownership(OwnershipName.USER));
        Set<ConstraintViolation<Menu>> violations = validator.validate(menu);
        assertEquals(violations.size(), 1);
        assertEquals("Menu must have name.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testNameIsEmpty() {
        Menu menu = new Menu("", true, new Ownership(OwnershipName.USER));
        Set<ConstraintViolation<Menu>> violations = validator.validate(menu);
        assertEquals(violations.size(), 1);
        assertEquals("Menu must have name.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testNameIsTooLong() {
        Menu menu = new Menu("", true, new Ownership(OwnershipName.USER));
        String name = StringUtils.repeat("очень_длинное_название_меню", 20);
        menu.setName(name);
        Set<ConstraintViolation<Menu>> violations = validator.validate(menu);
        assertEquals(violations.size(), 1);
        assertEquals("Menu's name '" + name + "' must be '255' characters long",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testCaloriesAreNegative() {
        Menu menu = new Menu("Бюджетное", true, new Ownership(OwnershipName.USER));
        menu.setCalories(new BigDecimal("-100"));
        Set<ConstraintViolation<Menu>> violations = validator.validate(menu);
        assertEquals(violations.size(), 1);
        assertEquals("Menu's calories '-100' must be positive or '0'.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testCaloriesHasTooManyFractionDigits() {
        Menu menu = new Menu("Бюджетное", true, new Ownership(OwnershipName.USER));
        menu.setCalories(new BigDecimal("123.12"));
        Set<ConstraintViolation<Menu>> violations = validator.validate(menu);
        assertEquals(violations.size(), 1);
        assertEquals("Calories '123.12' must have up to '7' integer digits and '1' fraction digits.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testCaloriesValueIsTooHigh() {
        Menu menu = new Menu("Бюджетное", true, new Ownership(OwnershipName.USER));
        menu.setCalories(new BigDecimal("12345678.1"));
        Set<ConstraintViolation<Menu>> violations = validator.validate(menu);
        assertEquals(violations.size(), 1);
        assertEquals("Calories '12345678.1' must have up to '7' integer digits and '1' fraction digits.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testProteinsAreNegative() {
        Menu menu = new Menu("Бюджетное", true, new Ownership(OwnershipName.USER));
        menu.setProteins(new BigDecimal(-150));
        Set<ConstraintViolation<Menu>> violations = validator.validate(menu);
        assertEquals(violations.size(), 1);
        assertEquals("Menu's proteins '-150' must be positive or '0'.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testProteinsHasTooManyFractionDigits() {
        Menu menu = new Menu("Бюджетное", true, new Ownership(OwnershipName.USER));
        menu.setProteins(new BigDecimal("123.12"));
        Set<ConstraintViolation<Menu>> violations = validator.validate(menu);
        assertEquals(violations.size(), 1);
        assertEquals("Proteins '123.12' must have up to '7' integer digits and '1' fraction digits.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testProteinsValueIsTooHigh() {
        Menu menu = new Menu("Бюджетное", true, new Ownership(OwnershipName.USER));
        menu.setProteins(new BigDecimal("12345678.1"));
        Set<ConstraintViolation<Menu>> violations = validator.validate(menu);
        assertEquals(violations.size(), 1);
        assertEquals("Proteins '12345678.1' must have up to '7' integer digits and '1' fraction digits.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testFatsAreNegative() {
        Menu menu = new Menu("Бюджетное", true, new Ownership(OwnershipName.USER));
        menu.setFats(new BigDecimal(-150));
        Set<ConstraintViolation<Menu>> violations = validator.validate(menu);
        assertEquals(violations.size(), 1);
        assertEquals("Menu's fats '-150' must be positive or '0'.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testFatsHasTooManyFractionDigits() {
        Menu menu = new Menu("Бюджетное", true, new Ownership(OwnershipName.USER));
        menu.setFats(new BigDecimal("123.12"));
        Set<ConstraintViolation<Menu>> violations = validator.validate(menu);
        assertEquals(violations.size(), 1);
        assertEquals("Fats '123.12' must have up to '7' integer digits and '1' fraction digits.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testFatsValueIsTooHigh() {
        Menu menu = new Menu("Бюджетное", true, new Ownership(OwnershipName.USER));
        menu.setFats(new BigDecimal("12345678.12"));
        Set<ConstraintViolation<Menu>> violations = validator.validate(menu);
        assertEquals(violations.size(), 1);
        assertEquals("Fats '12345678.12' must have up to '7' integer digits and '1' fraction digits.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testCarbsAreNegative() {
        Menu menu = new Menu("Бюджетное", true, new Ownership(OwnershipName.USER));
        menu.setCarbs(new BigDecimal(-150));
        Set<ConstraintViolation<Menu>> violations = validator.validate(menu);
        assertEquals(violations.size(), 1);
        assertEquals("Menu's carbs '-150' must be positive or '0'.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testCarbsHasTooManyFractionDigits() {
        Menu menu = new Menu("Бюджетное", true, new Ownership(OwnershipName.USER));
        menu.setCarbs(new BigDecimal("123.12"));
        Set<ConstraintViolation<Menu>> violations = validator.validate(menu);
        assertEquals(violations.size(), 1);
        assertEquals("Carbs '123.12' must have up to '7' integer digits and '1' fraction digits.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testCarbsValueIsTooHigh() {
        Menu menu = new Menu("Бюджетное", true, new Ownership(OwnershipName.USER));
        menu.setCarbs(new BigDecimal("12345678.12"));
        Set<ConstraintViolation<Menu>> violations = validator.validate(menu);
        assertEquals(violations.size(), 1);
        assertEquals("Carbs '12345678.12' must have up to '7' integer digits and '1' fraction digits.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testIsActiveIsNull() {
        Menu menu = new Menu("Бюджетное", null, new Ownership(OwnershipName.USER));
        Set<ConstraintViolation<Menu>> violations = validator.validate(menu);
        assertEquals(violations.size(), 1);
        assertEquals("Menu must have field 'isActive' defined.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testAuthorNameIsTooLong() {
        Menu menu = new Menu("Меню", true, new Ownership(OwnershipName.USER));
        String name = StringUtils.repeat("очень_длинное_имя_автора", 20);
        menu.setAuthorName(name);
        Set<ConstraintViolation<Menu>> violations = validator.validate(menu);
        assertEquals(violations.size(), 1);
        assertEquals("Author name's length of the menu '" + name + "' mustn't be more than '255' characters long.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testAuthorImageLinkIsTooLong() {
        Menu menu = new Menu("Меню", true, new Ownership(OwnershipName.USER));
        String link = StringUtils.repeat("очень_длинная_ссылка", 20);
        menu.setAuthorImageLink(link);
        Set<ConstraintViolation<Menu>> violations = validator.validate(menu);
        assertEquals(violations.size(), 1);
        assertEquals("Author image link's length of the menu '" + link + "' mustn't be more than '255' characters long.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testMenuDescriptionIsTooLong() {
        Menu menu = new Menu("Меню", true, new Ownership(OwnershipName.USER));
        String description = StringUtils.repeat("очень_длинная_описание_меню", 100);
        menu.setMenuDescription(description);
        Set<ConstraintViolation<Menu>> violations = validator.validate(menu);
        assertEquals(violations.size(), 1);
        assertEquals("Menu description's length '" + description + "' mustn't be more than '1000' characters long.",
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
    public void testHasInvalidMenuCategory() {
        Menu menu = new Menu("Бюджетное", true, new Ownership(OwnershipName.USER));
        menu.setMenuCategory(new MenuCategory("", true));
        Set<ConstraintViolation<Menu>> violations = validator.validate(menu);
        assertEquals(violations.size(), 1);
        assertEquals("MenuCategory must have name.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testMenuIsValid() {
        Menu menu = new Menu("Бюджетное", true, new Ownership(OwnershipName.USER));
        menu.setMenuCategory(new MenuCategory("Вегетарианское", true));
        menu.setCarbs(new BigDecimal(100));
        menu.setFats(new BigDecimal(100));
        menu.setProteins(new BigDecimal(150));
        menu.setCalories(new BigDecimal(400));
        menu.setAuthorName("Повар");
        menu.setAuthorImageLink("https://bestrecipes.by");
        menu.setMenuDescription("Очень вкусное меню");
        Set<ConstraintViolation<Menu>> violations = validator.validate(menu);
        assertEquals(violations.size(), 0);
    }

    @After
    public void tearDown() {
        validatorFactory.close();
    }
}