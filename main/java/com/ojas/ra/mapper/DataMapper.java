package com.ojas.ra.mapper;

import java.io.Serializable;

import org.codehaus.jackson.annotate.JsonProperty;
@SuppressWarnings("serial")
public class DataMapper implements Serializable  {
	@JsonProperty("label")
	private String label;
	
	
	@JsonProperty("value")
	private String value;


	public String getLabel() {
		return label;
	}


	public void setLabel(String label) {
		this.label = label;
	}


	public String getValue() {
		return value;
	}


	public void setValue(String value) {
		this.value = value;
	}
	
	

}
