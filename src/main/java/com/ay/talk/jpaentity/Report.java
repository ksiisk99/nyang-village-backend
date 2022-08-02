package com.ay.talk.jpaentity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Report {
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "report_id")
    private Long Id;
	@Column(name = "room_name",length = 30)
	private String roomName;	//채팅방 이름
	@Column(name = "report_name",length = 20)
	private String reportName;	//신고 대상
	@Column(name = "report_content")
	private String reportContent;	//채팅 내용
	@Column(name = "report_why")
	private String reportWhy;	//신고 사유
	@Column(length = 20)
	private String reporter;	//신고자
	@Column(name = "reporter_student_id",length = 10)
	private String reporterStudentId;	//신고자 학번
	@Column(name = "target_student_id",length = 10)
	private String targetStudentId; 	//신고 대상 학번
	
	protected Report() {}

	public Report(String roomName, String reportName, String reportContent, String reportWhy, String reporter,
			String reporterStudentId, String targetStudentId) {
		super();
		this.roomName = roomName;
		this.reportName = reportName;
		this.reportContent = reportContent;
		this.reportWhy = reportWhy;
		this.reporter = reporter;
		this.reporterStudentId = reporterStudentId;
		this.targetStudentId = targetStudentId;
	}

	public Long getId() {
		return Id;
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
	
	
}
