package com.petstore.models.auth;

import java.io.Serializable;

public class UserCredential implements Serializable {

	private static final long serialVersionUID = 1L;
	private String username;
	private String password;

	public UserCredential(String username, String password) {
		super();
		this.username = username;
		this.password = password;
	}

	public UserCredential() {
		super();
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
}
