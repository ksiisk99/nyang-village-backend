package com.ay.talk.dao.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import com.ay.talk.dao.SubjectDao;
import com.ay.talk.entity.Subject;

@Repository
public class SubjectDaoImpl implements SubjectDao{
	
	private final MongoTemplate mongoTemplate;

	@Autowired
	public SubjectDaoImpl(MongoTemplate mongoTemplate) {
		this.mongoTemplate=mongoTemplate; //find ��
	}
	
	//��ü ���� ��������
	public List<Subject> findSubjects(){ 
		return mongoTemplate.find(new Query().with(Sort.by(Sort.Direction.ASC,"_id")), Subject.class);
	}
}
