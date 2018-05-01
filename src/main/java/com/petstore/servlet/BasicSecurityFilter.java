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

public class BasicSecurityFilter extends GenericFilterBean {

	private static Log logger = LogFactory.getLog(BasicSecurityFilter.class);

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
			if (httpRequest.getRequestURI().startsWith(httpRequest.getContextPath() + "/logout")) {
				filterChain.doFilter(httpRequest, httpResponse);
				return;
			}
			// Everything is Authorized now
			else {
				BasicSecureRequest secureRequest = new BasicSecureRequest(httpRequest);
				if (secureRequest.isAlreadyLoggedIn())
					filterChain.doFilter(secureRequest, response);
				// Perform Login
				secureRequest.doLogin();
				// Chain Further
				filterChain.doFilter(secureRequest, httpResponse);
			}
		} catch (Exception e) {
			HttpStatus httpStatus = ExceptionUtils.resolveStatus(e);
			String message = ExceptionUtils.buildExceptionMessage(e);
			logger.error(message, e);

			httpResponse.reset();
			addCorsHeaders(httpResponse);
			httpResponse.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_UTF8_VALUE);
			if (e instanceof AuthenticationRequiredException) {
				httpResponse.addHeader(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.WWW_AUTHENTICATE);
				httpResponse.addHeader(HttpHeaders.WWW_AUTHENTICATE, "Basic realm=\"Realm\"");
			}

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
	}

	@Override
	public void destroy() {
		logger.info(MessageFormat.format("{0} destoryed", BasicSecurityFilter.class.getName()));
	}

}
