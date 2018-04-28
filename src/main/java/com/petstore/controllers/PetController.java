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

import com.petstore.dao.entities.CategoryEntity;
import com.petstore.dao.entities.PetEntity;
import com.petstore.dao.repository.CategoryRepository;
import com.petstore.dao.repository.PetRepository;
import com.petstore.models.ApiResponse;
import com.petstore.models.Pet;

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
	private PetRepository petRepo;
	@Autowired
	private CategoryRepository categoryRepo;

	@RequestMapping(method = { RequestMethod.GET }, path = "/pets")
	public ApiResponse<List<PetEntity>> getPets() {
		List<PetEntity> petsInStore = petRepo.findAll();
		return new ApiResponse<List<PetEntity>>(HttpStatus.OK,
				MessageFormat.format("{0} pet(s) found.", petsInStore.size()), petsInStore);
	}

	@RequestMapping(method = { RequestMethod.GET }, path = "/pets/{id}")
	public ApiResponse<PetEntity> getPet(@PathVariable("id") long petId) {
		Optional<PetEntity> pet = petRepo.findById(petId);
		if (!pet.isPresent())
			throw new HttpClientErrorException(HttpStatus.NOT_FOUND,
					MessageFormat.format("Pet with id {0} does not belong to the store", petId));

		return new ApiResponse<PetEntity>(HttpStatus.OK,
				MessageFormat.format("Pet[ID:{0}] found in the store.", pet.get().getPetId(), pet.get().getPetName()),
				pet.get());
	}

	@RequestMapping(path = "/pets", method = { RequestMethod.POST })
	public ApiResponse<Void> postPet(@Valid @RequestBody Pet newPet) {

		CategoryEntity categoryEntity = null;
		if (newPet.getCategory() != null && newPet.getCategory().getId() > 0) {
			Optional<CategoryEntity> optionalCategory = categoryRepo.findById(newPet.getCategory().getId());
			if (!optionalCategory.isPresent())
				throw new HttpClientErrorException(HttpStatus.NOT_FOUND,
						MessageFormat.format("Category[ID:{0}, Name:{1}] does not belong to the store",
								newPet.getCategory().getId(), newPet.getCategory().getName()));
		}

		PetEntity newPetEntity = new PetEntity(newPet.getName());
		newPetEntity.setCategoryEntity(categoryEntity);
		petRepo.save(newPetEntity);
		return new ApiResponse<Void>(HttpStatus.OK, MessageFormat.format(
				"Pet[ID:{0}, Name:{1}] successfully inserted into the store.", newPet.getId(), newPet.getName()));
	}

	@RequestMapping(path = "/pets/{id}", method = { RequestMethod.DELETE })
	public ApiResponse<Void> deletePet(@PathVariable("id") long petId) {
		Optional<PetEntity> pet = petRepo.findById(petId);

		if (!pet.isPresent())
			throw new HttpClientErrorException(HttpStatus.NOT_FOUND,
					MessageFormat.format("Pet with id {0} does not belong to the store", petId));

		petRepo.deleteById(petId);
		return new ApiResponse<Void>(HttpStatus.OK,
				MessageFormat.format("Pet[ID:{0}] successfully removed from the store.", petId));
	}

}