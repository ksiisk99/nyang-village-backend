package com.ay.talk.entity;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

//신고 클래스
@Document(collection = "Report")
public class Report {
	@Id
	private String id;
	private String roomName; //채팅방 이름
	private String reportName; //신고 대상
	private String reportContent; //채팅 내용
	private String reportWhy; //신고 사유
	private String reporter; //신고자
	private String reporterStudentId; //신고자 학번
	private String targetStudentId; //신고 대상 학번
        
	
	public Report(String roomName,String reportName, String reportContent, String reportWhy, String reporter,
			String reporterStudentId, String targetStudentId) {
		super();
		this.roomName=roomName;
		this.reportName = reportName;
		this.reportContent = reportContent;
		this.reportWhy = reportWhy;
		this.reporter = reporter;
		this.reporterStudentId = reporterStudentId;
		this.targetStudentId = targetStudentId;
	}
	public String getRoomName() {
		return roomName;
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
	public String getReporterStudentId() {
		return reporterStudentId;
	}
	public String getTargetStudentId() {
		return targetStudentId;
	}
	public String getId() {
		return id;
	}
	
	
	
    
    
}
