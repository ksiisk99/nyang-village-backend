package com.ay.talk.dto.request;

import io.swagger.annotations.ApiModelProperty;

//웹소켓 처음 연결시 전달하는 데이터 정지유저나 이중로그인을 판별한다.
public class ReqConnectChat {
	@ApiModelProperty(example = "채팅방방 번호")
	private String roomId;
	@ApiModelProperty(example = "학번")
	private String studentId;
	@ApiModelProperty(example = "파베토큰")
	private String token;
	@ApiModelProperty(example = "앱 업데이트 버전")
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
