package com.ay.talk.entity;


//들어간 방의 사용자 정보
public class UserRoomData {
	private String nickName;
	private int roomId;
	private String roomName;
	private String professorName;
	
	public UserRoomData() {}
	public UserRoomData(String nickName, int roomId, String roomName) {
		super();
		this.nickName = nickName;
		this.roomId = roomId;
		this.roomName = roomName;
	}
	
	public UserRoomData(String nickName, int roomId, String roomName, String professorName) {
		super();
		this.nickName = nickName;
		this.roomId = roomId;
		this.roomName = roomName;
		this.professorName=professorName;
	}
	
	
	
	public String getProfessorName() {
		return professorName;
	}
	public String getRoomName() {
		return roomName;
	}
	public int getRoomId() {
		return roomId;
	}
	public String getNickName() {
		return nickName;
	}
	public void setRoomName(String roomName) {
		this.roomName = roomName;
	}
	public void setRoomId(int roomId) {
		this.roomId = roomId;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	
	
	
}
