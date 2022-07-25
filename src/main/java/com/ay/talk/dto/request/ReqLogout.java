package com.ay.talk.dto.request;

import io.swagger.annotations.ApiModelProperty;

public class ReqLogout {
	@ApiModelProperty(example = "����� �й�")
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
