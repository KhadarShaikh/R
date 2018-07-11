package com.ojas.ra.response;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Map;

import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

/**
 * 
 * @author skkhadar
 *
 */
public class Response {

	private String status;
	private HttpStatus errorCode;
	private String errorMessage;
	private ExceptionType exceptionType;
	private Object result;
	private int count;
	private int pages;
	private Map<String, Integer> vendorSet;
	private Map <String,Map<String, Integer>> allList;

	private long noOfDays;

	public Response(HttpStatus errorCode, Object result, String errorMessage) {
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
		this.result = result;
	}


	public Response(HttpStatus errorCode, String errorMessage, Object result) {

		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
		this.result = result;
	}

	public Response(Map<String, Integer> vendorSet, HttpStatus errorCode, String status) {
		this.errorCode = errorCode;
		this.status = status;
		this.vendorSet = vendorSet;
	}

	/*
	 * public Response(HttpStatus errorCode, String status, Map<String, Integer>
	 * skills, Map<String, Integer> yearsOfExperiencSet, Map<String, Integer>
	 * jobCategorySet, Map<String, Integer> budgetSet, Map<String, Integer>
	 * currentLocationMap, Map<String, Integer> vendorSet) { this.errorCode =
	 * errorCode; this.status = status; this.skillsMap = skills;
	 * this.yearsOfExperienceMap = yearsOfExperiencSet; this.jobCategoryMap =
	 * jobCategorySet; this.budgetMap= budgetSet; this.LocationMap =
	 * currentLocationMap; this.vendorSet = vendorSet; }
	 */

	/**
	 * Constructor
	 * 
	 * @param result
	 */

	public Response(String status) {
		this.status = status;
	}

	public Response(Object result) {
		ObjectMapper mapper = new ObjectMapper();

		mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
		mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		mapper.setDateFormat(df);

		try {

			this.result = mapper.writeValueAsString(result);
			JSONParser parser = new JSONParser();
			try {
				this.result = parser.parse((String) this.result);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
	}

	public Response(Object result, String status) {
		this.status = status;
		this.result = result;
	}

	public Response(Object result, HttpStatus errorCode, String errorMessage) {
		this.result = result;
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
	}

	public Response(String status, Object result, HttpStatus errorCode, String errorMessage, long noOfDays) {
		this.status = status;
		this.result = result;
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
		this.noOfDays = noOfDays;
	}

	public Response(String status, Object result, HttpStatus errorCode, String errorMessage) {
		this.status = status;
		this.result = result;
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
	}
	public Response(Object result,int pages, int count, HttpStatus errorCode, String errorMessage) {
		this.result = result;
		this.pages = pages;
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
		this.count = count;
		this.allList = allList;
	}
	///
	public Response(Object result, Map <String,Map<String, Integer>> allList,int pages, int count, HttpStatus errorCode, String errorMessage) {
		this.result = result;
		this.pages = pages;
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
		this.count = count;
		this.allList = allList;
	}

	public Map<String, Map<String, Integer>> getAllList() {
		return allList;
	}

	public void setAllList(Map<String, Map<String, Integer>> allList) {
		this.allList = allList;
	}

	public Response(String status, HttpStatus errorCode, String errorMessage, ExceptionType exceptionType) {
		this.status = status;
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
		this.exceptionType = exceptionType;
	}

	public Response(Object result, String status, HttpStatus errorCode, String errorMessage,
			ExceptionType exceptionType) {
		this.result = result;
		this.status = status;
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
		this.exceptionType = exceptionType;
	}

	public Response(String status, HttpStatus errorCode, String errorMessage) {
		this.status = status;
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;

	}

	public Response(String status, HttpStatus errorCode) {
		this.status = status;
		this.errorCode = errorCode;
	}

	public Response(Object result, HttpStatus errorCode, String string, ExceptionType noException) {
		this.result = result;

	}

	
	/*
	 * * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param status
	 *            the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	public HttpStatus getErrorCode() {
		return errorCode;
	}

	/**
	 * @param errorCode
	 *            the errorCode to set
	 */
	public void setErrorCode(HttpStatus errorCode) {
		this.errorCode = errorCode;
	}

	/**
	 * @return the errorMessage
	 */
	public String getErrorMessage() {
		return errorMessage;
	}

	/**
	 * @param errorMessage
	 *            the errorMessage to set
	 */
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	/**
	 * @return the String
	 */
	public ExceptionType getString() {
		return exceptionType;
	}

	/**
	 * @param String
	 *            the String to set
	 */
	public void setString(ExceptionType String) {
		this.exceptionType = String;
	}

	/**
	 * @return the result
	 */
	public Object getResult() {
		return result;
	}

	/**
	 * @param result
	 *            the result to set
	 */
	public void setResult(Object result) {
		this.result = result;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public int getPages() {
		return pages;
	}

	public void setPages(int pages) {
		this.pages = pages;
	}

	public ExceptionType getExceptionType() {
		return exceptionType;
	}

	public void setExceptionType(ExceptionType exceptionType) {
		this.exceptionType = exceptionType;
	}


	public Map<String, Integer> getVendorSet() {
		return vendorSet;
	}

	public void setVendorSet(Map<String, Integer> vendorSet) {
		this.vendorSet = vendorSet;
	}

	public long getNoOfDays() {
		return noOfDays;
	}

	public void setNoOfDays(long noOfDays) {
		this.noOfDays = noOfDays;
	}

	

}
