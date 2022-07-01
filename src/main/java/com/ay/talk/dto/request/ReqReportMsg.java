package com.ay.talk.dto.request;

public class ReqReportMsg {
    String reportName;
    String reportContent;
    String reportWhy;
    String reporter;
    String studentId;
    String roomName;
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
