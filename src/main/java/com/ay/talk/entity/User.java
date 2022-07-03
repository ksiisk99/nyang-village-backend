package com.ay.talk.entity;

import java.util.ArrayList;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;


@Document(collection="User")
public class User{
	private String studentId;
	private String fcm;
	private String date;

	@Field
	private ArrayList<UserRoomData> roomIds;
	@Field
	private ArrayList<String> authorities;
//	public User(String studentId, String fcm, String date, String stopdate, ArrayList<UserRoomData> roomIds) {
//		super();
//		this.studentId = studentId;
//		this.fcm = fcm;
//		this.date = date;
//		this.stopdate = stopdate;
//		this.roomIds = roomIds;
//	}
	
	public User(String studentId, String fcm, String date, ArrayList<UserRoomData> roomIds,
			ArrayList<String> authorities) {
		super();
		this.studentId = studentId;
		this.fcm = fcm;
		this.date = date;
		this.roomIds = roomIds;
		this.authorities = authorities;
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
	

	public ArrayList<UserRoomData> getRoomIds() {
		return roomIds;
	}
	public ArrayList<String> getAuthorities(){
		return authorities;
	}
}
