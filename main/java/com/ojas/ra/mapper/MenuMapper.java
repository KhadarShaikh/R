package com.ojas.ra.mapper;

import java.io.Serializable;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 * 
 * @author skkhadar
 *
 */
@SuppressWarnings("serial")
public class MenuMapper implements Serializable {

	@JsonProperty("_id")
	private String _id;

	@JsonProperty("roleId")
	private String roleId;

	@JsonProperty("moduleName")
	private String moduleName;

	@JsonProperty("headerName")
	private String headerName;

	@JsonProperty("uisref")
	private String uisref;

	@JsonProperty("status")
	private String status;

	public String get_id() {
		return _id;
	}

	public void set_id(String _id) {
		this._id = _id;
	}

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public String getModuleName() {
		return moduleName;
	}

	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}

	public String getHeaderName() {
		return headerName;
	}

	public void setHeaderName(String headerName) {
		this.headerName = headerName;
	}

	public String getUisref() {
		return uisref;
	}

	public void setUisref(String uisref) {
		this.uisref = uisref;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
