package com.petstore.dao.entities;

import static javax.persistence.GenerationType.IDENTITY;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * AbstractPetEntity generated by hbm2java
 */
@MappedSuperclass
public abstract class AbstractPetEntity extends com.petstore.dao.AbstractBaseEntity implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	private long petId;
	private CategoryEntity categoryEntity;
	private String petName;
	private String status;
	private Set<PetPhotosEntity> petPhotosEntities = new HashSet<PetPhotosEntity>(0);
	private Set<TagEntity> tagEntities = new HashSet<TagEntity>(0);

	public AbstractPetEntity() {
	}

	public AbstractPetEntity(String petName, String status) {
		this.petName = petName;
		this.status = status;
	}

	public AbstractPetEntity(CategoryEntity categoryEntity, String petName, String status, Set<PetPhotosEntity> petPhotosEntities,
			Set<TagEntity> tagEntities) {
		this.categoryEntity = categoryEntity;
		this.petName = petName;
		this.status = status;
		this.petPhotosEntities = petPhotosEntities;
		this.tagEntities = tagEntities;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "pet_id", unique = true, nullable = false)
	@JsonProperty("id")
	public long getPetId() {
		return this.petId;
	}

	public void setPetId(long petId) {
		this.petId = petId;
	}

	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "category_id")
	public CategoryEntity getCategoryEntity() {
		return this.categoryEntity;
	}

	public void setCategoryEntity(CategoryEntity categoryEntity) {
		this.categoryEntity = categoryEntity;
	}

	@Column(name = "pet_name", nullable = false, length = 500)
	@JsonProperty("name")
	public String getPetName() {
		return this.petName;
	}

	public void setPetName(String petName) {
		this.petName = petName;
	}

	@Column(name = "status", nullable = false, length = 50)
	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@JsonIgnore
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "petEntity")
	public Set<PetPhotosEntity> getPetPhotosEntities() {
		return this.petPhotosEntities;
	}

	public void setPetPhotosEntities(Set<PetPhotosEntity> petPhotosEntities) {
		this.petPhotosEntities = petPhotosEntities;
	}

	@JsonIgnore
	@ManyToMany(fetch = FetchType.LAZY, mappedBy = "petEntities")
	public Set<TagEntity> getTagEntities() {
		return this.tagEntities;
	}

	public void setTagEntities(Set<TagEntity> tagEntities) {
		this.tagEntities = tagEntities;
	}

}
