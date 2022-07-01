package com.ay.talk.dto.response;

//채팅 연결 시 한번 이중로그인과 정지에 대해서 체크하여 response한다.
public class ResConnectChat {
	int start; //1이중로그인 / 2정지  / 3새학기 시작
	String suspendedDate; //정지 기간
	String studentId;
	
	public ResConnectChat(int start, String suspendedDate, String studentId) {
		super();
		this.start = start;
		this.suspendedDate=suspendedDate;
		this.studentId=studentId;
	}
	

	public void setSuspendedDate(String suspendedDate) {
		this.suspendedDate = suspendedDate;
	}

	public void setStart(int start) {
		this.start = start;
	}
	
	public void setStudentId(String studentId) {
		this.studentId=studentId;
	}


	public int getStart() {
		return start;
	}


	public String getSuspendedDate() {
		return suspendedDate;
	}


	public String getStudentId() {
		return studentId;
	}
	
	
}
