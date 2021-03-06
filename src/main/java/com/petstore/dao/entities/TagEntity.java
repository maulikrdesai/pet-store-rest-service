package com.petstore.dao.entities;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * TagEntity generated by hbm2java
 */
@Entity
@Table(name = "tags")
public class TagEntity extends com.petstore.dao.entities.AbstractTagEntity implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	public TagEntity() {
		super();
	}

	public TagEntity(long tagId, String tagName) {
		super(tagId, tagName);
	}

	public TagEntity(long tagId, String tagName, Set<PetEntity> petEntities) {
		super(tagId, tagName, petEntities);
	}

	public static int compareByName(TagEntity t1, TagEntity t2) {
		return t1.getTagName().compareTo(t2.getTagName());
	}
}
