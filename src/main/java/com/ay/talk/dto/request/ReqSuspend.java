package com.ay.talk.dto.request;

import io.swagger.annotations.ApiModelProperty;

public class ReqSuspend {
	@ApiModelProperty(example = "고유 식별 번호(=신고 목록 번호)")
	private String id;
	@ApiModelProperty(example = "정지받을 사용자 아이디(학번)")
	private String studentId;
	@ApiModelProperty(example = "정지기간")
	private String period;
	@ApiModelProperty(example = "채팅내용")
	private String reportContent;
	@ApiModelProperty(example = "정지사유")
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
