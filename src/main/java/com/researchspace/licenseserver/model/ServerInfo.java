package com.researchspace.licenseserver.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;

/**
 * Basic info about the customer's server
 *
 */
@Entity
public class ServerInfo {

  private Long id;

  public void setId(Long id) {
    this.id = id;
  }

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  public Long getId() {
    return id;
  }

  private String uniqueId;
  private String url;
  private String macId;

  /**
   * GEts MAC address of the server. Can be null if this was not obtainable.
   */
  public String getMacId() {
    return macId;
  }

  public void setMacId(String macId) {
    this.macId = macId;
  }

  /**
   * A unique id for the server. This can be set by the RSpace instance on setup.
   */
  @Column(nullable = false, unique = true)
  public String getUniqueId() {
    return uniqueId;
  }

  public void setUniqueId(String uniqueId) {
    this.uniqueId = uniqueId;
  }


  public String getUrl() {
    return url;
  }

  @Override
  public String toString() {
    return "ServerInfo [uniqueId=" + uniqueId + ", url=" + url + "]";
  }

  /*
   * For hibernate
   */
  public ServerInfo() {

  }

  /**
   * API constructor
   *
   * @param uniqueId a unique id for the server.
   * @param url      The URL of the server install if known
   */
  public ServerInfo(String uniqueId, String url) {
    super();
    this.uniqueId = uniqueId;
    this.url = url;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((id == null) ? 0 : id.hashCode());
    result = prime * result + ((uniqueId == null) ? 0 : uniqueId.hashCode());
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
    ServerInfo other = (ServerInfo) obj;
    if (id == null) {
			if (other.id != null) {
				return false;
			}
    } else if (!id.equals(other.id)) {
			return false;
		}
		if (uniqueId == null) {
			return other.uniqueId == null;
		} else {
			return uniqueId.equals(other.uniqueId);
		}
  }

  public void setUrl(String url) {
    this.url = url;
  }

  @Lob()
  public String getServerInformation() {
    return serverInformation;
  }

  public void setServerInformation(String serverInformation) {
    this.serverInformation = serverInformation;
  }

  private String serverInformation;


}
