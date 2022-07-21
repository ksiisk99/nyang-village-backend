package com.ay.talk.dto.response;

import io.swagger.annotations.ApiModelProperty;

//채팅 연결 시 한번 이중로그인과 정지에 대해서 체크하여 response한다.
public class ResConnectChat {
	@ApiModelProperty(example = "채팅 연결 시 신호 => 1:이중로그인 / 2:정지 / 3:새학기시작")
	private int start; //1이중로그인 / 2정지  / 3새학기 시작
	@ApiModelProperty(example = "start가 2번일 경우 정지 기간 아니면 NULL")
	private String suspendedDate; //정지 기간
	@ApiModelProperty(example = "학번")
	private String studentId;
	
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
