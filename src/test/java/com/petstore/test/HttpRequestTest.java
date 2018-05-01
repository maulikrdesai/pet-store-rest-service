package com.petstore.test;

import static org.assertj.core.api.Assertions.assertThat;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;

import com.petstore.models.ApiResponse;
import com.petstore.models.Category;
import com.petstore.models.Pet;
import com.petstore.models.Tag;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@SuppressWarnings("unchecked")
public class HttpRequestTest {

	private static final String RESOURCE_URL_FORMAT = "http://{0}:{1}/{2}";

	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate restTemplate;

	@Test
	public void noAuthentication() throws Exception {
		assertThat(
				this.restTemplate
						.exchange(MessageFormat.format(RESOURCE_URL_FORMAT, "localhost", port + "", "/pets"),
								HttpMethod.GET, null, ApiResponse.class)
						.getBody().getStatus().equals(HttpStatus.UNAUTHORIZED));
	}

	@Test
	public void unauthorized() throws Exception {
		assertThat(
				this.restTemplate.withBasicAuth("user", UUID.randomUUID().toString())
						.exchange(MessageFormat.format(RESOURCE_URL_FORMAT, "localhost", port + "", "/pets"),
								HttpMethod.GET, null, ApiResponse.class)
						.getBody().getStatus().equals(HttpStatus.UNAUTHORIZED));
	}

	@Test
	public void GET_PETS() throws Exception {
		assertThat(this.restTemplate.withBasicAuth("user", "user")
				.exchange(MessageFormat.format(RESOURCE_URL_FORMAT, "localhost", port + "", "/pets"), HttpMethod.GET,
						null, ApiResponse.class)
				.getBody().getStatus().equals(HttpStatus.OK));
	}

	@Test
	public void GET_PET() throws Exception {
		List<Map<String, Object>> pets = (List<Map<String, Object>>) this.restTemplate.withBasicAuth("user", "user")
				.exchange(MessageFormat.format(RESOURCE_URL_FORMAT, "localhost", port + "", "/pets"), HttpMethod.GET,
						null, ApiResponse.class)
				.getBody().getResult();
		Map<String, Object> pet = pets.get(0);

		assertThat(
				this.restTemplate.withBasicAuth("user", "user")
						.exchange(MessageFormat.format(RESOURCE_URL_FORMAT, "localhost", port + "",
								"/pets/" + pet.get("id")), HttpMethod.GET, null, ApiResponse.class)
						.getBody().getStatus().equals(HttpStatus.OK));
	}

	@Test
	public void DELETE() throws Exception {
		List<Map<String, Object>> pets = (List<Map<String, Object>>) this.restTemplate.withBasicAuth("user", "user")
				.exchange(MessageFormat.format(RESOURCE_URL_FORMAT, "localhost", port + "", "/pets"), HttpMethod.GET,
						null, ApiResponse.class)
				.getBody().getResult();
		Map<String, Object> pet = pets.get(0);

		assertThat(
				this.restTemplate.withBasicAuth("user", "user")
						.exchange(MessageFormat.format(RESOURCE_URL_FORMAT, "localhost", port + "",
								"/pets/" + pet.get("id")), HttpMethod.DELETE, null, ApiResponse.class)
						.getBody().getStatus().equals(HttpStatus.OK));

		assertThat(
				this.restTemplate.withBasicAuth("user", "user")
						.exchange(MessageFormat.format(RESOURCE_URL_FORMAT, "localhost", port + "",
								"/pets/" + pet.get("id")), HttpMethod.GET, null, ApiResponse.class)
						.getBody().getStatus().equals(HttpStatus.NOT_FOUND));
	}

	@Test
	public void POST_PET_BAD_REQ() throws Exception {
		assertThat(this.restTemplate.withBasicAuth("user", "user")
				.exchange(MessageFormat.format(RESOURCE_URL_FORMAT, "localhost", port + "", "/pets"), HttpMethod.POST,
						new HttpEntity<Pet>(new Pet("", null, null, null, null, null)), ApiResponse.class)
				.getBody().getStatus().equals(HttpStatus.BAD_REQUEST));
	}

	@Test
	public void POST_PET_FULL() throws Exception {
		String petName = UUID.randomUUID().toString();
		Pet posted = new Pet(petName, "Descrption", new Category(0, "DOG"),
				Arrays.asList(new String[] { "phot1", "photo2" }),
				Arrays.asList(new Tag[] { new Tag(0, "tag1"), new Tag(0, "tag2") }), "AVAILABLE");
		ApiResponse<Pet> apiResponse = (ApiResponse<Pet>) this.restTemplate.withBasicAuth("user", "user")
				.exchange(MessageFormat.format(RESOURCE_URL_FORMAT, "localhost", port + "", "/pets"), HttpMethod.POST,
						new HttpEntity<Pet>(posted), ApiResponse.class)
				.getBody();
		assertThat(apiResponse.getStatus().equals(HttpStatus.OK));
	}

	@Test
	public void POST_PET_MIN() throws Exception {
		String petName = UUID.randomUUID().toString();
		Pet posted = new Pet(petName, null, null, Arrays.asList(new String[] { "phot1", "photo2" }), null, null);
		ApiResponse<Pet> apiResponse = (ApiResponse<Pet>) this.restTemplate.withBasicAuth("user", "user")
				.exchange(MessageFormat.format(RESOURCE_URL_FORMAT, "localhost", port + "", "/pets"), HttpMethod.POST,
						new HttpEntity<Pet>(posted), ApiResponse.class)
				.getBody();
		assertThat(apiResponse.getStatus().equals(HttpStatus.OK));
	}

}