package com.ay.talk.dto;

import io.swagger.annotations.ApiModelProperty;

public class ReportDto {
	@ApiModelProperty(example = "���� �ĺ� ��ȣ(=�Ű� ��� ��ȣ)")
	private String id;
	@ApiModelProperty(example = "ä�ù� �̸�(=�����)")
	private String roomName; //ä�ù� �̸�
	@ApiModelProperty(example = "�Ű� �����(=�г���)")
	private String reportName; //�Ű� ���
	@ApiModelProperty(example = "ä�� ����")
	private String reportContent; //ä�� ����
	@ApiModelProperty(example = "�Ű� ����")
	private String reportWhy; //�Ű� ����
	@ApiModelProperty(example = "�Ű���")
	private String reporter; //�Ű���
	@ApiModelProperty(example = "�Ű��� �й�")
	private String reporterStudentId; //�Ű��� �й�
	@ApiModelProperty(example = "�Ű� ����� �й�")
	private String targetStudentId; //�Ű� ��� �й�
    
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
