package com.ojas.ra.domain;

import java.io.Serializable;

import org.bson.types.ObjectId;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 * 
 * @author skkhadar
 *
 */
@SuppressWarnings("serial")
public class Plans implements Serializable {

	@JsonProperty("_id")
	private ObjectId _id;

	@JsonProperty("rate")
	private String rate;

	@JsonProperty("period")
	private String period;

	@JsonProperty("plus")
	private String plus;

	@JsonProperty("vacancy")
	private String vacancy;

	@JsonProperty("cv")
	private String cv;

	@JsonProperty("email")
	private String email;

	@JsonProperty("status")
	private String status;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public ObjectId get_id() {
		return _id;
	}

	public void set_id(ObjectId _id) {
		this._id = _id;
	}

	public String getRate() {
		return rate;
	}

	public void setRate(String rate) {
		this.rate = rate;
	}

	

	public String getPeriod() {
		return period;
	}

	public void setPeriod(String period) {
		this.period = period;
	}

	public String getPlus() {
		return plus;
	}

	public void setPlus(String plus) {
		this.plus = plus;
	}

	public String getVacancy() {
		return vacancy;
	}

	public void setVacancy(String vacancy) {
		this.vacancy = vacancy;
	}

	public String getCv() {
		return cv;
	}

	public void setCv(String cv) {
		this.cv = cv;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

}
