package com.ay.talk.jpaentity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class RandomName {
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "random_name_id")
	private Long Id;
	@Column(length = 20)
	private String name;
	
	protected RandomName() {}

	public RandomName(String name) {
		this.name = name;
	}
	
	public Long getId() {
		return Id;
	}

	public String getName() {
		return name;
	}
}
