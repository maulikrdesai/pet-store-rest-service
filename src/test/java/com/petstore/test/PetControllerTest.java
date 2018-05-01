package com.petstore.test;

import static org.assertj.core.api.Assertions.assertThat;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Base64;
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
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import com.petstore.Application;
import com.petstore.models.ApiResponse;
import com.petstore.models.Category;
import com.petstore.models.Pet;
import com.petstore.models.Tag;
import com.petstore.models.auth.UserCredential;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@SuppressWarnings({ "unchecked", "rawtypes" })
public class PetControllerTest {

	private static final String RESOURCE_URL_FORMAT = "http://{0}:{1}/{2}";

	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate restTemplate;

	public RequestEntity buildRequest(boolean authorized, String url, HttpMethod method, Object body,
			HttpHeaders baseHeader) throws Exception {
		HttpHeaders httpHeaders = new HttpHeaders();
		if (baseHeader != null)
			httpHeaders.addAll(baseHeader);

		if (authorized) {
			HttpEntity<UserCredential> userCredentials = new HttpEntity<UserCredential>(
					new UserCredential("user", "user"));
			ResponseEntity<ApiResponse> loginResponse = this.restTemplate.exchange(
					MessageFormat.format(RESOURCE_URL_FORMAT, "localhost", port + "", "/login"), HttpMethod.POST,
					userCredentials, ApiResponse.class);
			httpHeaders.add(HttpHeaders.AUTHORIZATION,
					"Bearer " + Base64.getEncoder().encodeToString(Application.objectMapper
							.writeValueAsString(loginResponse.getBody().getResult()).getBytes(StandardCharsets.UTF_8)));
		}
		return new RequestEntity(body, httpHeaders, method, new URI(url));
	}

	public ResponseEntity<ApiResponse> getAllPets() throws Exception {
		ResponseEntity<ApiResponse> responseEntity = this.restTemplate.exchange(buildRequest(true,
				MessageFormat.format(RESOURCE_URL_FORMAT, "localhost", port + "", "/pets"), HttpMethod.GET, null, null),
				ApiResponse.class);
		return responseEntity;
	}

	public ResponseEntity<ApiResponse> getThePet(String petId) throws Exception {
		String url = MessageFormat.format(RESOURCE_URL_FORMAT, "localhost", port + "", "/pets/" + petId);
		return this.restTemplate.withBasicAuth("user", "user")
				.exchange(buildRequest(true, url, HttpMethod.GET, null, null), ApiResponse.class);
	}

	public ResponseEntity<ApiResponse> deleteThePet(String petId) throws Exception {
		String url = MessageFormat.format(RESOURCE_URL_FORMAT, "localhost", port + "", "/pets/" + petId);
		return this.restTemplate.withBasicAuth("user", "user")
				.exchange(buildRequest(true, url, HttpMethod.DELETE, null, null), ApiResponse.class);
	}

	public ResponseEntity<ApiResponse> postAPet(Pet pet) throws Exception {
		String url = MessageFormat.format(RESOURCE_URL_FORMAT, "localhost", port + "", "/pets");
		RequestEntity<Pet> requestEntity = buildRequest(true, url, HttpMethod.POST, pet, null);
		return this.restTemplate.withBasicAuth("user", "user").exchange(requestEntity, ApiResponse.class);

	}

	@Test
	public void NO_AUTHENTICATION() throws Exception {
		ResponseEntity<ApiResponse> responseEntity = this.restTemplate.exchange(buildRequest(false,
				MessageFormat.format(RESOURCE_URL_FORMAT, "localhost", port + "", "/pets"), HttpMethod.GET, null, null),
				ApiResponse.class);
		assertThat(responseEntity.getBody().getStatus().equals(HttpStatus.UNAUTHORIZED));
		assertThat(responseEntity.getHeaders().get(HttpHeaders.WWW_AUTHENTICATE).contains("Bearer realm=\"Realm\""));
		assertCrossOriginHeaders(responseEntity);
	}

	@Test
	public void UNAUTHORIZED() throws Exception {
		ResponseEntity<ApiResponse> responseEntity = this.restTemplate.exchange(buildRequest(false,
				MessageFormat.format(RESOURCE_URL_FORMAT, "localhost", port + "", "/pets"), HttpMethod.GET, null, null),
				ApiResponse.class);
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

	@Test
	public void GET_PETS() throws Exception {
		ResponseEntity<ApiResponse> responseEntity = this.getAllPets();
		assertThat(responseEntity.getBody().getStatus().equals(HttpStatus.OK));
	}

	@Test
	public void GET_A_PET() throws Exception {
		ResponseEntity<ApiResponse> responseEntity = getAllPets();
		List<Map<String, Object>> pets = (List<Map<String, Object>>) responseEntity.getBody().getResult();
		Map<String, Object> pet = pets.get(0);
		responseEntity = getThePet(pet.get("id").toString());
		assertThat(responseEntity.getBody().getStatus().equals(HttpStatus.OK));
	}

	@Test
	public void DELETE_A_PET() throws Exception {
		ResponseEntity<ApiResponse> responseEntity = getAllPets();
		List<Map<String, Object>> pets = (List<Map<String, Object>>) responseEntity.getBody().getResult();
		Map<String, Object> pet = pets.get(0);
		assertThat(responseEntity.getBody().getStatus().equals(HttpStatus.OK));

		String petId = pet.get("id").toString();
		responseEntity = deleteThePet(petId);
		assertThat(responseEntity.getBody().getStatus().equals(HttpStatus.OK));

		responseEntity = getThePet(petId);
		assertThat(responseEntity.getBody().getStatus().equals(HttpStatus.NOT_FOUND));
	}

	@Test
	public void POST_PET_BAD_REQ() throws Exception {
		String url = MessageFormat.format(RESOURCE_URL_FORMAT, "localhost", port + "", "/pets");
		RequestEntity<Pet> requestEntity = buildRequest(true, url, HttpMethod.POST,
				new Pet("", null, null, null, null, null), null);
		ResponseEntity<ApiResponse> responseEntity = this.restTemplate.withBasicAuth("user", "user")
				.exchange(requestEntity, ApiResponse.class);
		assertThat(responseEntity.getBody().getStatus().equals(HttpStatus.BAD_REQUEST));
	}

	@Test
	public void POST_A_FULLY_DETAILED_PET() throws Exception {
		String petName = UUID.randomUUID().toString();
		Pet pet = new Pet(petName, "Descrption", new Category(0, "DOG"),
				Arrays.asList(new String[] { "phot1", "photo2" }),
				Arrays.asList(new Tag[] { new Tag(0, "tag1"), new Tag(0, "tag2") }), "AVAILABLE");

		ResponseEntity<ApiResponse> responseEntity = postAPet(pet);
		ApiResponse<Pet> apiResponse = (ApiResponse<Pet>) responseEntity.getBody();
		assertThat(apiResponse.getStatus().equals(HttpStatus.OK));
	}

	@Test
	public void POST_A_MIN_DETAILED_PET() throws Exception {
		String petName = UUID.randomUUID().toString();
		Pet pet = new Pet(petName, null, null, Arrays.asList(new String[] { "phot1", "photo2" }), null, null);
		ResponseEntity<ApiResponse> responseEntity = postAPet(pet);
		ApiResponse<Pet> apiResponse = (ApiResponse<Pet>) responseEntity.getBody();
		assertThat(apiResponse.getStatus().equals(HttpStatus.OK));
	}

	public static void assertCrossOriginHeaders(ResponseEntity<?> responseEntity) {
		assertThat(responseEntity.getHeaders().get(HttpHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS).contains("true"));
		assertThat(responseEntity.getHeaders().get(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN).contains("*"));
		assertThat(responseEntity.getHeaders().get(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS).contains("*"));
		assertThat(responseEntity.getHeaders().get(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS).contains("*"));
	}

}