package com.ay.talk.dto.response;

//ä�� ���� �� �ѹ� ���߷α��ΰ� ������ ���ؼ� üũ�Ͽ� response�Ѵ�.
public class ResConnectChat {
	int start; //1���߷α��� / 2����  / 3���б� ����
	String suspendedDate; //���� �Ⱓ
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
