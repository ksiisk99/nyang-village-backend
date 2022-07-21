package com.ay.talk.dto.request;

import io.swagger.annotations.ApiModelProperty;

public class ReqPcLogin {
	@ApiModelProperty(example = "�й�")
	private String studentId;
	@ApiModelProperty(example = "��й�ȣ")
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
