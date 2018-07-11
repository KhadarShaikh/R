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
public class Payment implements Serializable {

	@JsonProperty("_id")
	private ObjectId _id;

	@JsonProperty("registrationId")
	private ObjectId registrationId;

	@JsonProperty("cvv")
	private String cvv;

	@JsonProperty("cardNumber")
	private String cardNumber;

	@JsonProperty("validYear")
	private String validYear;

	@JsonProperty("validMonth")
	private String validMonth;

	@JsonProperty("accountHolderName")
	private String accountHolderName;

	@JsonProperty("cardType")
	private String cardType;

	public String getValidYear() {
		return validYear;
	}

	public void setValidYear(String validYear) {
		this.validYear = validYear;
	}

	public String getValidMonth() {
		return validMonth;
	}

	public void setValidMonth(String validMonth) {
		this.validMonth = validMonth;
	}

	public String getCardType() {
		return cardType;
	}

	public void setCardType(String cardType) {
		this.cardType = cardType;
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

	public String getCvv() {
		return cvv;
	}

	public void setCvv(String cvv) {
		this.cvv = cvv;
	}

	public String getCardNumber() {
		return cardNumber;
	}

	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}

	public String getAccountHolderName() {
		return accountHolderName;
	}

	public void setAccountHolderName(String accountHolderName) {
		this.accountHolderName = accountHolderName;
	}
}
