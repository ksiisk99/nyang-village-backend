package com.ay.talk.repository;

import java.util.List;

public interface ServerRepository {
		
		//checkRoomNames 초기화
		public void initCheckRoomNames(int size);
		
		//subjects, roomInNames, roomInTokens 초기화
		public void initRoomIn(String subjectName, int roomId);
		
		//랜덤 닉네임 초기화
		public void initRandomName(String name,int idx);
		
		//사용자 정보를 가져와서 학번에 대응하는 fcm토큰과 정지기간 초기화하고 방정보 초기화
		public void initUser(String fcm, String studentId, String roomId, String nickName, String suspendedPriod,String roomName);
		
		//버전 비교
		public int getVersion();
		
		//정지 기간 풀린 회원 데이터 삭제(=교체)
		public void removeSuspendedUser(String studentId, String fcm);
		

		
		//각 방에 대한 처음 랜덤닉네임 인덱스 방 인원의 사이즈만큼 return하는 이유는 순차적으로 랜덤닉네임을 할당하기 때문
		public int getStartRandomNickNameIdx(String roomName);
		
		//방에 대한 랜덤닉네임 체크
		public boolean setCheckRoomName(String roomName,int idx);
		
		//랜덤닉네임
		public String getRandomNickName(int idx);
		
		//과목명에 대한 방 아이디값
		public int getRoomId(String roomName);
		
		//과목명에 대한 교수님 성함
		public String getProfessorName(String subject);
		
		//방 안의 사용자 닉네임들
		public List<String> getRoomInNames(String roomName);
		
		//방 안의 사용자 토큰들
		public List<String> getRoomInTokens(String roomId);
		
		//방 안의 사용자 토큰들 roomId로 조회
		public List<String> getRoomInTokens2(int roomId);
		
		//randomNames 인덱스로 방 안에 사용자 랜덤닉네임 추가
		public void addRoomInName(String roomName,int idx);
		
		//랜덤닉네임을 매개변수로 받아 방 안에 사용자 닉네임 추가
		public void addRoomInName(String roomName,String nickName);
		
		//방 안에 사용자 토큰 추가
		public void addRoomInToken(String roomName,String fcm);
		
		
		
		//방 안의 사용자 닉네임 삭제
		public void removeRoomInName(String roomName, String nickName);
		
		//방 안의 사용자 토큰 삭제
		public void removeRoomInToken(String roomName, String fcm);
		
		//정지 회원 추가
		public void addSuspendedUser(String studentId, String userInfo);
		
		//회원 정보 조회(fcm,suspendedPriod)
		public String getUserInfo(String studentId);
		
		//회원 정보 추가
		public void addUserInfo(String studentId, String userInfo);
}