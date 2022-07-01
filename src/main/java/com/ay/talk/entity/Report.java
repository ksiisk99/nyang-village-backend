package com.ay.talk.entity;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

//�Ű� Ŭ����
@Document(collection = "Report")
public class Report {
	@Id
	private String id;
	private String roomName; //ä�ù� �̸�
	private String reportName; //�Ű� ���
	private String reportContent; //ä�� ����
	private String reportWhy; //�Ű� ����
	private String reporter; //�Ű���
	private String reporterStudentId; //�Ű��� �й�
	private String targetStudentId; //�Ű� ��� �й�
        
	
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
