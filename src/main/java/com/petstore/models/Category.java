package com.petstore.models;

import java.io.Serializable;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class Category implements Serializable {

	private static final long serialVersionUID = 1L;
	private long id;
	private String name;

	public Category() {
		super();
	}

	public Category(long id, String name) {
		super();
		this.id = id;
		this.name = name;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@NotNull
	@Size(min = 1, max = 150)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
