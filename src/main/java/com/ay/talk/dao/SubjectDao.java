package com.ay.talk.dao;

import java.util.List;

import com.ay.talk.entity.Subject;

public interface SubjectDao {
	//전체 과목 가져오기
	public List<Subject> findSubjects();
}
