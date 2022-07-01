package com.ay.talk.entity;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "RandomName")
public class RandomName {
	@Field
	private String name;
	
	
	public RandomName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	
}
