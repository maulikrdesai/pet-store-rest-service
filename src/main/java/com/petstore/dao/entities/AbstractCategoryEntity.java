package com.petstore.dao.entities;

import static javax.persistence.GenerationType.IDENTITY;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * AbstractCategoryEntity generated by hbm2java
 */
@MappedSuperclass
public abstract class AbstractCategoryEntity extends com.petstore.dao.AbstractBaseEntity implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	private long categoryId;
	private String categoryName;
	private Set<PetEntity> petEntities = new HashSet<PetEntity>(0);

	public AbstractCategoryEntity() {
	}

	public AbstractCategoryEntity(String categoryName) {
		this.categoryName = categoryName;
	}

	public AbstractCategoryEntity(String categoryName, Set<PetEntity> petEntities) {
		this.categoryName = categoryName;
		this.petEntities = petEntities;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "category_id", unique = true, nullable = false)
	@JsonProperty("id")
	public long getCategoryId() {
		return this.categoryId;
	}

	public void setCategoryId(long categoryId) {
		this.categoryId = categoryId;
	}

	@Column(name = "category_name", unique = true, nullable = false, length = 500)
	@JsonProperty("name")
	public String getCategoryName() {
		return this.categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	@JsonIgnore
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "categoryEntity")
	public Set<PetEntity> getPetEntities() {
		return this.petEntities;
	}

	public void setPetEntities(Set<PetEntity> petEntities) {
		this.petEntities = petEntities;
	}

}
