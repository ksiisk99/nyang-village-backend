package com.ay.talk.jpaentity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "userroominfo")
public class UserRoomInfo {
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_room_info_id")
    private Long Id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "student_id")
	private User user;
	
	@Column(name = "nick_name", length = 30)
	private String nickName;
	@Column(name = "room_id", length=4)
	private int roomId;
	@Column(name = "room_name", length=70)
	private String roomName;
	@Column(name = "professor_name", length=20)
	private String professorName;
	
	protected UserRoomInfo() {}
	
	public UserRoomInfo(String nickName, int roomId, String roomName, String professorName) {
		this.nickName = nickName;
		this.roomId = roomId;
		this.roomName = roomName;
		this.professorName = professorName;
	}

	public Long getId() {
		return Id;
	}
	public User getUser() {
		return user;
	}
	public String getNickName() {
		return nickName;
	}
	public int getRoomId() {
		return roomId;
	}
	public String getRoomName() {
		return roomName;
	}
	public String getProfessorName() {
		return professorName;
	}

	public void makeFk(User user) {
		this.user=user;
	}
	
	
}
