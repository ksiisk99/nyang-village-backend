package com.ay.talk.dto.request;

import io.swagger.annotations.ApiModelProperty;

public class ReqLogin {
	@ApiModelProperty(example = "앱 업데이트 버전")
	private int version; //앱 업데이트 버전
	@ApiModelProperty(example = "파이어베이스 토큰")
    private String fcm; //파이어베이스 토큰
	@ApiModelProperty(example = "학번")
    private String studentId; //학번
	@ApiModelProperty(example = "비밀번호")
    private String password; //비밀번호

    public ReqLogin() {}
    
	public ReqLogin(int version, String fcm, String studentId, String password) {
		super();
		this.version = version;
		this.fcm = fcm;
		this.studentId = studentId;
		this.password = password;
	}

	public int getVersion() {
		return version;
	}

	public String getFcm() {
		return fcm;
	}

	public String getStudentId() {
		return studentId;
	}


	public String getPassword() {
		return password;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public void setFcm(String fcm) {
		this.fcm = fcm;
	}

	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
