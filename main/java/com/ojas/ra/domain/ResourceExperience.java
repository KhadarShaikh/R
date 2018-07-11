package com.ojas.ra.domain;

import java.io.Serializable;

import org.bson.types.ObjectId;
import org.codehaus.jackson.annotate.JsonProperty;

@SuppressWarnings("serial")
public class ResourceExperience implements Serializable {

	@JsonProperty("_id")
	private ObjectId _id;

	@JsonProperty("yearsOfExperience")
	private String yearsOfExperience;

	@JsonProperty("monthsOfExperience")
	private String monthsOfExperience;

	public ObjectId get_id() {
		return _id;
	}

	public void set_id(ObjectId _id) {
		this._id = _id;
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

}
