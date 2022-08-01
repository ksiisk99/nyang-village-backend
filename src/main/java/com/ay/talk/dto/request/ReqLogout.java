package com.ay.talk.dto.request;

import io.swagger.annotations.ApiModelProperty;

public class ReqLogout {
	@ApiModelProperty(example = "학번")
	private String studentId;
	@ApiModelProperty(example = "파이어베이스 토큰")
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
