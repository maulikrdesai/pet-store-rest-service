package com.petstore.servlet;

import java.security.Principal;
import java.util.Set;

public class UserPrincipal implements Principal {

	private final String username;
	private final Set<String> roles;

	public UserPrincipal(String user, Set<String> roles) {
		super();
		this.username = user;
		this.roles = roles;
	}

	@Override
	public String getName() {
		return username;
	}

	public Set<String> getRoles() {
		return roles;
	}

}
