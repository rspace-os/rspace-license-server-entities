package com.researchspace.licenseserver.model;

import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import org.apache.commons.lang.RandomStringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class CustomerContactTest {

  private static Validator validator;

  @BeforeAll
  public static void before() {
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    validator = factory.getValidator();
  }

  @Test
  @DisplayName("test j.validator annotations for phone numbers")
  public void testValidateTelNumbers() {
    CustomerContact cc = createValidContact();
    cc.setTelephone("+1 (123) 456-7890 x111");
    assertNViolations(cc, 0);
    cc.setTelephone("+1 (123) 456-7890 ext111");
    assertNViolations(cc, 0);
    cc.setTelephone("+1zzz (123) 456-7890 ext111");
    cc.setTelephone("+44 (131) 456-7890 ext111");
    assertNViolations(cc, 0);
    cc.setTelephone("    +44 (131) 456-7890 ext111   ");
    Assertions.assertEquals("+44 (131) 456-7890 ext111", cc.getTelephone());
  }

  @Test
  @DisplayName("test j.validator annotations for customer contacts")
  public void testValidation() {

    CustomerContact cc = createValidContact();
    assertNViolations(cc, 0);
    String TOO_LONG = RandomStringUtils.randomAlphabetic(CustomerContact.MAX_NAME_LENGTH + 1);
    cc.setFirstName(TOO_LONG);
    assertNViolations(cc, 1);
    cc.setLastName(TOO_LONG);
    assertNViolations(cc, 2);
    cc.setEmail("a@" + TOO_LONG);
    assertNViolations(cc, 3);
    String TOO_LONG_PHONE = RandomStringUtils.randomNumeric(31);
    cc.setTelephone(TOO_LONG_PHONE);
    assertNViolations(cc, 4);
    // no numbers AND too long
    cc.setTelephone(TOO_LONG);
    assertNViolations(cc, 5);

    //reset
    cc = createValidContact();
    // no @ character
    cc.setEmail("qwee");
    assertNViolations(cc, 1);

    // now check that letters etc. are validated:
    cc = createValidContact();
    cc.setFirstName("1234");
    assertNViolations(cc, 1);
    cc.setLastName("234-&5");
    assertNViolations(cc, 2);
    cc.setEmail("123@4456");
    assertNViolations(cc, 3);
  }

  private CustomerContact createValidContact() {
    CustomerContact cc = new CustomerContact();
    cc.setFirstName("any");
    cc.setLastName("srurname");
    cc.setEmail("a@b.com");
    cc.setTelephone("0123 456-789");
    return cc;
  }

  private void assertNViolations(CustomerContact cc, int expectedViolationCount) {
    Set<ConstraintViolation<CustomerContact>> constraintViolations = validator.validate(cc);
    Assertions.assertEquals(expectedViolationCount, constraintViolations.size());

  }

}
