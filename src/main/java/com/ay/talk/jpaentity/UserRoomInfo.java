package com.ay.talk.jpaentity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class UserRoomInfo {
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_room_info_id")
    private Long Id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;
	
	private String nickName;
	private String roomId;
	private String roomName;
	private String professorName;
	
	protected UserRoomInfo() {}
	
	public UserRoomInfo(Long id, User user, String nickName, String roomId, String roomName, String professorName) {
		super();
		Id = id;
		this.user = user;
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
	public String getRoomId() {
		return roomId;
	}
	public String getRoomName() {
		return roomName;
	}
	public String getProfessorName() {
		return professorName;
	}
	
	
}
