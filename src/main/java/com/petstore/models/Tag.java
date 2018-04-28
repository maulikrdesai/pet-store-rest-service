package com.petstore.models;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class Tag {
	private long id;
	private String name;

	public Tag() {
		super();
	}

	public Tag(long id, String name) {
		super();
		this.id = id;
		this.name = name;
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@NotNull
	@Size(min = 1, max = 150)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
