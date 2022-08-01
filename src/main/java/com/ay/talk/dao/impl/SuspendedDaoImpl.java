package com.ay.talk.dao.impl;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;
import com.ay.talk.dao.SuspendedDao;
import com.ay.talk.dto.request.ReqSuspend;
import com.ay.talk.entity.Suspended;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

@Component
public class SuspendedDaoImpl implements SuspendedDao{
	private final MongoTemplate mongoTemplate;
	private final MongoDatabase mongoDatabase;
	private MongoCollection<Suspended> suspendedCollection;
	@Autowired
	public SuspendedDaoImpl(MongoTemplate mongoTemplate, MongoDatabase mongoDatabase) {
		this.mongoTemplate=mongoTemplate; //find 용
		this.mongoDatabase=mongoDatabase; //insert update용
	}
	
	@PostConstruct
	public void initCollection() {
		suspendedCollection=mongoDatabase.getCollection("Suspended",Suspended.class);
	}

	//정지 유저 정보들 가져오기
	public List<Suspended> findSuspendedUserList(){
		return mongoTemplate.findAll(Suspended.class);
	}

	//정지 기간 풀린 유저 정보 삭제
	public void removeSuspendedUser(String studentId) {
		mongoTemplate.remove(new Query().addCriteria(Criteria.where("studentId").is(studentId)),Suspended.class);
	}

	//정지 회원 추가
	@Override
	public void insertSuspendedUser(ReqSuspend reqSuspend) {
		// TODO Auto-generated method stub
		suspendedCollection.insertOne(new Suspended(reqSuspend.getStudentId(), reqSuspend.getPeriod(), reqSuspend.getReportContent(), reqSuspend.getReportWhy()));		
	}
	
}
