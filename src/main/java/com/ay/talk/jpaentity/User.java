package com.ay.talk.jpaentity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class User {
	@Id
	@Column(name = "student_id", length = 10)
	private String studentId;
	private String fcm;
	@Column(length = 6)
	private String date;
	
	@OneToMany(mappedBy = "user")
	private List<UserRoomInfo> userRoomInfos;
	
	@Enumerated(EnumType.STRING)
	@Column(length = 10)
	private Authority authority;
	
	protected User() {}
	
	public User(String studentId, String fcm, String date, List<UserRoomInfo> userRoomInfos, Authority authority) {
		super();
		this.studentId = studentId;
		this.fcm = fcm;
		this.date = date;
		this.userRoomInfos = userRoomInfos;
		this.authority = authority;
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
