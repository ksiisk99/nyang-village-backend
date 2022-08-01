package com.ay.talk.dao;

import java.util.List;

import com.ay.talk.entity.Subject;

public interface SubjectDao {
	//전체 과목 조회
	public List<Subject> findSubjects();
}
