package com.ojas.ra.mapper;

import java.io.Serializable;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 * 
 * @author skkhadar
 *
 */
@SuppressWarnings("serial")
public class ResourceMapper implements Serializable {

	@JsonProperty("_id")
	private String _id;

	@JsonProperty("registrationId")
	private String registrationId;

	@JsonProperty("firstName")
	private String firstName;

	@JsonProperty("lastName")
	private String lastName;

	@JsonProperty("gender")
	private String gender;

	@JsonProperty("dateOfBirth")
	private String dateOfBirth;

	@JsonProperty("mobileNumber")
	private String mobileNumber;

	@JsonProperty("emailId")
	private String emailId;

	@JsonProperty("yearsOfExperience")
	private String yearsOfExperience;

	@JsonProperty("monthsOfExperience")
	private String monthsOfExperience;

	@JsonProperty("currentLocation")
	private String currentLocation;

	@JsonProperty("availability")
	private String availability;

	@JsonProperty("jobCategory")
	private String jobCategory;

	@JsonProperty("resId")
	private String resId;

	@JsonProperty("primarySkills")
	private String primarySkills;

	@JsonProperty("secondarySkills")
	private String secondarySkills;

	@JsonProperty("preferredLocation")
	private String preferredLocation;

	@JsonProperty("rate")
	private String rate;

	@JsonProperty("state")
	private String state;

	@JsonProperty("country")
	private String country;

	@JsonProperty("certifications")
	private String certifications;

	private byte[] uploadResume;

	@JsonProperty("status")
	private String status;

	@JsonProperty("bulkUpload")
	private String bulkUpload;

	@JsonProperty("softLock")
	private String softLock;

	@JsonProperty("hardLock")
	private String hardLock;

	@JsonProperty("companyName")
	private String companyName;

	@JsonProperty("budget")
	private String budget;

	@JsonProperty("relocate")
	private String relocate;

	@JsonProperty("gst")
	private String gst;

	@JsonProperty("isSowUser")
	private String isSowUser;

	@JsonProperty("vendorMobile")
	private String vendorMobile;

	@JsonProperty("vendorEmailId")
	private String vendorEmailId;

	public String getVendorMobile() {
		return vendorMobile;
	}

	public void setVendorMobile(String vendorMobile) {
		this.vendorMobile = vendorMobile;
	}

	public String getVendorEmailId() {
		return vendorEmailId;
	}

	public void setVendorEmailId(String vendorEmailId) {
		this.vendorEmailId = vendorEmailId;
	}

	public String getIsSowUser() {
		return isSowUser;
	}

	public void setIsSowUser(String isSowUser) {
		this.isSowUser = isSowUser;
	}

	public String getGst() {
		return gst;
	}

	public void setGst(String gst) {
		this.gst = gst;
	}

	public String getRelocate() {
		return relocate;
	}

	public void setRelocate(String relocate) {
		this.relocate = relocate;
	}

	public String getResId() {
		return resId;
	}

	public void setResId(String resId) {
		this.resId = resId;
	}

	public String getBudget() {
		return budget;
	}

	public void setBudget(String budget) {
		this.budget = budget;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getCertifications() {
		return certifications;
	}

	public void setCertifications(String certifications) {
		this.certifications = certifications;
	}

	public String getSoftLock() {
		return softLock;
	}

	public void setSoftLock(String softLock) {
		this.softLock = softLock;
	}

	public String getHardLock() {
		return hardLock;
	}

	public void setHardLock(String hardLock) {
		this.hardLock = hardLock;
	}

	public String getBulkUpload() {
		return bulkUpload;
	}

	public void setBulkUpload(String bulkUpload) {
		this.bulkUpload = bulkUpload;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public byte[] getUploadResume() {
		return uploadResume;
	}

	public void setUploadResume(byte[] uploadResume) {
		this.uploadResume = uploadResume;
	}

	public String get_id() {
		return _id;
	}

	public void set_id(String _id) {
		this._id = _id;
	}

	public String getRegistrationId() {
		return registrationId;
	}

	public void setRegistrationId(String registrationId) {
		this.registrationId = registrationId;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(String dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getYearsOfExperience() {
		return yearsOfExperience;
	}

	public void setYearsOfExperience(String yearsOfExperience) {
		this.yearsOfExperience = yearsOfExperience;
	}

	public String getMonthsOfExperience() {
		return monthsOfExperience;
	}

	public void setMonthsOfExperience(String monthsOfExperience) {
		this.monthsOfExperience = monthsOfExperience;
	}

	public String getCurrentLocation() {
		return currentLocation;
	}

	public void setCurrentLocation(String currentLocation) {
		this.currentLocation = currentLocation;
	}

	public String getAvailability() {
		return availability;
	}

	public void setAvailability(String availability) {
		this.availability = availability;
	}

	public String getJobCategory() {
		return jobCategory;
	}

	public void setJobCategory(String jobCategory) {
		this.jobCategory = jobCategory;
	}

	public String getPrimarySkills() {
		return primarySkills;
	}

	public void setPrimarySkills(String primarySkills) {
		this.primarySkills = primarySkills;
	}

	public String getSecondarySkills() {
		return secondarySkills;
	}

	public void setSecondarySkills(String secondarySkills) {
		this.secondarySkills = secondarySkills;
	}

	public String getPreferredLocation() {
		return preferredLocation;
	}

	public void setPreferredLocation(String preferredLocation) {
		this.preferredLocation = preferredLocation;
	}

	public String getRate() {
		return rate;
	}

	public void setRate(String rate) {
		this.rate = rate;
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
