package com.petstore.servlet.exception;

import javax.servlet.ServletException;

public class AuthenticationFailedException extends ServletException {

	private static final long serialVersionUID = 1L;

	public AuthenticationFailedException() {
		super();
	}

	public AuthenticationFailedException(String message, Throwable rootCause) {
		super(message, rootCause);
	}

	public AuthenticationFailedException(String message) {
		super(message);
	}

	public AuthenticationFailedException(Throwable rootCause) {
		super(rootCause);
	}

}
