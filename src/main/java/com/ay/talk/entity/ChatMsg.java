package com.ay.talk.entity;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "ChatMsg")
public class ChatMsg {
	private String roomId;
	private String nickName;
	private String content;
	private String time;
	
	public ChatMsg(String roomId, String nickName, String content, String time) {
		super();
		this.roomId = roomId;
		this.nickName = nickName;
		this.content = content;
		this.time = time;
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
