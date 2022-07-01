package com.ay.talk.dao.impl;

import java.util.List;

import javax.annotation.PostConstruct;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;
import com.ay.talk.dao.ReportDao;
import com.ay.talk.dto.request.ReqReportMsg;
import com.ay.talk.entity.Report;
import com.ay.talk.entity.Suspended;
import com.ay.talk.entity.User;
import com.ay.talk.entity.UserRoomData;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

@Component
public class ReportDaoImpl implements ReportDao{
	private final MongoTemplate mongoTemplate;
	private final MongoDatabase mongoDatabase;
	private MongoCollection<Report> reportCollection;

	@Autowired
	public ReportDaoImpl(MongoTemplate mongoTemplate, MongoDatabase mongoDatabase) {
		this.mongoTemplate=mongoTemplate; //find 용
		this.mongoDatabase=mongoDatabase; //insert update용
	}
	
	@PostConstruct
	public void initMongoCollections() {
		reportCollection=mongoDatabase.getCollection("Report",Report.class); //신고 컬렉션 초기화
	}
	
	//유저 신고
	public void insertReport(UserRoomData userRoomData, ReqReportMsg reportMsg) {
		Query query=new Query();
		query.addCriteria(Criteria.where("roomIds").is(userRoomData));
		query.fields().include("studentId");		
		User reportUser=mongoTemplate.findOne(query, User.class); //신고 대상 학번 찾기

		Report report=new Report(reportMsg.getRoomName(), reportMsg.getReportName(), 
				reportMsg.getReportContent(), reportMsg.getReportWhy(), 
				reportMsg.getReporter(), reportMsg.getStudentId(),reportUser.getStudentId());
		reportCollection.insertOne(report);
	}
	
	//신고 목록
	@Override
	public List<Report> findReports() {
		// TODO Auto-generated method stub
		return mongoTemplate.findAll(Report.class);
	}

	//처리 완료된 신고 항목 삭제
	@Override
	public void removeReports(String id) {
		// TODO Auto-generated method stub
		Query query=new Query();
		query.addCriteria(Criteria.where("id").is(id));
		mongoTemplate.remove(query,Report.class);
	}
	
	
}
