package com.ay.talk.jpaentity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "subject")
public class Subject {
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "subject_id")
	private Long Id;
	@Column(length = 70)
	private String name;
	
	protected Subject() {}

	public Subject(String name) {
		this.name = name;
	}
	
	public Long getId() {
		return Id;
	}

	public String getName() {
		return name;
	}
	
}
