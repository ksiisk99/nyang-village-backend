package com.ay.talk.jpaentity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "chatmsg")
public class ChatMsg {
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "chat_msg_id")
    private Long Id;
	@Column(name = "room_id", length = 4)
	private String roomId;
	@Column(name = "nick_name", length = 20)
	private String nickName;
	private String content;
	@Column(length = 6)
	private String time;
	
	protected ChatMsg() {}

	public ChatMsg(String roomId, String nickName, String content, String time) {
		super();
		this.roomId = roomId;
		this.nickName = nickName;
		this.content = content;
		this.time = time;
	}

	public Long getId() {
		return Id;
	}

	public String getRoomId() {
		return roomId;
	}

	public String getNickName() {
		return nickName;
	}

	public String getContent() {
		return content;
	}

	public String getTime() {
		return time;
	}
	
	
}
