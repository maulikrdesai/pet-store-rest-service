package com.petstore.dao.entities;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.util.StringUtils;

import com.petstore.models.Pet;

/**
 * PetEntity generated by hbm2java
 */
@Entity
@Table(name = "pets")
public class PetEntity extends com.petstore.dao.entities.AbstractPetEntity implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	public PetEntity() {
		super();
	}

	public PetEntity(String petName) {
		super(petName);
	}

	public PetEntity(CategoryEntity categoryEntity, String petName, Set<PetPhotosEntity> petPhotosEntities, Set<TagEntity> tagEntities) {
		super(categoryEntity, petName, petPhotosEntities, tagEntities);
	}

	public PetEntity(Pet newPet) {
	}

	public static int compareByName(PetEntity t1, PetEntity t2) {
		return t1.getPetName().compareTo(t2.getPetName());
	}

	@Transient
	public CategoryEntity getCategory() {
		return super.getCategoryEntity();
	}

	@Transient
	public List<TagEntity> getTags() {
		return super.getTagEntities().parallelStream().sorted(TagEntity::compareByName).collect(Collectors.toList());
	}

	@Transient
	public List<String> getPhotoUrls() {
		return super.getPetPhotosEntities().stream().map(pp -> pp.getPhotoUrl()).filter(s -> StringUtils.isEmpty(s)).sorted()
				.collect(Collectors.toList());
	}
}
