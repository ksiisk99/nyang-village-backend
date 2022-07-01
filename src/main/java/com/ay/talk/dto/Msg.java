package com.ay.talk.dto;


//stomp�� ����� ä�� �޽��� ��ü
public class Msg {
	private String roomId;
	private String nickName;
	private String content;
	private String time;
	private int type; //0����, 1����, 2����
	private int pmType=0; //pc���� ���� ������ mobile���� ���� ������ pc�� 1
	
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
