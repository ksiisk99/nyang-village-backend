package com.ay.talk.jpaentity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "suspended")
public class Suspended {
	@Id
	@Column(name = "student_id", length = 10)
	private String studentId;
	@Column(length = 6)
	private String period;
	@Column(name = "report_content")
	private String reportContent;
	@Column(name = "report_why")
	private String reportWhy;
	
	protected Suspended() {}

	public Suspended(String studentId, String period, String reportContent, String reportWhy) {
		super();
		this.studentId = studentId;
		this.period = period;
		this.reportContent = reportContent;
		this.reportWhy = reportWhy;
	}

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
