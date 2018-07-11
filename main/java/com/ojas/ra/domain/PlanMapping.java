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
public class PlanMapping implements Serializable {

	@JsonProperty("_id")
	private ObjectId _id;

	@JsonProperty("registrationId")
	private ObjectId registrationId;

	@JsonProperty("plan_id")
	private ObjectId plan_id;
	
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

	public ObjectId getRegistrationId() {
		return registrationId;
	}

	public void setRegistrationId(ObjectId registrationId) {
		this.registrationId = registrationId;
	}

	public ObjectId getPlan_id() {
		return plan_id;
	}

	public void setPlan_id(ObjectId plan_id) {
		this.plan_id = plan_id;
	}

}
