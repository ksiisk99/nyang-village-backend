package com.ay.talk.repository;

import java.util.ArrayList;
import java.util.List;

import com.ay.talk.dto.request.ReqReportMsg;
import com.ay.talk.dto.request.ReqSuspend;
import com.ay.talk.entity.ChatMsg;
import com.ay.talk.entity.RandomName;
import com.ay.talk.entity.Report;
import com.ay.talk.entity.Subject;
import com.ay.talk.entity.Suspended;
import com.ay.talk.entity.User;
import com.ay.talk.entity.UserRoomData;

public interface DbRepository {
	//전체 과목 가져오기
		public List<Subject> findSubjects();
		
		//랜덤 닉네임들 가져오기
		public List<RandomName> findRandomNames();
		
		//유저 정보들 가져오기
		public List<User> findUserList();
		
		//방 정보들 가져오기
		//public List<Room> findRoomList();
		
		//정지 유저 정보들 가져오기
		public List<Suspended> findSuspendedUserList();
		
		//정지 기간 풀린 유저 정보 삭제
		public void removeSuspendedUser(String studentId);
		
		//새로운 유저 추가
		public void insertUser(User user);
		
		//채팅 메시지 추가
		public void insertChatMsg(ChatMsg chatMsg);
		
		//유저 정보 가져오기
		public User findUser(String studentId);
		
		//유저 정보 방에 대해서 업데이트
		public void updateUserRoom(String studentId,ArrayList<UserRoomData> roomIds);
		
		//유저 정보 방과 토큰에 대해서 업데이트
		public  void updateUserRoomAndToken(String studentId,ArrayList<UserRoomData> roomIds, String fcm);
		
		//유저 신고
		public void insertReport(UserRoomData userRoomData, ReqReportMsg reportMsg);
		
		public List<Report> findReports();
		
		//정지 회원 추가
		public void insertSuspendedUser(ReqSuspend reqSuspend);

		//처리 완료된 신고 항목 삭제
		public void removeReports(String id);
}