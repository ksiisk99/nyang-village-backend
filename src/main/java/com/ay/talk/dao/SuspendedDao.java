package com.ay.talk.dao;

import java.util.List;

import com.ay.talk.dto.request.ReqSuspend;
import com.ay.talk.entity.Suspended;

public interface SuspendedDao {
	//정지 유저 정보들 가져오기
	public List<Suspended> findSuspendedUserList();

	//정지 기간 풀린 유저 정보 삭제
	public void removeSuspendedUser(String studentId);
	
	//정지 회원 추가
	public void insertSuspendedUser(ReqSuspend reqSuspend);
}