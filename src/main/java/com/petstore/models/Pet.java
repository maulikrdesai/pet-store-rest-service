package com.petstore.models;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class Pet {
	private static final AtomicLong ID_GENERATOR = new AtomicLong();

	private long id;
	private String name;
	private String description;
	private Category category;
	private List<String> photoUrls;
	private List<Tag> tags;
	private String status;

	public Pet() {
		super();
	}

	public Pet(String name, String description, Category category, List<String> photoUrls, List<Tag> tags,
			String status) {
		super();

		this.id = ID_GENERATOR.incrementAndGet();
		this.name = name;
		this.description = description;
		this.category = category;
		this.photoUrls = photoUrls;
		this.tags = tags;
		this.status = status;
	}

	@NotNull
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@NotNull
	@Size(min = 2, max = 150)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Size(min = 2, max = 4000)
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public List<String> getPhotoUrls() {
		return photoUrls;
	}

	public void setPhotoUrls(List<String> photoUrls) {
		this.photoUrls = photoUrls;
	}

	public List<Tag> getTags() {
		return tags;
	}

	public void setTags(List<Tag> tags) {
		this.tags = tags;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
