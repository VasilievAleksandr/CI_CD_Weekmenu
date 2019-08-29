package by.weekmenu.api.entity;

import org.apache.commons.lang3.StringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import java.time.LocalDateTime;
import java.util.Set;

import static org.junit.Assert.*;

public class RecycleBinTest {

    private static ValidatorFactory validatorFactory;
    private static Validator validator;

    @Before
    public void setUp() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    private RecycleBin createRecycleBin(String elementName, String entityName, LocalDateTime deleteDate) {
        RecycleBin recycleBin = new RecycleBin();
        recycleBin.setElementName(elementName);
        recycleBin.setEntityName(entityName);
        recycleBin.setDeleteDate(deleteDate);
        return recycleBin;
    }

    @Test
    public void testElementNameIsNull() {
        RecycleBin recycleBin = createRecycleBin(null, "Рецепт", LocalDateTime.now());
        Set<ConstraintViolation<RecycleBin>> violations = validator.validate(recycleBin);
        assertEquals(violations.size(), 1);
        assertEquals("Recycle Bin must have element name.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testElementNameIsBlank() {
        RecycleBin recycleBin = createRecycleBin("   ", "Рецепт", LocalDateTime.now());
        Set<ConstraintViolation<RecycleBin>> violations = validator.validate(recycleBin);
        assertEquals(violations.size(), 1);
        assertEquals("Recycle Bin must have element name.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testElementNameIsEmpty() {
        RecycleBin recycleBin = createRecycleBin("", "Рецепт", LocalDateTime.now());
        Set<ConstraintViolation<RecycleBin>> violations = validator.validate(recycleBin);
        assertEquals(violations.size(), 1);
        assertEquals("Recycle Bin must have element name.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testElementNameIsTooLong() {
        String name = StringUtils.repeat("Гречневая каша", "/", 60);
        RecycleBin recycleBin = createRecycleBin(name, "Рецепт", LocalDateTime.now());
        Set<ConstraintViolation<RecycleBin>> violations = validator.validate(recycleBin);
        assertEquals(violations.size(), 1);
        assertEquals("Element name '" + name + "' must be '255' characters long",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testEntityNameIsNull() {
        RecycleBin recycleBin = createRecycleBin("Гречневая каша", null, LocalDateTime.now());
        Set<ConstraintViolation<RecycleBin>> violations = validator.validate(recycleBin);
        assertEquals(violations.size(), 1);
        assertEquals("Recycle Bin must have entity name.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testEntityNameIsBlank() {
        RecycleBin recycleBin = createRecycleBin("Гречневая каша", "   ", LocalDateTime.now());
        Set<ConstraintViolation<RecycleBin>> violations = validator.validate(recycleBin);
        assertEquals(violations.size(), 1);
        assertEquals("Recycle Bin must have entity name.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testEntityNameIsEmpty() {
        RecycleBin recycleBin = createRecycleBin("Гречневая каша", "", LocalDateTime.now());
        Set<ConstraintViolation<RecycleBin>> violations = validator.validate(recycleBin);
        assertEquals(violations.size(), 1);
        assertEquals("Recycle Bin must have entity name.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testEntityNameIsTooLong() {
        String name = StringUtils.repeat("Рецепт", "/", 60);
        RecycleBin recycleBin = createRecycleBin("Гречневая каша", name, LocalDateTime.now());
        Set<ConstraintViolation<RecycleBin>> violations = validator.validate(recycleBin);
        assertEquals(violations.size(), 1);
        assertEquals("Entity name '" + name + "' must be '255' characters long",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testDeleteDateIsNull() {
        RecycleBin recycleBin = createRecycleBin("Гречневая каша", "Рецепт", null);
        Set<ConstraintViolation<RecycleBin>> violations = validator.validate(recycleBin);
        assertEquals(violations.size(), 1);
        assertEquals("Recycle Bin must have delete date.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testRecycleBinIsValid() {
        RecycleBin recycleBin = createRecycleBin("Гречневая каша", "Рецепт", LocalDateTime.now());
        Set<ConstraintViolation<RecycleBin>> violations = validator.validate(recycleBin);
        assertEquals(violations.size(), 0);
    }

    @After
    public void tearDown() {
        validatorFactory.close();
    }
}