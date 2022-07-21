package com.ay.talk.dto.request;

import io.swagger.annotations.ApiModelProperty;

//������ ó�� ����� �����ϴ� ������ ���������� ���߷α����� �Ǻ��Ѵ�.
public class ReqConnectChat {
	@ApiModelProperty(example = "ä�ù�� ��ȣ")
	private String roomId;
	@ApiModelProperty(example = "�й�")
	private String studentId;
	@ApiModelProperty(example = "�ĺ���ū")
	private String token;
	@ApiModelProperty(example = "�� ������Ʈ ����")
	int version;
	
	public ReqConnectChat() {}
	
	public ReqConnectChat(String roomId,String studentId, String token) {
		super();
		this.roomId=roomId;
		this.studentId = studentId;
		this.token = token;
	}
	
	public ReqConnectChat(String roomId, String studentId, String token, int version) {
		super();
		this.roomId = roomId;
		this.studentId = studentId;
		this.token = token;
		this.version = version;
	}

	public String getRoomId() {
		return roomId;
	}

	public void setRoomId(String roomId) {
		this.roomId = roomId;
	}

	public String getStudentId() {
		return studentId;
	}

	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}
	
	
}
