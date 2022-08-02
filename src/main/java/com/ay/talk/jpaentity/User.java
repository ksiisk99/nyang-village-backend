package com.ay.talk.jpaentity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class User {
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="user_id")
	private Long Id;
	private String studentId;
	private String fcm;
	private String date;
	
	@OneToMany(mappedBy = "user")
	private List<UserRoomInfo> userRoomInfos;
	
	@Enumerated(EnumType.STRING)
	private Authority authority;
	
	protected User() {}

	public User(Long id, String studentId, String fcm, String date, List<UserRoomInfo> userRoomInfos,
			Authority authority) {
		super();
		Id = id;
		this.studentId = studentId;
		this.fcm = fcm;
		this.date = date;
		this.userRoomInfos = userRoomInfos;
		this.authority = authority;
	}

	public Long getId() {
		return Id;
	}

	public String getStudentId() {
		return studentId;
	}

	public String getFcm() {
		return fcm;
	}

	public String getDate() {
		return date;
	}

	public List<UserRoomInfo> getUserRoomInfos() {
		return userRoomInfos;
	}

	public Authority getAuthority() {
		return authority;
	}
	
	
}
