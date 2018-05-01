package com.petstore.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.petstore.servlet.BasicSecurityFilter;

@Configuration
public class ApplicationConfiguration {
	@Bean
	public FilterRegistrationBean<BasicSecurityFilter> corsFilter() {
		FilterRegistrationBean<BasicSecurityFilter> bean = new FilterRegistrationBean<BasicSecurityFilter>(
				new BasicSecurityFilter());
		bean.setOrder(FilterRegistrationBean.HIGHEST_PRECEDENCE);
		return bean;
	}
}
