package com.petstore.services;

import java.text.MessageFormat;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.petstore.dao.entities.CategoryEntity;
import com.petstore.dao.entities.PetEntity;
import com.petstore.dao.entities.PetEntity.PetStatus;
import com.petstore.dao.entities.PetPhotosEntity;
import com.petstore.dao.entities.TagEntity;
import com.petstore.dao.repository.PetRepository;
import com.petstore.models.Pet;
import com.petstore.models.Tag;

@Service
@Transactional
public class PetService {

	@PersistenceContext
	EntityManager em;
	@Autowired
	PetRepository petRepo;

	public List<PetEntity> findAll() {
		return petRepo.findAll();
	}

	public PetEntity findById(long petId) {
		return em.find(PetEntity.class, petId);
	}

	public void deleteById(long petId) {
		PetEntity petEntity = em.find(PetEntity.class, petId);
		if (petEntity == null)
			throw new IllegalArgumentException(
					MessageFormat.format("Pet with id {0} does not belong to the store", petId));
		em.remove(petEntity);
	}

	public PetEntity save(Pet newPet) {
		PetEntity newPetEntity = em.find(PetEntity.class, newPet.getId());
		if (newPetEntity != null)
			throw new IllegalArgumentException(
					MessageFormat.format("Pet with id {0} already belong to the store", newPet.getId()));

		newPetEntity = em.merge(new PetEntity(newPet.getName(), PetStatus.resolveStatus(newPet.getStatus())));
		dtoToEntity(newPet, newPetEntity);
		return newPetEntity;
	}

	public PetEntity update(Long petId, Pet petUpdate) {
		PetEntity petEntity = em.find(PetEntity.class, petId);
		if (petEntity == null)
			throw new IllegalArgumentException(
					MessageFormat.format("Pet with id {0} does not belong to the store", petUpdate.getId()));

		dtoToEntity(petUpdate, petEntity);
		return petEntity;
	}

	public void dtoToEntity(Pet petUpdate, PetEntity petEntity) {

		petEntity.setPetName(petUpdate.getName());
		petEntity.setStatus(PetStatus.resolveStatus(petUpdate.getStatus()).name());

		// Resolve Category
		CategoryEntity categoryEntity = null;
		if (petUpdate.getCategory() != null && petUpdate.getCategory().getId() > 0) {
			categoryEntity = em.find(CategoryEntity.class, petUpdate.getCategory().getId());
			if (categoryEntity == null)
				categoryEntity = em.merge(new CategoryEntity(petUpdate.getCategory().getName()));
		}
		petEntity.setCategoryEntity(categoryEntity);
		petEntity.getTagEntities().removeIf((p) -> true);
		petEntity.getPetPhotosEntities().removeIf((p) -> true);

		if (petUpdate.getTags() != null) {
			for (Tag tag : petUpdate.getTags()) {
				TagEntity tagEntity = em.find(TagEntity.class, tag.getId());
				if (tagEntity == null)
					tagEntity = em.merge(new TagEntity(0, tag.getName()));
				petEntity.getTagEntities().add(tagEntity);
			}
		}
		for (String photoUrl : petUpdate.getPhotoUrls())
			petEntity.getPetPhotosEntities().add(em.merge(new PetPhotosEntity(petEntity, photoUrl)));

	}

}
