package com.ay.talk.dto.request;

import org.bson.types.ObjectId;

public class ReqSuspend {
	private String id;
	private String studentId;
	private String period;
	private String reportContent;
	private String reportWhy;
	
	public ReqSuspend() {}
	public ReqSuspend(String id, String studentId, String period, String reportContent, String reportWhy) {
		super();
		this.id = id;
		this.studentId = studentId;
		this.period = period;
		this.reportContent = reportContent;
		this.reportWhy = reportWhy;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getStudentId() {
		return studentId;
	}
	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}
	public String getPeriod() {
		return period;
	}
	public void setPeriod(String period) {
		this.period = period;
	}
	public String getReportContent() {
		return reportContent;
	}
	public void setReportContent(String reportContent) {
		this.reportContent = reportContent;
	}
	public String getReportWhy() {
		return reportWhy;
	}
	public void setReportWhy(String reportWhy) {
		this.reportWhy = reportWhy;
	}
	
	
}
