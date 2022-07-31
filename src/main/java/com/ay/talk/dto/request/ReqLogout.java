package com.ay.talk.dto.request;

import io.swagger.annotations.ApiModelProperty;

public class ReqLogout {
	@ApiModelProperty(example = "����� �й�")
	private String studentId;
	@ApiModelProperty(example = "�ĺ� ��ū")
	private String fcm;

    public ReqLogout(){}
	public ReqLogout(String studentId, String fcm) {
		super();
		this.studentId = studentId;
		this.fcm = fcm;
	}

	public String getStudentId() {
		return studentId;
	}
	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}
	public String getFcm() {
		return fcm;
	}
	public void setFcm(String fcm) {
		this.fcm = fcm;
	}
    
    
}
