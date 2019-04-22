package by.weekmenu.api.entity;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import java.util.Set;

import static org.junit.Assert.*;

public class BankTest {

    private static ValidatorFactory validatorFactory;
    private static Validator validator;

    @Before
    public void setUp() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Test
    public void testNameIsNull() {
        Bank bank = new Bank(null, new Currency());
        Set<ConstraintViolation<Bank>> violations = validator.validate(bank);
        assertEquals(violations.size(), 1);
        assertEquals("Bank must have name.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testNameIsBlank() {
        Bank bank = new Bank("   ", new Currency());
        Set<ConstraintViolation<Bank>> violations = validator.validate(bank);
        assertEquals(violations.size(), 1);
        assertEquals("Bank must have name.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testNameIsEmpty() {
        Bank bank = new Bank("", new Currency());
        Set<ConstraintViolation<Bank>> violations = validator.validate(bank);
        assertEquals(violations.size(), 1);
        assertEquals("Bank must have name.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testBaseCurrencyIsNull() {
        Bank bank = new Bank("НБ РБ", null);
        Set<ConstraintViolation<Bank>> violations = validator.validate(bank);
        assertEquals(violations.size(), 1);
        assertEquals("Bank must have base currency",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testBankIsValid() {
        Bank bank = new Bank("НБ РБ", new Currency("руб.", "BYN", true));
        Set<ConstraintViolation<Bank>> violations = validator.validate(bank);
        assertEquals(violations.size(), 0);
    }

    @After
    public void tearDown() {
        validatorFactory.close();
    }
}