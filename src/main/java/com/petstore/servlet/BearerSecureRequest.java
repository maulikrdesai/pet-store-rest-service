package com.petstore.servlet;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.util.StringUtils;

import com.petstore.Application;
import com.petstore.config.ApplicationConfiguration;
import com.petstore.models.auth.UserPrincipal;

public class BearerSecureRequest extends HttpServletRequestWrapper {

	private static final String BEARER_AUTH_PATTERN = "Bearer (.*)";
	private static Log logger = LogFactory.getLog(BearerSecureRequest.class);

	public BearerSecureRequest(HttpServletRequest request) {
		super(request);
	}

	public boolean isAlreadyLoggedIn() {
		return getSession(false) != null;
	}

	@Override
	public HttpSession getSession() {
		return getSession(true);
	}

	@Override
	public HttpSession getSession(boolean create) {
		HttpSession session = null;
		UserPrincipal principal = getUserPrincipal();
		if (principal != null)
			session = ApplicationConfiguration.LOGGED_IN_SESSIONS.get(principal.getSessionId());
		if (session != null || !create)
			return session;
		return super.getSession(create);
	}

	@Override
	public UserPrincipal getUserPrincipal() {
		try {
			String authorization = super.getHeader(HttpHeaders.AUTHORIZATION);
			if (StringUtils.isEmpty(authorization))
				return null;
			Matcher matcher = Pattern.compile(BEARER_AUTH_PATTERN).matcher(authorization);
			if (!matcher.find())
				return null;
			authorization = matcher.group(1);
			return Application.objectMapper.readValue(
					Base64.getDecoder().decode(authorization.getBytes(StandardCharsets.UTF_8)), UserPrincipal.class);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}

}
