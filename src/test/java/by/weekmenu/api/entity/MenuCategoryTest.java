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

import static org.junit.Assert.*;

public class MenuCategoryTest {

    private static ValidatorFactory validatorFactory;
    private static Validator validator;

    @Before
    public void setUp() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Test
    public void testMenuCategoryNameIsNull() {
        MenuCategory menuCategory = new MenuCategory(null, true);
        Set<ConstraintViolation<MenuCategory>> violations = validator.validate(menuCategory);
        assertEquals(violations.size(), 1);
        assertEquals("MenuCategory must have name.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testMenuCategoryNameIsBlank() {
        MenuCategory menuCategory = new MenuCategory("   ", true);
        Set<ConstraintViolation<MenuCategory>> violations = validator.validate(menuCategory);
        assertEquals(violations.size(), 1);
        assertEquals("MenuCategory must have name.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testMenuCategoryNameIsEmpty() {
        MenuCategory menuCategory = new MenuCategory("", true);
        Set<ConstraintViolation<MenuCategory>> violations = validator.validate(menuCategory);
        assertEquals(violations.size(), 1);
        assertEquals("MenuCategory must have name.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testNameIsTooLong() {
        MenuCategory menuCategory = new MenuCategory("", true);
        String name = StringUtils.repeat("очень_длинное_название_категории_меню", 20);
        menuCategory.setName(name);
        Set<ConstraintViolation<MenuCategory>> violations = validator.validate(menuCategory);
        assertEquals(violations.size(), 1);
        assertEquals("MenuCategory's name '" + name +"' must be '255' characters long",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testMenuCategoryPriorityIsNegative() {
        MenuCategory menuCategory = new MenuCategory("Бюджетное меню 1", true);
        menuCategory.setPriority(-100);
        Set<ConstraintViolation<MenuCategory>> violations = validator.validate(menuCategory);
        assertEquals(violations.size(), 1);
        assertEquals("MenuCategory's priority '-100' must be positive.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testMenuCategoryPriorityIsZero() {
        MenuCategory menuCategory = new MenuCategory("Бюджетное меню 1", true);
        menuCategory.setPriority(0);
        Set<ConstraintViolation<MenuCategory>> violations = validator.validate(menuCategory);
        assertEquals(violations.size(), 1);
        assertEquals("MenuCategory's priority '0' must be positive.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testMenuCategoryImageLinkIsTooLong() {
        MenuCategory menuCategory = new MenuCategory("Бюджетное меню 1", true);
        String imageLink = StringUtils.repeat("path_to_the_image", "/", 20);
        menuCategory.setImageLink(imageLink);
        Set<ConstraintViolation<MenuCategory>> violations = validator.validate(menuCategory);
        assertEquals(violations.size(), 1);
        assertEquals("ImageLink's length of the menuCategory '" + imageLink + "' mustn't be more than '255' characters long.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testMenuCategoryIsActiveIsNull() {
        MenuCategory menuCategory = new MenuCategory("Бюджетное меню 1", null);
        Set<ConstraintViolation<MenuCategory>> violations =validator.validate(menuCategory);
        assertEquals(violations.size(), 1);
        assertEquals("MenuCategory must have field 'isActive' defined.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testMenuCategoryIsValid() {
        MenuCategory menuCategory = new MenuCategory("Бюджетное меню 1", false);
        menuCategory.setImageLink("/images/dinner.jpg");
        menuCategory.setPriority(100);
        Set<ConstraintViolation<MenuCategory>> violations = validator.validate(menuCategory);
        assertEquals(violations.size(), 0);
    }

    @After
    public void tearDown() {
        validatorFactory.close();
    }
}