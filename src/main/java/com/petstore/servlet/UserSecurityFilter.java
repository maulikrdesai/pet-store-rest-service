package com.petstore.servlet;

import java.io.IOException;
import java.io.Writer;
import java.text.MessageFormat;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.filter.GenericFilterBean;

import com.petstore.Application;
import com.petstore.models.ApiResponse;
import com.petstore.servlet.exception.AuthenticationRequiredException;
import com.petstore.utils.ExceptionUtils;

public class UserSecurityFilter extends GenericFilterBean {

	private static Log logger = LogFactory.getLog(UserSecurityFilter.class);

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
			throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		try {

			// Cross-Origin Header added
			addCorsHeaders(httpResponse);
			// Return OPTION request
			if (httpRequest.getMethod().equalsIgnoreCase("OPTIONS"))
				return;
			// Pass-thru for logout
			boolean logoutReq = httpRequest.getRequestURI().startsWith("/logout");
			boolean loginReq = httpRequest.getRequestURI().startsWith("/login");
			boolean getPets = httpRequest.getRequestURI().startsWith("/pets")
					&& httpRequest.getMethod().equalsIgnoreCase("GEt");
			BearerSecureRequest secureRequest = new BearerSecureRequest(httpRequest);

			if (!getPets && !logoutReq && !loginReq && !secureRequest.isAlreadyLoggedIn())
				throw new AuthenticationRequiredException("Please perform login using /login");

			filterChain.doFilter(secureRequest, response);
		} catch (Exception e) {
			HttpStatus httpStatus = ExceptionUtils.resolveStatus(e);
			String message = ExceptionUtils.buildExceptionMessage(e);
			logger.error(message, e);

			httpResponse.reset();
			addCorsHeaders(httpResponse);
			httpResponse.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_UTF8_VALUE);
			if (e instanceof AuthenticationRequiredException)
				httpResponse.addHeader(HttpHeaders.WWW_AUTHENTICATE, "Bearer realm=\"Realm\"");

			httpResponse.setStatus(httpStatus.value());
			Writer writer = httpResponse.getWriter();
			Application.objectMapper.writeValue(writer, new ApiResponse<Void>(httpStatus, message));
			writer.close();
		}
	}

	private void addCorsHeaders(HttpServletResponse httpResponse) {
		httpResponse.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS, "true");
		httpResponse.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "*");
		httpResponse.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS, "*");
		httpResponse.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS, "*");
		httpResponse.addHeader(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.WWW_AUTHENTICATE);
	}

	@Override
	public void destroy() {
		logger.info(MessageFormat.format("{0} destoryed", UserSecurityFilter.class.getName()));
	}

}
