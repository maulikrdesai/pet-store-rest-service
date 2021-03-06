package com.petstore.dao.entities;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.MappedSuperclass;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * AbstractTagEntity generated by hbm2java
 */
@MappedSuperclass
public abstract class AbstractTagEntity extends com.petstore.dao.AbstractBaseEntity implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	private long tagId;
	private String tagName;
	private Set<PetEntity> petEntities = new HashSet<PetEntity>(0);

	public AbstractTagEntity() {
	}

	public AbstractTagEntity(long tagId, String tagName) {
		this.tagId = tagId;
		this.tagName = tagName;
	}

	public AbstractTagEntity(long tagId, String tagName, Set<PetEntity> petEntities) {
		this.tagId = tagId;
		this.tagName = tagName;
		this.petEntities = petEntities;
	}

	@Id
	@Column(name = "tag_id", unique = true, nullable = false)
	@JsonProperty("id")
	public long getTagId() {
		return this.tagId;
	}

	public void setTagId(long tagId) {
		this.tagId = tagId;
	}

	@Column(name = "tag_name", nullable = false, length = 500)
	@JsonProperty("name")
	public String getTagName() {
		return this.tagName;
	}

	public void setTagName(String tagName) {
		this.tagName = tagName;
	}

	@JsonIgnore
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "pettags", joinColumns = { @JoinColumn(name = "tag_id", nullable = false, updatable = false) }, inverseJoinColumns = { @JoinColumn(name = "pet_id", nullable = false, updatable = false) })
	public Set<PetEntity> getPetEntities() {
		return this.petEntities;
	}

	public void setPetEntities(Set<PetEntity> petEntities) {
		this.petEntities = petEntities;
	}

}
