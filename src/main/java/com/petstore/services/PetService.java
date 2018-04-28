package com.petstore.services;

import java.text.MessageFormat;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.petstore.dao.entities.CategoryEntity;
import com.petstore.dao.entities.PetEntity;
import com.petstore.dao.entities.PetEntity.PetStatus;
import com.petstore.dao.entities.PetPhotosEntity;
import com.petstore.dao.entities.TagEntity;
import com.petstore.dao.repository.CategoryRepository;
import com.petstore.dao.repository.PetRepository;
import com.petstore.dao.repository.TagRepository;
import com.petstore.models.Pet;
import com.petstore.models.Tag;

@Service
public class PetService {

	@Autowired
	PetRepository petRepo;
	@Autowired
	CategoryRepository categoryRepo;
	@Autowired
	TagRepository tagRepo;

	public List<PetEntity> findAll() {
		return petRepo.findAll();
	}

	public Optional<PetEntity> findById(long petId) {
		return petRepo.findById(petId);
	}

	public void deleteById(long petId) {
		petRepo.deleteById(petId);
	}

	public PetEntity save(Pet newPet) {
		Optional<PetEntity> optionalPet = petRepo.findById(newPet.getId());
		if (optionalPet.isPresent())
			throw new IllegalArgumentException(
					MessageFormat.format("Pet with id {0} already belong to the store", newPet.getId()));

		PetEntity newPetEntity = new PetEntity(newPet.getName(), PetStatus.resolveStatus(newPet.getStatus()));
		dtoToEntity(newPet, newPetEntity);
		return petRepo.save(newPetEntity);
	}

	public PetEntity update(Long petId, Pet petUpdate) {
		Optional<PetEntity> optionalPet = petRepo.findById(petId);
		if (!optionalPet.isPresent())
			throw new IllegalArgumentException(
					MessageFormat.format("Pet with id {0} does not belong to the store", petUpdate.getId()));

		PetEntity petEntity = optionalPet.get();
		dtoToEntity(petUpdate, petEntity);
		return petRepo.save(petEntity);
	}

	public void dtoToEntity(Pet petUpdate, PetEntity petEntity) {

		petEntity.setPetName(petUpdate.getName());
		petEntity.setStatus(PetStatus.resolveStatus(petUpdate.getStatus()).name());

		// Resolve Category
		CategoryEntity categoryEntity = null;
		if (petUpdate.getCategory() != null && petUpdate.getCategory().getId() > 0) {
			Optional<CategoryEntity> optionalCategory = categoryRepo.findById(petUpdate.getCategory().getId());
			if (!optionalCategory.isPresent())
				categoryEntity = new CategoryEntity(petUpdate.getCategory().getName());
		}
		petEntity.setCategoryEntity(categoryEntity);

		// Resolve Tags
		if (petUpdate.getTags() != null) {
			petEntity.setTagEntities(new HashSet<>());
			for (Tag tag : petUpdate.getTags()) {
				TagEntity tagEntity = null;
				if (petUpdate.getCategory() != null && petUpdate.getCategory().getId() > 0) {
					Optional<TagEntity> optionalTag = tagRepo.findById(tag.getId());
					if (!optionalTag.isPresent())
						tagEntity = new TagEntity(0, tag.getName());
				}
				petEntity.getTagEntities().add(tagEntity);
			}
		}
		// Resolve PhotoUrls
		petEntity.setPetPhotosEntities(new HashSet<>());
		for (String photoUrl : petUpdate.getPhotoUrls())
			petEntity.getPetPhotosEntities().add(new PetPhotosEntity(petEntity, photoUrl));

	}

}
