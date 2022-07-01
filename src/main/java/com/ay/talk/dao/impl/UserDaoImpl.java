package com.ay.talk.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import com.ay.talk.dao.UserDao;
import com.ay.talk.entity.User;
import com.ay.talk.entity.UserRoomData;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

@Component
public class UserDaoImpl implements UserDao{
	
	private final MongoTemplate mongoTemplate;
	private final MongoDatabase mongoDatabase;
	private MongoCollection<User> userCollection;

	@Autowired
	public UserDaoImpl(MongoTemplate mongoTemplate, MongoDatabase mongoDatabase) {
		this.mongoTemplate=mongoTemplate; //find 용
		this.mongoDatabase=mongoDatabase; //insert update용
	}
	
	@PostConstruct
	public void initMongoCollection() {
		userCollection=mongoDatabase.getCollection("User",User.class); //유저 컬렉션 초기화
	}

	//유저 정보들 가져오기
	public List<User> findUserList(){ 
		return mongoTemplate.findAll(User.class);
	}

	//새로운 유저 추가
	public void insertUser(User user) {
		//mongoTemplate.insert(user,"User");
		userCollection.insertOne(user);
	}

	//유저 정보 가져오기
	public User findUser(String studentId) {
		return mongoTemplate.findOne(Query.query(Criteria.where("studentId").is(studentId)), User.class);
	}

	//유저 정보 방에 대해서 업데이트
	public void updateUserRoom(String studentId,ArrayList<UserRoomData> roomIds) {
		Update update=new Update();
		update.set("roomIds", roomIds);
		mongoTemplate.updateFirst(Query.query(Criteria.where("studentId").is(studentId)), update, "User");
	}
	
	//유저 정보 방과 토큰에 대해서 업데이트
	public  void updateUserRoomAndToken(String studentId,ArrayList<UserRoomData> roomIds, String fcm) {
		Update update=new Update();
		update.set("roomIds", roomIds);
		update.set("fcm", fcm);
		mongoTemplate.updateFirst(Query.query(Criteria.where("studentId").is(studentId)), update, "User");
	}
	
	
}
