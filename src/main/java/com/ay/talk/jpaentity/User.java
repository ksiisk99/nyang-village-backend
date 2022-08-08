package com.ay.talk.jpaentity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "user")
public class User {
	@Id
	@Column(name = "student_id", length = 10)
	private String studentId;
	private String fcm;
	@Column(length = 6)
	private String date;
	
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	private List<UserRoomInfo> userRoomInfos=new ArrayList<UserRoomInfo>();
	
	@Enumerated(EnumType.STRING)
	@Column(length = 10)
	private Authority authority;
	
	protected User() {}
	
	public User(String studentId, String fcm, String date, Authority authority) {
		super();
		this.studentId = studentId;
		this.fcm = fcm;
		this.date = date;
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
	
	public void addUserRoomInfo(UserRoomInfo userRoomInfo) {
		userRoomInfos.add(userRoomInfo);
		userRoomInfo.makeFk(this);
	}
	
	public void changeUserRoomInfos(List<UserRoomInfo> userRoomInfos) {
		this.userRoomInfos=userRoomInfos;
	}
	
	public void changeFcm(String fcm) {
		this.fcm=fcm;
	}
}
