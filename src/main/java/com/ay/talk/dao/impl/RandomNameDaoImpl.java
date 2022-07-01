package com.ay.talk.dao.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import com.ay.talk.dao.RandomNameDao;
import com.ay.talk.entity.RandomName;

@Component
public class RandomNameDaoImpl implements RandomNameDao{
	private final MongoTemplate mongoTemplate;

	@Autowired
	public RandomNameDaoImpl(MongoTemplate mongoTemplate) {
		this.mongoTemplate=mongoTemplate; //find ¿ë
	}
	
	@Override
	public List<RandomName> findRandomNames() {
		// TODO Auto-generated method stub
		return mongoTemplate.findAll(RandomName.class);
	}
	
}
