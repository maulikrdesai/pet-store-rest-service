package com.petstore.models;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class Tag {
	private int id;
	private String name;

	public Tag() {
		super();
	}

	public Tag(int id, String name) {
		super();
		this.id = id;
		this.name = name;
	}

	@NotNull
	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Size(min = 1, max = 150)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
