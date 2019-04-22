package by.weekmenu.api.entity;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.util.Set;

import static org.junit.Assert.*;

public class ExchangeRateTest {

    private static ValidatorFactory validatorFactory;
    private static Validator validator;

    @Before
    public void setUp() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Test
    public void testDateIsNull() {
        ExchangeRate exchangeRate = new ExchangeRate(null, new BigDecimal("1.12"),
                new Bank("НБ РБ", new Currency("руб.", "BYN", true)),
                new Currency("руб.", "BYN", true));
        Set<ConstraintViolation<ExchangeRate>> violations = validator.validate(exchangeRate);
        assertEquals(violations.size(), 1);
        assertEquals("ExchangeRate must have date.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testRateIsNull() {
        ExchangeRate exchangeRate = new ExchangeRate(LocalDate.of(2019, Month.APRIL, 22), null,
                new Bank("НБ РБ", new Currency("руб.", "BYN", true)),
                new Currency("руб.", "BYN", true));
        Set<ConstraintViolation<ExchangeRate>> violations = validator.validate(exchangeRate);
        assertEquals(violations.size(), 1);
        assertEquals("ExchangeRate must have rate.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testRateHasTooManyIntegerDigits() {
        ExchangeRate exchangeRate = new ExchangeRate(LocalDate.of(2019, Month.APRIL, 22), new BigDecimal("12345678.12"),
                new Bank("НБ РБ", new Currency("руб.", "BYN", true)),
                new Currency("руб.", "BYN", true));
        Set<ConstraintViolation<ExchangeRate>> violations = validator.validate(exchangeRate);
        assertEquals(violations.size(), 1);
        assertEquals("ExchangeRate's rate '12345678.12' must have up to '7' integer digits and '2' fraction digits.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testRateHasTooManyFractionDigits() {
        ExchangeRate exchangeRate = new ExchangeRate(LocalDate.of(2019, Month.APRIL, 22), new BigDecimal("12.123"),
                new Bank("НБ РБ", new Currency("руб.", "BYN", true)),
                new Currency("руб.", "BYN", true));
        Set<ConstraintViolation<ExchangeRate>> violations = validator.validate(exchangeRate);
        assertEquals(violations.size(), 1);
        assertEquals("ExchangeRate's rate '12.123' must have up to '7' integer digits and '2' fraction digits.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testRateIsNegative() {
        ExchangeRate exchangeRate = new ExchangeRate(LocalDate.of(2019, Month.APRIL, 22), new BigDecimal("-12.12"),
                new Bank("НБ РБ", new Currency("руб.", "BYN", true)),
                new Currency("руб.", "BYN", true));
        Set<ConstraintViolation<ExchangeRate>> violations = validator.validate(exchangeRate);
        assertEquals(violations.size(), 1);
        assertEquals("ExchangeRate's rate '-12.12' must be positive.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testRateIsZero() {
        ExchangeRate exchangeRate = new ExchangeRate(LocalDate.of(2019, Month.APRIL, 22), new BigDecimal("0"),
                new Bank("НБ РБ", new Currency("руб.", "BYN", true)),
                new Currency("руб.", "BYN", true));
        Set<ConstraintViolation<ExchangeRate>> violations = validator.validate(exchangeRate);
        assertEquals(violations.size(), 1);
        assertEquals("ExchangeRate's rate '0' must be positive.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testBankIsNull() {
        ExchangeRate exchangeRate = new ExchangeRate(LocalDate.of(2019, Month.APRIL, 22),
                new BigDecimal("12.12"), null, new Currency("руб.", "BYN", true));
        Set<ConstraintViolation<ExchangeRate>> violations = validator.validate(exchangeRate);
        assertEquals(violations.size(), 1);
        assertEquals("ExchangeRate must have bank.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testBankIsInvalid() {
        ExchangeRate exchangeRate = new ExchangeRate(LocalDate.of(2019, Month.APRIL, 22),
                new BigDecimal("12.12"), new Bank(null, new Currency("руб.", "BYN", true)),
                new Currency("дол.", "USD", true));
        Set<ConstraintViolation<ExchangeRate>> violations = validator.validate(exchangeRate);
        assertEquals(violations.size(), 1);
        assertEquals("Bank must have name.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testCurrencyIsNull() {
        ExchangeRate exchangeRate = new ExchangeRate(LocalDate.of(2019, Month.APRIL, 22),
                new BigDecimal("12.12"),
                new Bank("НБ РБ", new Currency("руб.", "BYN", true)),
                null);
        Set<ConstraintViolation<ExchangeRate>> violations = validator.validate(exchangeRate);
        assertEquals(violations.size(), 1);
        assertEquals("ExchangeRate must have currency.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testCurrencyIsInvalid() {
        ExchangeRate exchangeRate = new ExchangeRate(LocalDate.of(2019, Month.APRIL, 22),
                new BigDecimal("12.12"),
                new Bank("НБ РБ", new Currency("руб.", "BYN", true)),
                new Currency("   ", "BYN", true));
        Set<ConstraintViolation<ExchangeRate>> violations = validator.validate(exchangeRate);
        assertEquals(violations.size(), 1);
        assertEquals("Currency must have name.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testIsValid() {
        ExchangeRate exchangeRate = new ExchangeRate(LocalDate.of(2019, Month.APRIL, 22),
                new BigDecimal("12.12"),
                new Bank("НБ РБ", new Currency("руб.", "BYN", true)),
                new Currency("руб.", "BYN", true));
        Set<ConstraintViolation<ExchangeRate>> violations = validator.validate(exchangeRate);
        assertEquals(violations.size(), 0);
    }

    @After
    public void tearDown() {
        validatorFactory.close();
    }
}