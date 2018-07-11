package com.ojas.ra.mapper;

import java.io.Serializable;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 * @author skkhadar
 *
 */
@SuppressWarnings("serial")
public class RequirementMapper implements Serializable {

	@JsonProperty("_id")
	private String _id;

	@JsonProperty("jobId")
	private String jobId;

	@JsonProperty("jobCategory")
	private String jobCategory;

	@JsonProperty("jobType")
	private String jobType;

	@JsonProperty("jobTitle")
	private String jobTitle;

	@JsonProperty("description")
	private String description;

	@JsonProperty("jobLocation")
	private String jobLocation;

	@JsonProperty("rateType")
	private String rateType;

	@JsonProperty("yearsOfExperience")
	private String yearsOfExperience;

	@JsonProperty("monthsOfExperience")
	private String monthsOfExperience;

	@JsonProperty("primarySkills")
	private String primarySkills;

	@JsonProperty("secondarySkills")
	private String secondarySkills;

	@JsonProperty("resources")
	private String resources;

	@JsonProperty("joinWithin")
	private String joinWithin;

	@JsonProperty("registrationId")
	private String registrationId;

	@JsonProperty("status")
	private String status;

	@JsonProperty("uploadFile")
	private String uploadFile;

	@JsonProperty("certifications")
	private String certifications;

	@JsonProperty("companyName")
	private String companyName;

	@JsonProperty("budget")
	private String budget;

	@JsonProperty("qualifications")
	private String qualifications;

	@JsonProperty("postedDate")
	private String postedDate;

	@JsonProperty("jobRole")
	private String jobRole;

	@JsonProperty("phoneNumber")
	private String phoneNumber;

	@JsonProperty("gst")
	private String gst;
	
	@JsonProperty("isSowUser")
	private String isSowUser;
	

	@JsonProperty("job")
	private String job;


	public String getJob() {
		return job;
	}

	public void setJob(String job) {
		this.job = job;
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

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getQualifications() {
		return qualifications;
	}

	public void setQualifications(String qualifications) {
		this.qualifications = qualifications;
	}

	public String getPostedDate() {
		return postedDate;
	}

	public void setPostedDate(String postedDate) {
		this.postedDate = postedDate;
	}

	public String getJobRole() {
		return jobRole;
	}

	public void setJobRole(String jobRole) {
		this.jobRole = jobRole;
	}

	public String getJobId() {
		return jobId;
	}

	public void setJobId(String jobId) {
		this.jobId = jobId;
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

	public String getUploadFile() {
		return uploadFile;
	}

	public void setUploadFile(String uploadFile) {
		this.uploadFile = uploadFile;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getRegistrationId() {
		return registrationId;
	}

	public void setRegistrationId(String registrationId) {
		this.registrationId = registrationId;
	}

	public RequirementMapper() {
		super();
	}

	public String get_id() {
		return _id;
	}

	public void set_id(String _id) {
		this._id = _id;
	}

	public String getJobCategory() {
		return jobCategory;
	}

	public void setJobCategory(String jobCategory) {
		this.jobCategory = jobCategory;
	}

	public String getJobType() {
		return jobType;
	}

	public void setJobType(String jobType) {
		this.jobType = jobType;
	}

	public String getJobTitle() {
		return jobTitle;
	}

	public void setJobTitle(String jobTitle) {
		this.jobTitle = jobTitle;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getJobLocation() {
		return jobLocation;
	}

	public void setJobLocation(String jobLocation) {
		this.jobLocation = jobLocation;
	}

	public String getRateType() {
		return rateType;
	}

	public void setRateType(String rateType) {
		this.rateType = rateType;
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

	public String getResources() {
		return resources;
	}

	public void setResources(String resources) {
		this.resources = resources;
	}

	public String getJoinWithin() {
		return joinWithin;
	}

	public void setJoinWithin(String joinWithin) {
		this.joinWithin = joinWithin;
	}

}
