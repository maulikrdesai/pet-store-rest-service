package com.petstore.controllers;

import java.text.MessageFormat;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;

import com.petstore.dao.entities.PetEntity;
import com.petstore.models.ApiResponse;
import com.petstore.models.Pet;
import com.petstore.services.PetService;

/***
 * PetController offers following REST operation on Pet resource<br>
 * GET /pets <br>
 * GET /pets/{id} <br>
 * POST /pets <br>
 * DELETE /pets/{id} <br>
 * 
 * @author mdesai
 *
 */
@RestController
@CrossOrigin(allowCredentials = "true", allowedHeaders = "*", origins = "*")
public class PetController {

	@Autowired
	private PetService petService;

	@RequestMapping(method = { RequestMethod.GET }, path = "/pets")
	public ApiResponse<List<PetEntity>> getPets() {
		List<PetEntity> petsInStore = petService.findAll();
		return new ApiResponse<List<PetEntity>>(HttpStatus.OK,
				MessageFormat.format("{0} pet(s) found.", petsInStore.size()), petsInStore);
	}

	@RequestMapping(method = { RequestMethod.GET }, path = "/pets/{id}")
	public ApiResponse<PetEntity> getPet(@PathVariable("id") long petId) {
		Optional<PetEntity> optionalPet = petService.findById(petId);
		if (!optionalPet.isPresent())
			throw new HttpClientErrorException(HttpStatus.NOT_FOUND,
					MessageFormat.format("Pet with id {0} does not belong to the store", petId));

		PetEntity pet = optionalPet.get();
		return new ApiResponse<PetEntity>(HttpStatus.OK,
				MessageFormat.format("Pet[ID:{0}] found in the store.", pet.getPetId(), pet.getPetName()), pet);
	}

	@RequestMapping(method = { RequestMethod.PUT }, path = "/pets/{id}")
	public ApiResponse<PetEntity> editPet(@PathVariable("id") long petId, @RequestBody @Valid Pet petUpdate) {
		return new ApiResponse<PetEntity>(HttpStatus.OK,
				MessageFormat.format("Pet[ID:{0}, Name:{1}] successfully inserted into the store.", petUpdate.getId(),
						petUpdate.getName()),
				petService.update(petId, petUpdate));
	}

	@RequestMapping(path = "/pets", method = { RequestMethod.POST })
	public ApiResponse<PetEntity> postPet(@RequestBody @Valid Pet newPet) {
		return new ApiResponse<PetEntity>(HttpStatus.OK,
				MessageFormat.format("Pet[ID:{0}, Name:{1}] successfully inserted into the store.", newPet.getId(),
						newPet.getName()),
				petService.save(newPet));
	}

	@RequestMapping(path = "/pets/{id}", method = { RequestMethod.DELETE })
	public ApiResponse<Void> deletePet(@PathVariable("id") long petId) {
		Optional<PetEntity> pet = petService.findById(petId);

		if (!pet.isPresent())
			throw new HttpClientErrorException(HttpStatus.NOT_FOUND,
					MessageFormat.format("Pet with id {0} does not belong to the store", petId));

		petService.deleteById(petId);
		return new ApiResponse<Void>(HttpStatus.OK,
				MessageFormat.format("Pet[ID:{0}] successfully removed from the store.", petId));
	}

}