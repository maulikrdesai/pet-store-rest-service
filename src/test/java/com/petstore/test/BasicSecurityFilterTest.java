package com.petstore.test;

import static org.assertj.core.api.Assertions.assertThat;

import java.text.MessageFormat;
import java.util.UUID;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import com.petstore.models.ApiResponse;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@SuppressWarnings("rawtypes")
public class BasicSecurityFilterTest {

	private static final String RESOURCE_URL_FORMAT = "http://{0}:{1}/{2}";

	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate restTemplate;

	@Test
	public void noAuthentication() throws Exception {
		ResponseEntity<ApiResponse> responseEntity = this.restTemplate.exchange(
				MessageFormat.format(RESOURCE_URL_FORMAT, "localhost", port + "", "/pets"), HttpMethod.GET, null,
				ApiResponse.class);
		assertThat(responseEntity.getBody().getStatus().equals(HttpStatus.UNAUTHORIZED));
		assertThat(responseEntity.getHeaders().get(HttpHeaders.WWW_AUTHENTICATE).contains("Basic realm=\"Realm\""));
		assertCrossOriginHeaders(responseEntity);
	}

	@Test
	public void unauthorized() throws Exception {
		ResponseEntity<ApiResponse> responseEntity = this.restTemplate
				.withBasicAuth("user", UUID.randomUUID().toString())
				.exchange(MessageFormat.format(RESOURCE_URL_FORMAT, "localhost", port + "", "/pets"), HttpMethod.GET,
						null, ApiResponse.class);
		assertThat(responseEntity.getBody().getStatus().equals(HttpStatus.UNAUTHORIZED));
		assertThat(!responseEntity.getHeaders().containsKey(HttpHeaders.WWW_AUTHENTICATE));
		assertCrossOriginHeaders(responseEntity);
	}

	
	@Test
	public void logout() throws Exception {
		ResponseEntity<ApiResponse> responseEntity = this.restTemplate
				.withBasicAuth("user", UUID.randomUUID().toString())
				.exchange(MessageFormat.format(RESOURCE_URL_FORMAT, "localhost", port + "", "/logout"), HttpMethod.GET,
						null, ApiResponse.class);
		assertThat(responseEntity.getBody().getStatus().equals(HttpStatus.OK));
		assertThat(!responseEntity.getHeaders().containsKey(HttpHeaders.WWW_AUTHENTICATE));
		assertCrossOriginHeaders(responseEntity);
	}
	public static void assertCrossOriginHeaders(ResponseEntity<?> responseEntity) {
		assertThat(responseEntity.getHeaders().get(HttpHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS).contains("true"));
		assertThat(responseEntity.getHeaders().get(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN).contains("*"));
		assertThat(responseEntity.getHeaders().get(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS).contains("*"));
		assertThat(responseEntity.getHeaders().get(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS).contains("*"));
	}
}