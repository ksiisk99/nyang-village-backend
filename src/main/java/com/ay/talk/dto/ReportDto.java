package com.ay.talk.dto;

import io.swagger.annotations.ApiModelProperty;

public class ReportDto {
	@ApiModelProperty(example = "고유 식별 번호(=신고 목록 번호)")
	private String id;
	@ApiModelProperty(example = "채팅방 이름(=과목명)")
	private String roomName; //채팅방 이름
	@ApiModelProperty(example = "신고 대상자(=닉네임)")
	private String reportName; //신고 대상
	@ApiModelProperty(example = "채팅 내용")
	private String reportContent; //채팅 내용
	@ApiModelProperty(example = "신고 사유")
	private String reportWhy; //신고 사유
	@ApiModelProperty(example = "신고자")
	private String reporter; //신고자
	@ApiModelProperty(example = "신고자 학번")
	private String reporterStudentId; //신고자 학번
	@ApiModelProperty(example = "신고 대상자 학번")
	private String targetStudentId; //신고 대상 학번
    
    public ReportDto() {}
	public ReportDto(String id, String roomName, String reportName, String reportContent, String reportWhy,
			String reporter, String reporterStudentId, String targetStudentId) {
		super();
		this.id = id;
		this.roomName = roomName;
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

	public void setRoomName(String roomName) {
		this.roomName = roomName;
	}

	public String getReportName() {
		return reportName;
	}

	public void setReportName(String reportName) {
		this.reportName = reportName;
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

	public String getReporter() {
		return reporter;
	}

	public void setReporter(String reporter) {
		this.reporter = reporter;
	}

	public String getReporterStudentId() {
		return reporterStudentId;
	}

	public void setReporterStudentId(String reporterStudentId) {
		this.reporterStudentId = reporterStudentId;
	}

	public String getTargetStudentId() {
		return targetStudentId;
	}

	public void setTargetStudentId(String targetStudentId) {
		this.targetStudentId = targetStudentId;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	

	
	
}
