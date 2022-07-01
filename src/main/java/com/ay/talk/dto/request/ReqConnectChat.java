package com.ay.talk.dto.request;


//웹소켓 처음 연결시 전달하는 데이터 정지유저나 이중로그인을 판별한다.
public class ReqConnectChat {
	String roomId, studentId, token;
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
