package com.ay.talk.dto.request;

public class ReqPcLogin {
	private String studentId;
	private String password;
	
	public ReqPcLogin() {}
	
	public ReqPcLogin(String studentId, String password) {
		super();
		this.studentId = studentId;
		this.password = password;
	}

	public String getStudentId() {
		return studentId;
	}

	public String getPassword() {
		return password;
	}
}
