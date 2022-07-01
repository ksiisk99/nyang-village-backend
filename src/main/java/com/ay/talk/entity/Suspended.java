package com.ay.talk.entity;



import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="Suspended")
public class Suspended {
	
	private String studentId;
	private String period;
	private String reportContent;
	private String reportWhy;
	
	
	public Suspended(String studentId, String period, String reportContent, String reportWhy) {
		this.studentId = studentId;
		this.period = period;
		this.reportContent = reportContent;
		this.reportWhy = reportWhy;
	}
//	public Suspended(String studentId, String period) {
//		this.studentId = studentId;
//		this.period = period;
//	}
	public String getStudentId() {
		return studentId;
	}
	public String getPeriod() {
		return period;
	}
	public String getReportContent() {
		return reportContent;
	}
	public String getReportWhy() {
		return reportWhy;
	}
	
	
	
}
