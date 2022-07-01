package com.ay.talk.dto;

public class SubjectInfo {
	String subjectName;
	String professorName;
	
	
	public SubjectInfo(String subjectName, String professorName) {
		super();
		this.subjectName = subjectName;
		this.professorName = professorName;
	}
	public String getSubjectName() {
		return subjectName;
	}
	public String getProfessorName() {
		return professorName;
	}
	
	
}
