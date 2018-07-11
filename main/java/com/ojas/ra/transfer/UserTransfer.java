package com.ojas.ra.transfer;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.ser.ToStringSerializer;

import com.ojas.ra.mapper.PlansMapper;
import com.ojas.ra.mapper.RoleMapper;

/**
 * @author skkhadar
 *
 */
@SuppressWarnings("serial")
public class UserTransfer implements Serializable {

	@JsonSerialize(using = ToStringSerializer.class)
	private String _id;

	@JsonProperty("username")
	private String username;

	@JsonProperty("password")
	private String password;

	@JsonProperty("firstname")
	private String firstname;

	@JsonProperty("lastname")
	private String lastname;

	@JsonProperty("status")
	private String status;

	@JsonProperty("enabled")
	private Boolean enabled;

	@JsonProperty("softLock")
	private String softLock;

	@JsonProperty("hardLock")
	private String hardLock;

	private Boolean accountNonExpired;
	private Boolean accountNonLocked;
	private Boolean credentialsNonExpired;

	private String registrationId;

	private String registrationType;

	private List<RoleMapper> roleMapper;

	private List<PlansMapper> planMapper;

	private Map<String, String> roles;

	@JsonProperty("mobile")
	private String mobile;

	public List<PlansMapper> getPlanMapper() {
		return planMapper;
	}

	public void setPlanMapper(List<PlansMapper> planMapper) {
		this.planMapper = planMapper;
	}

	public String getSoftLock() {
		return softLock;
	}

	public void setSoftLock(String softLock) {
		this.softLock = softLock;
	}

	public String getHardLock() {
		return hardLock;
	}

	public void setHardLock(String hardLock) {
		this.hardLock = hardLock;
	}

	public UserTransfer() {
	}

	public String getRegistrationType() {
		return registrationType;
	}

	public void setRegistrationType(String registrationType) {
		this.registrationType = registrationType;
	}

	public String getMobile() {

		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public List<RoleMapper> getRoleMapper() {
		return roleMapper;
	}

	public void setRoleMapper(List<RoleMapper> roleMapper) {
		this.roleMapper = roleMapper;
	}

	public String get_id() {
		return _id;
	}

	public void set_id(String _id) {
		this._id = _id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	public Boolean getAccountNonExpired() {
		return accountNonExpired;
	}

	public void setAccountNonExpired(Boolean accountNonExpired) {
		this.accountNonExpired = accountNonExpired;
	}

	public Boolean getAccountNonLocked() {
		return accountNonLocked;
	}

	public void setAccountNonLocked(Boolean accountNonLocked) {
		this.accountNonLocked = accountNonLocked;
	}

	public Boolean getCredentialsNonExpired() {
		return credentialsNonExpired;
	}

	public void setCredentialsNonExpired(Boolean credentialsNonExpired) {
		this.credentialsNonExpired = credentialsNonExpired;
	}

	public String getRegistrationId() {
		return registrationId;
	}

	public void setRegistrationId(String registrationId) {
		this.registrationId = registrationId;
	}

	public Map<String, String> getRoles() {
		return roles;
	}

	public void setRoles(Map<String, String> roles) {
		this.roles = roles;
	}

}