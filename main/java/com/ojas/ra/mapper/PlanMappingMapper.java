package com.ojas.ra.mapper;

import java.io.Serializable;

import org.codehaus.jackson.annotate.JsonProperty;
/**
 * 
 * @author skkhadar
 *
 */
@SuppressWarnings("serial")
public class PlanMappingMapper implements Serializable {

	@JsonProperty("_id")
	private String _id;

	@JsonProperty("registrationId")
	private String registrationId;

	@JsonProperty("plan_id")
	private String plan_id;

	@JsonProperty("status")
	private String status;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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

	public String getPlan_id() {
		return plan_id;
	}

	public void setPlan_id(String plan_id) {
		this.plan_id = plan_id;
	}

}
