package com.ojas.ra.mapper;

import java.io.Serializable;

import org.codehaus.jackson.annotate.JsonProperty;
/**
 * 
 * @author skkhadar
 *
 */
@SuppressWarnings("serial")
public class RateTypeMapper implements Serializable{

	@JsonProperty("_id")
	private String _id;

	@JsonProperty("rateType")
	private String rateType;

	public String get_id() {
		return _id;
	}

	public void set_id(String _id) {
		this._id = _id;
	}

	public String getRateType() {
		return rateType;
	}

	public void setRateType(String rateType) {
		this.rateType = rateType;
	}

}
