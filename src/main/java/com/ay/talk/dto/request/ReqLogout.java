package com.ay.talk.dto.request;

import io.swagger.annotations.ApiModelProperty;

public class ReqLogout {
	@ApiModelProperty(example = "사용자 학번")
	private String studentId;

    public ReqLogout(){}
    public ReqLogout(String studentId) {
        this.studentId = studentId;
    }
	public String getStudentId() {
		return studentId;
	}
	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}
    
    
}
