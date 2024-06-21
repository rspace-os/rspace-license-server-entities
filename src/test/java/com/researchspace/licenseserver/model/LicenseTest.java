package com.researchspace.licenseserver.model;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Calendar;
import java.util.Date;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class LicenseTest {

  private static Validator validator;

  @BeforeAll
  public static void before() {
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    validator = factory.getValidator();
  }

  License license;

  @Test
  public void testIsActive() {
    license = new License();
    Calendar cal = Calendar.getInstance();
    cal.add(Calendar.MONTH, -5);
    license.setActivationDate(cal.getTime());
    cal.add(Calendar.MONTH, 10);
    license.setExpiryDate(cal.getTime());
    license.setUniqueKey("any");
    Assertions.assertTrue(license.isActive());

    license.setRevoked(true);
    Assertions.assertFalse(license.isActive());

    license.setRevoked(false);
    Assertions.assertTrue(license.isActive());

    cal.add(Calendar.MONTH, -8);
    license.setExpiryDate(cal.getTime());
    Assertions.assertFalse(license.isActive());
    // with throw IAE as is before activation time
    cal.add(Calendar.MONTH, -12);
    assertThrows(IllegalArgumentException.class, () -> license.setExpiryDate(cal.getTime()));

  }

  @Test
  public void testSetup() {
    license = new License();
    Set<ConstraintViolation<License>> constraintViolations = validator.validate(license);
    Assertions.assertEquals(2, constraintViolations.size());
    license.setExpiryDate(new Date());
    license.setUniqueKey("any");
    Calendar cal = Calendar.getInstance();
    cal.add(Calendar.MONTH, -5);
    license.setActivationDate(cal.getTime());

    constraintViolations = validator.validate(license);
    Assertions.assertEquals(0, constraintViolations.size());

    license = new License(new Date(), new Date(), "some", -2);
    constraintViolations = validator.validate(license);
    Assertions.assertEquals(1, constraintViolations.size());
    Assertions.assertEquals("Total seats must be a positive number",
        constraintViolations.iterator().next().getMessage());
  }

  @Test
  public void testPermits() {
    License license = new License();
    license.setTotalFreeRSpaceadmin(5);
    license.setTotalFreeSysadmin(3);
    license.setTotalUserSeats(30);
    setUpDates(license);
    Assertions.assertEquals(3, license.permits("ROLE_ADMIN", 2));
    Assertions.assertEquals(1, license.permits("ROLE_SYSADMIN", 2));
    Assertions.assertEquals(20, license.permits("ROLE_USER", 10));

  }

  private void setUpDates(License license2) {
    Calendar cal = Calendar.getInstance();
    cal.add(Calendar.YEAR, -1);
    license2.setActivationDate(cal.getTime());
    cal.add(Calendar.YEAR, 2);
    license2.setExpiryDate(cal.getTime());
  }

}
