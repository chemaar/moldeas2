package org.weso.moldeas.transformer.organizations;

public class OrganizationTO {
	
	private String id;
	private String idContract;
	private String nutsCode;
	private String type;
	private String label;
	private String fullAddress;
	private String street;
	private String postalCode;
	private String region;
	private String email;
	private String telephone;
	private String fax;
	private String homepage;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getIdContract() {
		return idContract;
	}
	public void setIdContract(String idContract) {
		this.idContract = idContract;
	}
	public String getNutsCode() {
		return nutsCode;
	}
	public void setNutsCode(String nutsCode) {
		this.nutsCode = nutsCode;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public String getFullAddress() {
		return fullAddress;
	}
	public void setFullAddress(String fullAddress) {
		this.fullAddress = fullAddress;
	}
	public String getStreet() {
		return street;
	}
	public void setStreet(String street) {
		this.street = street;
	}
	public String getPostalCode() {
		return postalCode;
	}
	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}
	public String getRegion() {
		return region;
	}
	public void setRegion(String region) {
		this.region = region;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getTelephone() {
		return telephone;
	}
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
	public String getFax() {
		return fax;
	}
	public void setFax(String fax) {
		this.fax = fax;
	}
	public String getHomepage() {
		return homepage;
	}
	public void setHomepage(String homepage) {
		this.homepage = homepage;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + ((fax == null) ? 0 : fax.hashCode());
		result = prime * result
				+ ((fullAddress == null) ? 0 : fullAddress.hashCode());
		result = prime * result
				+ ((homepage == null) ? 0 : homepage.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result
				+ ((idContract == null) ? 0 : idContract.hashCode());
		result = prime * result + ((label == null) ? 0 : label.hashCode());
		result = prime * result
				+ ((nutsCode == null) ? 0 : nutsCode.hashCode());
		result = prime * result
				+ ((postalCode == null) ? 0 : postalCode.hashCode());
		result = prime * result + ((region == null) ? 0 : region.hashCode());
		result = prime * result + ((street == null) ? 0 : street.hashCode());
		result = prime * result
				+ ((telephone == null) ? 0 : telephone.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		OrganizationTO other = (OrganizationTO) obj;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (fax == null) {
			if (other.fax != null)
				return false;
		} else if (!fax.equals(other.fax))
			return false;
		if (fullAddress == null) {
			if (other.fullAddress != null)
				return false;
		} else if (!fullAddress.equals(other.fullAddress))
			return false;
		if (homepage == null) {
			if (other.homepage != null)
				return false;
		} else if (!homepage.equals(other.homepage))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (idContract == null) {
			if (other.idContract != null)
				return false;
		} else if (!idContract.equals(other.idContract))
			return false;
		if (label == null) {
			if (other.label != null)
				return false;
		} else if (!label.equals(other.label))
			return false;
		if (nutsCode == null) {
			if (other.nutsCode != null)
				return false;
		} else if (!nutsCode.equals(other.nutsCode))
			return false;
		if (postalCode == null) {
			if (other.postalCode != null)
				return false;
		} else if (!postalCode.equals(other.postalCode))
			return false;
		if (region == null) {
			if (other.region != null)
				return false;
		} else if (!region.equals(other.region))
			return false;
		if (street == null) {
			if (other.street != null)
				return false;
		} else if (!street.equals(other.street))
			return false;
		if (telephone == null) {
			if (other.telephone != null)
				return false;
		} else if (!telephone.equals(other.telephone))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		return true;
	}
	public OrganizationTO() {
		super();
		// TODO Auto-generated constructor stub
	}
	public OrganizationTO(String id, String idContract, String nutsCode,
			String type, String label, String fullAddress, String street,
			String postalCode, String region, String email, String telephone,
			String fax, String homepage) {
		super();
		this.id = id;
		this.idContract = idContract;
		this.nutsCode = nutsCode;
		this.type = type;
		this.label = label;
		this.fullAddress = fullAddress;
		this.street = street;
		this.postalCode = postalCode;
		this.region = region;
		this.email = email;
		this.telephone = telephone;
		this.fax = fax;
		this.homepage = homepage;
	}
	@Override
	public String toString() {
		return "OrganizationTO [id=" + id + ", idContract=" + idContract
				+ ", nutsCode=" + nutsCode + ", type=" + type + ", label="
				+ label + ", fullAddress=" + fullAddress + ", street=" + street
				+ ", postalCode=" + postalCode + ", region=" + region
				+ ", email=" + email + ", telephone=" + telephone + ", fax="
				+ fax + ", homepage=" + homepage + "]";
	}
		

}
