package com.petstore.models.auth;

import java.security.Principal;
import java.util.Set;

import javax.servlet.http.HttpSession;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class UserPrincipal implements Principal {

	private String sessionId;
	private String username;
	private Set<String> roles;

	public UserPrincipal() {
		super();
	}

	public UserPrincipal(HttpSession session, String username, Set<String> roles) {
		super();
		this.sessionId = session.getId();
		this.username = username;
		this.roles = roles;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Set<String> getRoles() {
		return roles;
	}

	public void setRoles(Set<String> roles) {
		this.roles = roles;
	}

	@Override
	@JsonIgnore
	public String getName() {
		return username;
	}

}
