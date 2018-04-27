package com.petstore.models;

import java.io.Serializable;
import java.util.Date;

import org.springframework.http.HttpStatus;

public class ApiResponse<T> implements Serializable {

	private static final long serialVersionUID = 1L;

	private HttpStatus status;
	private String message;
	private Date timeStamp;
	private T result;

	public ApiResponse() {
		this(null, null, null);
	}

	public ApiResponse(HttpStatus status, String message) {
		this(status, message, null);
	}

	public ApiResponse(HttpStatus status, String message, T result) {
		super();
		this.timeStamp = new Date();
		this.status = status;
		this.message = message;
		this.result = result;
	}

	public HttpStatus getStatus() {
		return status;
	}

	public void setStatus(HttpStatus status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public T getResult() {
		return result;
	}

	public void setResult(T result) {
		this.result = result;
	}

	public Date getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(Date timeStamp) {
		this.timeStamp = timeStamp;
	}

}
