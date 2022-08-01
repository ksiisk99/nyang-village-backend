package com.ay.talk.dao;

import java.util.List;

import com.ay.talk.entity.RandomName;

public interface RandomNameDao {
	//랜덤닉네임 조회
	public List<RandomName> findRandomNames();
}
