package com.ojas.ra.mapper;

import java.io.Serializable;

import org.bson.types.ObjectId;
import org.codehaus.jackson.annotate.JsonProperty;

@SuppressWarnings("serial")
public class ProposedResource implements Serializable{
	@JsonProperty("_id")
	private ObjectId _id;

	@JsonProperty("requirementId")
	private ObjectId requirementId;

	@JsonProperty("registrationId")
	private ObjectId registrationId;

	@JsonProperty("vendorId")
	private ObjectId vendorId;

	@JsonProperty("resourceId")
	private ObjectId resourceId;

	@JsonProperty("status")
	private String status;

	public ObjectId get_id() {
		return _id;
	}

	public void set_id(ObjectId _id) {
		this._id = _id;
	}

	public ObjectId getRequirementId() {
		return requirementId;
	}

	public void setRequirementId(ObjectId requirementId) {
		this.requirementId = requirementId;
	}

	public ObjectId getRegistrationId() {
		return registrationId;
	}

	public void setRegistrationId(ObjectId registrationId) {
		this.registrationId = registrationId;
	}

	public ObjectId getVendorId() {
		return vendorId;
	}

	public void setVendorId(ObjectId vendorId) {
		this.vendorId = vendorId;
	}

	public ObjectId getResourceId() {
		return resourceId;
	}

	public void setResourceId(ObjectId resourceId) {
		this.resourceId = resourceId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	
	

}
