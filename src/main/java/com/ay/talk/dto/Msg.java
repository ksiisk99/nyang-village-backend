package com.ay.talk.dto;


//stomp에 사용할 채팅 메시지 객체
public class Msg {
	private String roomId;
	private String nickName;
	private String content;
	private String time;
	private int type; //0입장, 1퇴장, 2전송
	private int pmType=0; //pc에서 보낸 것인지 mobile에서 보낸 것인지 pc면 1
	
	public Msg() {}
	
	public Msg(String roomId, String nickName, int type) {
		super();
		this.roomId = roomId;
		this.nickName = nickName;
		this.type = type;
	}
	
	

	public Msg(String roomId, String nickName, String content, String time, int type, int pmType) {
		super();
		this.roomId = roomId;
		this.nickName = nickName;
		this.content = content;
		this.time = time;
		this.type = type;
		this.pmType = pmType;
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
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type=type;
	}
	
	public int getpmType() {
		return pmType;
	}
	
	public void setpmType() {
		pmType=1;
	}
}
