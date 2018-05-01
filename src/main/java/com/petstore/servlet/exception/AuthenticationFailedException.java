package com.petstore.servlet.exception;

public class AuthenticationFailedException extends RuntimeException	 {

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
