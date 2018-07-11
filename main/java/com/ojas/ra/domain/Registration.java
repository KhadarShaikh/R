package com.ojas.ra.domain;

import java.io.Serializable;
import java.util.Date;

import org.bson.types.ObjectId;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 * 
 * @author skkhadar
 *
 */
@SuppressWarnings("serial")
public class Registration implements Serializable {

	@JsonProperty("_id")
	private ObjectId _id;

	@JsonProperty("numberOfLicences")
	private String numberOfLicences;

	@JsonProperty("trailPeriod")
	private String trailPeriod;

	@JsonProperty("registeredDate")
	private Date registeredDate;

	@JsonProperty("expiryDate")
	private Date expiryDate;
	
	@JsonProperty("status")
	private String status;

	@JsonProperty("trailUser")
	private Boolean trailUser;

	@JsonProperty("companyName")
	private String companyName;

	@JsonProperty("registrationType")
	private String registrationType;

	@JsonProperty("companyType")
	private String companyType;

	@JsonProperty("mailId")
	private String mailId;

	@JsonProperty("mobile")
	private String mobile;

	@JsonProperty("url")
	private String url;

	@JsonProperty("addressLine1")
	private String addressLine1;

	@JsonProperty("addressLine2")
	private String addressLine2;

	@JsonProperty("zipcode")
	private String zipcode;

	@JsonProperty("city")
	private String city;

	@JsonProperty("state")
	private String state;

	@JsonProperty("country")
	private String country;

	@JsonProperty("contactPerson")
	private String contactPerson;

	@JsonProperty("planId")
	private String planId;

	@JsonProperty("sowUser")
	private String sowUser;
	
	

	public Date getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}

	public String getSowUser() {
		return sowUser;
	}

	public void setSowUser(String sowUser) {
		this.sowUser = sowUser;
	}

	public String getPlanId() {
		return planId;
	}

	public void setPlanId(String planId) {
		this.planId = planId;
	}

	private byte[] uploadFile;

	public Registration() {
		super();
	}

	public String getContactPerson() {
		return contactPerson;
	}

	public void setContactPerson(String contactPerson) {
		this.contactPerson = contactPerson;
	}

	public byte[] getUploadFile() {
		return uploadFile;
	}

	public void setUploadFile(byte[] uploadFile) {
		this.uploadFile = uploadFile;
	}

	public String getNumberOfLicences() {
		return numberOfLicences;
	}

	public void setNumberOfLicences(String numberOfLicences) {
		this.numberOfLicences = numberOfLicences;
	}

	public String getTrailPeriod() {
		return trailPeriod;
	}

	public void setTrailPeriod(String trailPeriod) {
		this.trailPeriod = trailPeriod;
	}

	public Date getRegisteredDate() {
		return registeredDate;
	}

	public void setRegisteredDate(Date registeredDate) {
		this.registeredDate = registeredDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Boolean getTrailUser() {
		return trailUser;
	}

	public void setTrailUser(Boolean trailUser) {
		this.trailUser = trailUser;
	}

	public String getRegistrationType() {
		return registrationType;
	}

	public void setRegistrationType(String registrationType) {
		this.registrationType = registrationType;
	}

	public ObjectId get_id() {
		return _id;
	}

	public void set_id(ObjectId _id) {
		this._id = _id;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getCompanyType() {
		return companyType;
	}

	public void setCompanyType(String companyType) {
		this.companyType = companyType;
	}

	public String getMailId() {
		return mailId;
	}

	public void setMailId(String mailId) {
		this.mailId = mailId;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getAddressLine1() {
		return addressLine1;
	}

	public void setAddressLine1(String addressLine1) {
		this.addressLine1 = addressLine1;
	}

	public String getAddressLine2() {
		return addressLine2;
	}

	public void setAddressLine2(String addressLine2) {
		this.addressLine2 = addressLine2;
	}

	public String getZipcode() {
		return zipcode;
	}

	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

}
