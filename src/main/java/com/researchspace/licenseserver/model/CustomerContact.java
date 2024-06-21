package com.researchspace.licenseserver.model;

import static org.apache.commons.lang.StringUtils.strip;

import com.fasterxml.jackson.annotation.JsonBackReference;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * Basic customer contact details
 *
 */
@Entity
public class CustomerContact implements Comparable<CustomerContact> {

  public static final int MAX_NAME_LENGTH = 50;

  public static final String AT_LEAST_ONE_CHAR = ".*(?=[A-Za-z]).+";

  public static final String LENGTH_ERROR_MSG = "Max length is " + MAX_NAME_LENGTH
      + " characters.";

  public static final String AT_LEAST_1_CHAR_IN_FIELD_MSG = "There must be at least one  letter character. ";

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((email == null) ? 0 : email.hashCode());
    result = prime * result + ((firstName == null) ? 0 : firstName.hashCode());
    result = prime * result + ((id == null) ? 0 : id.hashCode());
    result = prime * result + ((lastName == null) ? 0 : lastName.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
    CustomerContact other = (CustomerContact) obj;
    if (email == null) {
			if (other.email != null) {
				return false;
			}
    } else if (!email.equals(other.email)) {
			return false;
		}
    if (firstName == null) {
			if (other.firstName != null) {
				return false;
			}
    } else if (!firstName.equals(other.firstName)) {
			return false;
		}
    if (id == null) {
			if (other.id != null) {
				return false;
			}
    } else if (!id.equals(other.id)) {
			return false;
		}
    if (lastName == null) {
      return other.lastName == null;
    } else {
      return lastName.equals(other.lastName);
    }
  }

  private String firstName;

  private String lastName;

  private String email;

  private String telephone;

  private Long id;
  @JsonBackReference
  private CustomerInfo customerInfo;

  @ManyToOne
  @JsonBackReference
  public CustomerInfo getCustomerInfo() {
    return customerInfo;
  }

  public void setCustomerInfo(CustomerInfo customerInfo) {
    this.customerInfo = customerInfo;
  }

  public CustomerContact() {
    super();
  }

  /**
   * API constructor
   */
  public CustomerContact(String firstName, String lastName, String email, String telephone) {
    super();
    this.firstName = firstName;
    this.lastName = lastName;
    this.email = email;
    this.telephone = telephone;
  }

  @NotNull
  @NotEmpty(message = "first name cannot be empty")
  @Column(nullable = false, length = 50)
  @Length(max = MAX_NAME_LENGTH, message = "Max length is 50 characters")
  @Pattern(regexp = AT_LEAST_ONE_CHAR, message = AT_LEAST_1_CHAR_IN_FIELD_MSG)
  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  @NotNull
  @Length(max = MAX_NAME_LENGTH, message = LENGTH_ERROR_MSG)
  @Column(nullable = false, length = MAX_NAME_LENGTH)
  @NotEmpty(message = "Last name cannot be empty")
  @Pattern(regexp = AT_LEAST_ONE_CHAR, message = AT_LEAST_1_CHAR_IN_FIELD_MSG)
  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  @Email()
  @NotNull
  @NotEmpty(message = "email cannot be empty")
  @Column(nullable = false)
  @Length(max = MAX_NAME_LENGTH, message = LENGTH_ERROR_MSG)
  @Pattern(regexp = AT_LEAST_ONE_CHAR, message = AT_LEAST_1_CHAR_IN_FIELD_MSG)
  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  @NotNull
  @Column(nullable = false, length = 30)
  @NotEmpty(message = "Telephone cannot be empty")
  @Length(max = 30, message = "Max length is 30 characters")
  @Pattern(regexp = "^[ext\\d\\-\\s+\\(\\)]+$", message = "Phone number must be numbers, dashes and spaces, 'ext', '+', '(' or ')' only.")
  public String getTelephone() {
    return telephone;
  }

  public void setTelephone(String telephone) {
    this.telephone = strip(telephone);
  }

  public void setId(Long id) {
    this.id = id;
  }

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  public Long getId() {
    return id;
  }

  /**
   * Orders by last, first, email
   */
  public int compareTo(CustomerContact o) {
    int compare = lastName.compareTo(o.lastName);
    if (compare != 0) {
      return compare;
    }
    compare = firstName.compareTo(o.firstName);
    if (compare != 0) {
      return compare;
    }
    return email.compareTo(o.email);
  }

  @Override
  public String toString() {
    return "CustomerContact [firstName=" + firstName + ", lastName=" + lastName + ", email="
        + email + ", telephone=" + telephone + ", id=" + id + "]";
  }

}
