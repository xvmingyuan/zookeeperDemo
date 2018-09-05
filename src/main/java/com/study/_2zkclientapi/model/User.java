package com.study._2zkclientapi.model;

import java.io.Serializable;

public class User implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer id;
	private String name;

	public Integer getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public User setId(Integer id) {
		this.id = id;
		return this;
	}

	public User setName(String name) {
		this.name = name;
		return this;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", name=" + name + "]";
	}

}
