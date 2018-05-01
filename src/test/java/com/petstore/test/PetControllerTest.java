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
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import com.petstore.models.ApiResponse;
import com.petstore.models.Category;
import com.petstore.models.Pet;
import com.petstore.models.Tag;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@SuppressWarnings({ "unchecked", "rawtypes" })
public class PetControllerTest {

	private static final String RESOURCE_URL_FORMAT = "http://{0}:{1}/{2}";

	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate restTemplate;

	@Test
	public void GET_PETS() throws Exception {
		ResponseEntity<ApiResponse> responseEntity = this.restTemplate.withBasicAuth("user", "user").exchange(
				MessageFormat.format(RESOURCE_URL_FORMAT, "localhost", port + "", "/pets"), HttpMethod.GET, null,
				ApiResponse.class);
		assertThat(responseEntity.getBody().getStatus().equals(HttpStatus.OK));
	}

	@Test
	public void GET_PET() throws Exception {
		ResponseEntity<ApiResponse> responseEntity = this.restTemplate.withBasicAuth("user", "user").exchange(
				MessageFormat.format(RESOURCE_URL_FORMAT, "localhost", port + "", "/pets"), HttpMethod.GET, null,
				ApiResponse.class);
		List<Map<String, Object>> pets = (List<Map<String, Object>>) responseEntity.getBody().getResult();
		Map<String, Object> pet = pets.get(0);

		responseEntity = this.restTemplate.withBasicAuth("user", "user").exchange(
				MessageFormat.format(RESOURCE_URL_FORMAT, "localhost", port + "", "/pets/" + pet.get("id")),
				HttpMethod.GET, null, ApiResponse.class);
		assertThat(responseEntity.getBody().getStatus().equals(HttpStatus.OK));
	}

	@Test
	public void DELETE() throws Exception {
		ResponseEntity<ApiResponse> responseEntity = this.restTemplate.withBasicAuth("user", "user").exchange(
				MessageFormat.format(RESOURCE_URL_FORMAT, "localhost", port + "", "/pets"), HttpMethod.GET, null,
				ApiResponse.class);
		List<Map<String, Object>> pets = (List<Map<String, Object>>) responseEntity.getBody().getResult();
		Map<String, Object> pet = pets.get(0);

		responseEntity = this.restTemplate.withBasicAuth("user", "user").exchange(
				MessageFormat.format(RESOURCE_URL_FORMAT, "localhost", port + "", "/pets/" + pet.get("id")),
				HttpMethod.DELETE, null, ApiResponse.class);
		assertThat(responseEntity.getBody().getStatus().equals(HttpStatus.OK));

		responseEntity = this.restTemplate.withBasicAuth("user", "user").exchange(
				MessageFormat.format(RESOURCE_URL_FORMAT, "localhost", port + "", "/pets/" + pet.get("id")),
				HttpMethod.GET, null, ApiResponse.class);
		assertThat(responseEntity.getBody().getStatus().equals(HttpStatus.NOT_FOUND));
	}

	@Test
	public void POST_PET_BAD_REQ() throws Exception {
		ResponseEntity<ApiResponse> responseEntity = this.restTemplate.withBasicAuth("user", "user").exchange(
				MessageFormat.format(RESOURCE_URL_FORMAT, "localhost", port + "", "/pets"), HttpMethod.POST,
				new HttpEntity<Pet>(new Pet("", null, null, null, null, null)), ApiResponse.class);
		assertThat(responseEntity.getBody().getStatus().equals(HttpStatus.BAD_REQUEST));
	}

	@Test
	public void POST_PET_FULL() throws Exception {
		String petName = UUID.randomUUID().toString();
		Pet posted = new Pet(petName, "Descrption", new Category(0, "DOG"),
				Arrays.asList(new String[] { "phot1", "photo2" }),
				Arrays.asList(new Tag[] { new Tag(0, "tag1"), new Tag(0, "tag2") }), "AVAILABLE");
		ResponseEntity<ApiResponse> responseEntity = this.restTemplate.withBasicAuth("user", "user").exchange(
				MessageFormat.format(RESOURCE_URL_FORMAT, "localhost", port + "", "/pets"), HttpMethod.POST,
				new HttpEntity<Pet>(posted), ApiResponse.class);
		ApiResponse<Pet> apiResponse = (ApiResponse<Pet>) responseEntity.getBody();
		assertThat(apiResponse.getStatus().equals(HttpStatus.OK));
	}

	@Test
	public void POST_PET_MIN() throws Exception {
		String petName = UUID.randomUUID().toString();
		Pet posted = new Pet(petName, null, null, Arrays.asList(new String[] { "phot1", "photo2" }), null, null);
		ResponseEntity<ApiResponse> responseEntity = this.restTemplate.withBasicAuth("user", "user").exchange(
				MessageFormat.format(RESOURCE_URL_FORMAT, "localhost", port + "", "/pets"), HttpMethod.POST,
				new HttpEntity<Pet>(posted), ApiResponse.class);
		ApiResponse<Pet> apiResponse = (ApiResponse<Pet>) responseEntity.getBody();
		assertThat(apiResponse.getStatus().equals(HttpStatus.OK));
	}

}