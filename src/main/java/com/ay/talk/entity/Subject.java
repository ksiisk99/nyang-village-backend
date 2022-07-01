package com.ay.talk.entity;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Subject")
public class Subject {
	
	private String name;

	public Subject(String name) {
		super();
		this.name = name;
	}

	public String getName() {
		return name;
	}

}
