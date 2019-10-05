//package by.weekmenu.api.entity;
//
//import org.junit.After;
//import org.junit.Before;
//import org.junit.Test;
//
//import javax.validation.ConstraintViolation;
//import javax.validation.Validation;
//import javax.validation.Validator;
//import javax.validation.ValidatorFactory;
//
//import java.util.Set;
//
//import static org.junit.Assert.*;
//
//public class MenuRecipeTest {
//
//    private static ValidatorFactory validatorFactory;
//    private static Validator validator;
//
//    @Before
//    public void setUp() {
//        validatorFactory = Validation.buildDefaultValidatorFactory();
//        validator = validatorFactory.getValidator();
//    }
//
//    private Menu getValidMenu() {
//        return new Menu("Бюджетное", true, new Ownership(OwnershipName.USER));
//    }
//
//    private Recipe getValidRecipe() {
//        return new Recipe("Курица с ананасами", true, new CookingMethod("Тушение"), new Ownership(OwnershipName.USER));
//    }
//
//    private MealType getValidMealType() {
//        return new MealType("Обед", true);
//    }
//
//    private DayOfWeek getValidDayOfWeek() {
//        return new DayOfWeek("Понедельник", "ПН");
//    }
//
//    @Test
//    public void testMenuIsInvalid() {
//        MenuRecipe menuRecipe = new MenuRecipe(new Menu("Бюджетное", null, new Ownership(OwnershipName.USER)),
//                getValidRecipe(), getValidMealType(), getValidDayOfWeek());
//        Set<ConstraintViolation<MenuRecipe>> violations = validator.validate(menuRecipe);
//        assertEquals(violations.size(), 1);
//        assertEquals("Menu must have field 'isActive' defined.",
//                violations.iterator().next().getMessage());
//    }
//
//    @Test
//    public void testMenuIsNull() {
//        MenuRecipe menuRecipe = new MenuRecipe(null,
//                getValidRecipe(), getValidMealType(), getValidDayOfWeek());
//        Set<ConstraintViolation<MenuRecipe>> violations = validator.validate(menuRecipe);
//        assertEquals(violations.size(), 1);
//        assertEquals("MenuRecipe must have menu.",
//                violations.iterator().next().getMessage());
//    }
//
//    @Test
//    public void testRecipeIsInvalid() {
//        MenuRecipe menuRecipe = new MenuRecipe(getValidMenu(),
//                new Recipe("Курица с ананасами", true, null, new Ownership(OwnershipName.USER)),
//                getValidMealType(), getValidDayOfWeek());
//        Set<ConstraintViolation<MenuRecipe>> violations = validator.validate(menuRecipe);
//        assertEquals(violations.size(), 1);
//        assertEquals("Recipe's cookingMethod mustn't be null.",
//                violations.iterator().next().getMessage());
//    }
//
//    @Test
//    public void testRecipeIsNull() {
//        MenuRecipe menuRecipe = new MenuRecipe(getValidMenu(), null, getValidMealType(), getValidDayOfWeek());
//        Set<ConstraintViolation<MenuRecipe>> violations = validator.validate(menuRecipe);
//        assertEquals(violations.size(), 1);
//        assertEquals("MenuRecipe must have recipe.",
//                violations.iterator().next().getMessage());
//    }
//
//    @Test
//    public void testMealTypeIsInvalid() {
//        MenuRecipe menuRecipe = new MenuRecipe(getValidMenu(), getValidRecipe(),
//                new MealType(null, true),
//                getValidDayOfWeek());
//        Set<ConstraintViolation<MenuRecipe>> violations = validator.validate(menuRecipe);
//        assertEquals(violations.size(), 1);
//        assertEquals("MealType must have name.",
//                violations.iterator().next().getMessage());
//    }
//
//    @Test
//    public void testDishTypeIsNull() {
//        MenuRecipe menuRecipe = new MenuRecipe(getValidMenu(), getValidRecipe(), null, getValidDayOfWeek());
//        Set<ConstraintViolation<MenuRecipe>> violations = validator.validate(menuRecipe);
//        assertEquals(violations.size(), 1);
//        assertEquals("MenuRecipe must have mealType.",
//                violations.iterator().next().getMessage());
//    }
//
//    @Test
//    public void testDayOfWeekIsInvalid() {
//        MenuRecipe menuRecipe = new MenuRecipe(getValidMenu(), getValidRecipe(), getValidMealType(),
//                new DayOfWeek("Понедельник", null));
//        Set<ConstraintViolation<MenuRecipe>> violations = validator.validate(menuRecipe);
//        assertEquals(violations.size(), 1);
//        assertEquals("DayOfWeek must have shortName.",
//                violations.iterator().next().getMessage());
//    }
//
//    @Test
//    public void testDayOfWeekIsNull() {
//        MenuRecipe menuRecipe = new MenuRecipe(getValidMenu(), getValidRecipe(), getValidMealType(), null);
//        Set<ConstraintViolation<MenuRecipe>> violations = validator.validate(menuRecipe);
//        assertEquals(violations.size(), 1);
//        assertEquals("MenuRecipe must have dayOfWeek.",
//                violations.iterator().next().getMessage());
//    }
//
//    @Test
//    public void testIsValid() {
//        MenuRecipe menuRecipe = new MenuRecipe(getValidMenu(), getValidRecipe(), getValidMealType(), getValidDayOfWeek());
//        Set<ConstraintViolation<MenuRecipe>> violations = validator.validate(menuRecipe);
//        assertEquals(violations.size(), 0);
//    }
//
//    @After
//    public void tearDown() {
//        validatorFactory.close();
//    }
//
//}