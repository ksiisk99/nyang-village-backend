package com.ay.talk.dao.impl;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import com.ay.talk.dao.ChatMsgDao;
import com.ay.talk.entity.ChatMsg;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

@Component
public class ChatMsgDaoImpl implements ChatMsgDao{
	private final MongoTemplate mongoTemplate;
	private final MongoDatabase mongoDatabase;
	private MongoCollection<ChatMsg> chatMsgCollection;

	@Autowired
	public ChatMsgDaoImpl(MongoTemplate mongoTemplate, MongoDatabase mongoDatabase) {
		this.mongoTemplate=mongoTemplate; //find 용
		this.mongoDatabase=mongoDatabase; //insert update용
	}
	
	@PostConstruct
	public void initMongoCollections() {
		chatMsgCollection=mongoDatabase.getCollection("ChatMsg",ChatMsg.class); //채팅 메시지 컬렉션 초기화
	}
	
	//채팅 메시지 추가
	public void insertChatMsg(ChatMsg chatMsg) {
		chatMsgCollection.insertOne(chatMsg);
	}
}
