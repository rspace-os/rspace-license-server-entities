package com.researchspace.licenseserver.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import java.util.Set;
import java.util.TreeSet;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * Entity for holding customer information.
 *
 */
@Entity
public class CustomerInfo {

  private Long id;

  private String organisationName;
  @JsonManagedReference
  private Set<CustomerContact> contacts = new TreeSet<>();

  public CustomerInfo(String name) {
    if (name == null) {
      throw new IllegalArgumentException("Name must not be null");
    }
    this.organisationName = name;
  }

  @Override
  public String toString() {
    return "CustomerInfo [id=" + id + ", organisationName=" + organisationName + "]";
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((organisationName == null) ? 0 : organisationName.hashCode());
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
    CustomerInfo other = (CustomerInfo) obj;
		if (organisationName == null) {
			return other.organisationName == null;
		} else {
			return organisationName.equals(other.organisationName);
		}
  }

  @NotNull
  @Column(nullable = false)
  @NotEmpty(message = "Organisation name cannot be empty.")
  @Length(max = CustomerContact.MAX_NAME_LENGTH, message = CustomerContact.LENGTH_ERROR_MSG)
  public String getOrganisationName() {
    return organisationName;
  }

  public CustomerInfo() {
    super();
  }

  public void setOrganisationName(String organisationName) {
    this.organisationName = organisationName;
  }

  @OneToMany(fetch = FetchType.EAGER, mappedBy = "customerInfo", cascade = CascadeType.ALL, orphanRemoval = true)
  @JsonManagedReference
  public Set<CustomerContact> getContacts() {
    return contacts;
  }

  void setContacts(Set<CustomerContact> contacts) {
    this.contacts = contacts;
  }

  public boolean addContact(CustomerContact contact) {
    contact.setCustomerInfo(this);
    return this.contacts.add(contact);
  }

  public boolean removeContact(CustomerContact contact) {
    contact.setCustomerInfo(null);
    return this.contacts.remove(contact);
  }

  public void setId(Long id) {
    this.id = id;
  }

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  public Long getId() {
    return id;
  }

}
