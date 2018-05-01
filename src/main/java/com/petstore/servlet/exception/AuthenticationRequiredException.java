package com.petstore.servlet.exception;

public class AuthenticationRequiredException extends AuthenticationFailedException {

	private static final long serialVersionUID = 1L;

	public AuthenticationRequiredException() {
		super();
	}

	public AuthenticationRequiredException(String message, Throwable rootCause) {
		super(message, rootCause);
	}

	public AuthenticationRequiredException(String message) {
		super(message);
	}

	public AuthenticationRequiredException(Throwable rootCause) {
		super(rootCause);
	}

}
