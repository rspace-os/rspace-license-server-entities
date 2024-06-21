package com.researchspace.licenseserver.model;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * Core license class. Invariants are :
 *
 * <ul>
 * <li> Expiry date must be after activation date
 * <li> Creation date is not null
 * <li> License key is not null.
 * </ul>
 *
 */
@Entity
public class License {

  public static final String APIKEY_HEADER = "rs-apikey";

  private Date creationDate;

  private Date activationDate;

  public static final String DATE_FORMAT = "dd-MM-yyyy";

  private boolean isRevoked = false;

  public static final int ADMIN_PROPORTION = 20;

  public static final int SEAT_COUNT_TO_ENFORCE_MAX_ADMINS = 50;

  /**
   * Returns <code>true</code> if license is revoked, <code>false</code> if license is valid.
   */
  public boolean isRevoked() {
    return isRevoked;
  }

  /**
   * Revokes this license if isRevoked == true. This will inactivate the license, regardless of seat
   * count or date. REstores licens
   *
   * @param revoked <code>true</code> to revoke, <code>false</code> to re-enable.
   */
  public void setRevoked(boolean revoked) {
    this.isRevoked = revoked;
  }

  @Transient
  @JsonGetter
  public String getExpiryDateFormatted() {
    SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
    return sdf.format(expiryDate);
  }

  @Transient
  @JsonGetter
  public String getActivationDateFormatted() {
    SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
    return sdf.format(activationDate);
  }


  private Date expiryDate;

  private String uniqueKey;

  private int totalUserSeats;

  private int totalFreeSysadmin = 1;

  private int totalFreeRSpaceadmin;

  private Long id;

  private CustomerInfo customerInfo;

  private String expiryDateFormatted;

  public static String getDateFormat() {
    return DATE_FORMAT;
  }

  public void setExpiryDateFormatted(String expiryDateFormatted) {
    this.expiryDateFormatted = expiryDateFormatted;
  }

  public void setActivationDateFormatted(String activationDateFormatted) {
    this.activationDateFormatted = activationDateFormatted;
  }


  private String activationDateFormatted;

  @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
  public CustomerInfo getCustomerInfo() {
    return customerInfo;
  }

  private ServerInfo serverInfo;

  @OneToOne(cascade = CascadeType.ALL)
  public ServerInfo getServerInfo() {
    return serverInfo;
  }

  public void setServerInfo(ServerInfo serverInfo) {
    this.serverInfo = serverInfo;
  }

  public void setCustomerInfo(CustomerInfo customerInfo) {
    this.customerInfo = customerInfo;
  }

  public License() {
    super();
    this.creationDate = new Date();
  }

  void assertNotNull(Object... args) {
    for (Object o : args) {
      if (o == null) {
        throw new IllegalArgumentException("null");
      }
    }
  }

  /**
   * API constructor. o arguments can be <code>null</code>.
   */
  public License(Date activationDate, Date expiryDate, String uniqueKey, int totalUserSeats) {
    this();
    assertNotNull(activationDate, expiryDate, uniqueKey);
    if (expiryDate.before(activationDate)) {
      throw new IllegalArgumentException("expiry date can't be before activation date");
    }
    this.activationDate = new Date(activationDate.getTime());
    this.expiryDate = new Date(expiryDate.getTime());
    this.uniqueKey = uniqueKey;
    this.totalUserSeats = totalUserSeats;
  }

  @Temporal(TemporalType.TIMESTAMP)
  @NotNull
  public Date getCreationDate() {
    return new Date(creationDate.getTime());
  }

  @Override
  public String toString() {
    return "License [creationDate=" + creationDate + ", activationDate=" + activationDate
        + ", expiryDate=" + expiryDate + ", uniqueKey=" + uniqueKey + ", totalUserSeats="
        + totalUserSeats + ", totalFreeSysadmin=" + totalFreeSysadmin
        + ", totalFreeRSpaceadmin=" + totalFreeRSpaceadmin + ", id=" + id + "]";
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((uniqueKey == null) ? 0 : uniqueKey.hashCode());
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
    License other = (License) obj;
		if (uniqueKey == null) {
			return other.uniqueKey == null;
		} else {
			return uniqueKey.equals(other.uniqueKey);
		}
  }

  /*
   * For hibernate only
   */
  void setCreationDate(Date creationDate) {
    this.creationDate = creationDate;
  }

  @Temporal(TemporalType.TIMESTAMP)
  @NotNull(message = " Activation date required")
  public Date getActivationDate() {
    return activationDate;
  }

  public void setActivationDate(Date activationDate) {
    this.activationDate = activationDate;
  }

  @Temporal(TemporalType.TIMESTAMP)
  @NotNull(message = "Expiry date required")
  public Date getExpiryDate() {
    return expiryDate;
  }

  public void setExpiryDate(Date expiryDate) {
    if (activationDate != null && expiryDate.before(activationDate)) {
      throw new IllegalArgumentException("Expiry can't be before activation date!");
    }
    this.expiryDate = expiryDate;
  }

  @Column(nullable = false)
  public String getUniqueKey() {
    return uniqueKey;
  }

  public void setUniqueKey(String uniqueKey) {
    this.uniqueKey = uniqueKey;
  }

  @Min(value = 0, message = "Total seats must be a positive number")
  public int getTotalUserSeats() {
    return totalUserSeats;
  }

  public void setTotalUserSeats(int totalUserSeats) {
    if (totalUserSeats < 0) {
      throw new IllegalArgumentException("Seat number cannot be negative");
    }
    this.totalUserSeats = totalUserSeats;
  }

  @Min(value = 1, message = "There must always be at least one free sysadmin seat.")
  public int getTotalFreeSysadmin() {
    return totalFreeSysadmin;
  }

  public void setTotalFreeSysadmin(int totalFreeSysadmin) {
    this.totalFreeSysadmin = totalFreeSysadmin;
  }

  @Min(value = 0, message = "Free Admin seat count must be 0 or more.")
  public int getTotalFreeRSpaceadmin() {
    return totalFreeRSpaceadmin;
  }

  public void setTotalFreeRSpaceadmin(int totalFreeRSpaceadmin) {
    this.totalFreeRSpaceadmin = totalFreeRSpaceadmin;
  }

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  public Long getId() {
    return id;
  }

  /**
   * License is active if current time is after activation but before expiry and license is not
   * revoked.
   */
  @Transient
  @JsonIgnore
  public boolean isActive() {
    Date now = new Date();
    return now.after(activationDate) && now.before(expiryDate) && !isRevoked;
  }

  public void setId(Long id) {
    this.id = id;
  }

  private int usedSeatCount;


  /**
   * An estimate of how many  enabled users are on the system. This may not be accurate as it only
   * gets updated when an RSpace instance  communicates back to this server.
   */
  @Min(0)
  public int getUsedSeatCount() {
    return usedSeatCount;
  }

  /**
   * Sets the used seat count, should be an integer > 0,
   */
  public void setUsedSeatCount(int usedSeatCount) {
    this.usedSeatCount = usedSeatCount;
  }


  /**
   * Calculates the number of accounts who can be created.
   *
   * @param roleName               The rolename. These should agree with specified role names as
   *                               defined in RSpace.
   * @param currEnabledUsersInRole the current number of enabled users in the specified role.
   * @return The number of licenses that can be created. 0 or a  negative number  implies no
   * licenses can be created, either because seat count is full or license has expired or been
   * revoked.
   */
  public int permits(String roleName, int currEnabledUsersInRole) {
    if (!isActive()) {
      return -1;
    }
    switch (roleName) {
      case "ROLE_ADMIN":
        return totalFreeRSpaceadmin - currEnabledUsersInRole;

      case "ROLE_SYSADMIN":
        return totalFreeSysadmin - currEnabledUsersInRole;
      default:
        return totalUserSeats - currEnabledUsersInRole;
    }
  }

}
