package com.ojas.ra.util;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ojas.ra.domain.Requirement;

/**
 * 
 * @author skkhadar
 *
 */
@SuppressWarnings("serial")
public class Result implements Serializable {

	@JsonProperty
	private List<Requirement> list;

	public List<Requirement> getList() {
		return list;
	}

	public void setList(List<Requirement> list) {
		this.list = list;
	}

}
