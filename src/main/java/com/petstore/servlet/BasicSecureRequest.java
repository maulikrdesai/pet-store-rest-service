package com.petstore.servlet;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.springframework.http.HttpHeaders;

import com.mysql.jdbc.StringUtils;
import com.petstore.servlet.exception.AuthenticationFailedException;
import com.petstore.servlet.exception.AuthenticationRequiredException;

public class BasicSecureRequest extends HttpServletRequestWrapper {

	private static final HashMap<String, Set<String>> userRoles = new HashMap<>();
	private static final String USER_PRINCIPAL_KEY = "UserPrincipal";
	private static final String BASIC_AUTH_PATTERN = "Basic (.*)";
	private static final String BASIC_USER_PASS_PATTERN = "([^:]+)*:([^:]+)*";

	static {
		userRoles.put("user", new HashSet<>(Arrays.asList("USER")));
		userRoles.put("user", new HashSet<>(Arrays.asList("USER", "ADMIN")));
	}

	public BasicSecureRequest(HttpServletRequest request) {
		super(request);
	}

	@Override
	public boolean isUserInRole(String role) {
		if (getUserPrincipal() == null)
			return false;
		return getUserPrincipal().getRoles().contains(role);
	}

	public void doLogin() throws ServletException {
		String basicAuth = super.getHeader(HttpHeaders.AUTHORIZATION);
		if (StringUtils.isEmptyOrWhitespaceOnly(basicAuth))
			throw new AuthenticationRequiredException("Please provide BASIC authentication to access resource");

		Matcher matcher = Pattern.compile(BASIC_AUTH_PATTERN).matcher(basicAuth);
		if (!matcher.find())
			throw new AuthenticationFailedException("Authentication could not be parsed");

		basicAuth = matcher.group(1);
		basicAuth = new String(Base64.getDecoder().decode(basicAuth.getBytes(StandardCharsets.UTF_8)),
				StandardCharsets.UTF_8);
		matcher = Pattern.compile(BASIC_USER_PASS_PATTERN).matcher(basicAuth);
		if (!matcher.find())
			throw new AuthenticationFailedException("Authentication could not be parsed");

		this.login(matcher.group(1), matcher.group(2));
	}

	@Override
	public void login(String username, String password) throws ServletException {
		if (!userRoles.containsKey(username) || !password.equalsIgnoreCase(username))
			throw new AuthenticationFailedException("Username/Password does not matches");
		this.getSession().setAttribute(USER_PRINCIPAL_KEY, new UserPrincipal(username, userRoles.get(username)));
	}

	@Override
	public void logout() throws ServletException {
		super.getSession().invalidate();
	}

	@Override
	public UserPrincipal getUserPrincipal() {
		return (UserPrincipal) super.getSession().getAttribute(USER_PRINCIPAL_KEY);
	}

	public boolean isAlreadyLoggedIn() {
		if (getUserPrincipal() == null)
			return false;
		return true;
	}

}
