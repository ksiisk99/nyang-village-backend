package com.ay.talk.dto.request;

import io.swagger.annotations.ApiModelProperty;

public class ReqReportMsg {
	@ApiModelProperty(example = "신고 대상자(=닉네임)")
    String reportName;
	@ApiModelProperty(example = "채팅 내용")
    String reportContent;
	@ApiModelProperty(example = "신고 사유")
    String reportWhy;
	@ApiModelProperty(example = "신고자")
    String reporter;
	@ApiModelProperty(example = "신고자 학번")
    String studentId;
	@ApiModelProperty(example = "채팅방 이름(=과목명)")
    String roomName;
	@ApiModelProperty(example = "교수님 성함")
    String professorName;
    
    public ReqReportMsg() {}
    
    public ReqReportMsg(String reportName, String reportContent, String reportWhy, String reporter, String studentId,String roomName) {
        this.reportName = reportName;
        this.reportContent = reportContent;
        this.reportWhy = reportWhy;
        this.reporter = reporter;
        this.studentId = studentId;
        this.roomName=roomName;
    }
    
    public ReqReportMsg(String reportName, String reportContent, String reportWhy, String reporter, String studentId,String roomName,String professorName) {
        this.reportName = reportName;
        this.reportContent = reportContent;
        this.reportWhy = reportWhy;
        this.reporter = reporter;
        this.studentId = studentId;
        this.roomName=roomName;
        this.professorName=professorName;
    }
    
    

    public String getProfessorName() {
		return professorName;
	}

	

	public String getReportName() {
        return reportName;
    }

    public String getReportContent() {
        return reportContent;
    }

    public String getReportWhy() {
        return reportWhy;
    }

    public String getReporter() {
        return reporter;
    }

    public String getStudentId() {
        return studentId;
    }
    
    public String getRoomName() {
    	return roomName;
    }
}
