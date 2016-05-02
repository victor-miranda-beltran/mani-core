package com.victormiranda.mani.core.dto.transaction;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jdk.nashorn.internal.runtime.options.Option;

import java.util.Optional;


public final class Category {

	private final Integer id;
	private final String name;
	private final Optional<Category> parent;

	@JsonCreator
	public Category(@JsonProperty("id") Integer id,
					@JsonProperty("name") String name,
					@JsonProperty("parent") Optional<Category> parent) {
		this.id = id;
		this.name = name;
		this.parent = parent;
	}

	public Integer getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public Optional<Category> getParent() {
		return parent;
	}
}
