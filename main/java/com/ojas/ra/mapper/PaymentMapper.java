package com.ojas.ra.mapper;

import java.io.Serializable;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 * 
 * @author skkhadar
 *
 */
@SuppressWarnings("serial")
public class PaymentMapper implements Serializable {

	@JsonProperty("_id")
	private String _id;

	@JsonProperty("registrationId")
	private String registrationId;

	@JsonProperty("cvv")
	private String cvv;

	@JsonProperty("cardNumber")
	private String cardNumber;

	@JsonProperty("validYear")
	private String validYear;
	
	@JsonProperty("validMonth")	
   private String validMonth;


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

	@JsonProperty("cardType")
	private String cardType;

	@JsonProperty("accountHolderName")
	private String accountHolderName;

	public String getCardType() {
		return cardType;
	}

	public void setCardType(String cardType) {
		this.cardType = cardType;
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
