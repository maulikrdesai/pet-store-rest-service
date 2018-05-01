package com.petstore.config;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.petstore.servlet.UserSecurityFilter;

@Configuration
public class ApplicationConfiguration {

	public static final Map<String, HttpSession> LOGGED_IN_SESSIONS = new HashMap<>();
	public static final String USER_TOKEN_HEADER = "UserToken";

	@Bean
	public FilterRegistrationBean<UserSecurityFilter> securityFilter() {
		FilterRegistrationBean<UserSecurityFilter> bean = new FilterRegistrationBean<UserSecurityFilter>(
				new UserSecurityFilter());
		bean.setOrder(FilterRegistrationBean.HIGHEST_PRECEDENCE);
		return bean;
	}

	@Bean
	public HttpSessionListener httpSessionListener() {
		return new HttpSessionListener() {
			@Override
			public void sessionCreated(HttpSessionEvent event) {
				LOGGED_IN_SESSIONS.put(event.getSession().getId(), event.getSession());
			}

			@Override
			public void sessionDestroyed(HttpSessionEvent event) {
				LOGGED_IN_SESSIONS.remove(event.getSession().getId());
			}
		};
	}
}
