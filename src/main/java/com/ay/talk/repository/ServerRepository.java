package com.ay.talk.repository;

import java.util.ArrayList;
import java.util.HashMap;

public interface ServerRepository {
		
		//checkRoomNames 초기화
		public void initCheckRoomNames(int size);
		
		//subjects, roomInNames, roomInTokens 초기화
		public void initRoomIn(String subjectName, int roomId);
		
		//랜덤 닉네임 초기화
		public void initRandomName(String name,int idx);
		
		//사용자 정보를 가져와서 fbTokens변수에 학번에 대응하는 fcm토큰으로 초기화하고 방에 해당하는 닉네임 초기화
		public void initUser(String fcm, String studentId, int roomId, String nickName);
		
		//정지 회원 정보를 가져와서 suspendedUsers 초기화
		public void initSuspendedUser(String studentId, String period);
		
		//버전 비교
		public int getVersion();
		
		//정지 회원 날짜 주기 정지 회원이 아니면 null
		public String getSuspendedUser(String studentId);
		
		//정지 기간 풀린 회원 데이터 삭제
		public void removeSuspendedUser(String studentId);
		
		//회원 fcm토큰
		public String getUserFbToken(String studentId);
		
		//각 방에 대한 처음 랜덤닉네임 인덱스 방 인원의 사이즈만큼 return하는 이유는 순차적으로 랜덤닉네임을 할당하기 때문
		public int getStartRandomNickNameIdx(String roomName);
		
		//각 방에 대한 랜덤닉네임을 사용중인지 아닌지 확인
		public boolean isCheckRoomName(String roomName,int idx);
		
		//방에 대한 랜덤닉네임 체크
		public void setCheckRoomName(String roomName,int idx, boolean check);
		
		//랜덤닉네임
		public String getRandomNickName(int idx);
		
		//과목명에 대한 방 아이디값
		public int getRoomId(String roomName);
		
		//방 안의 사용자 닉네임들
		public ArrayList<String> getRoomInNames(String roomName);
		
		//방 안의 사용자 토큰들
		public ArrayList<String> getRoomInTokens(String roomId);
		
		//방 안의 사용자 토큰들 roomId로 조회
		public ArrayList<String> getRoomInTokens2(int roomId);
		
		//randomNames 인덱스로 방 안에 사용자 랜덤닉네임 추가
		public void addRoomInName(String roomName,int idx);
		
		//랜덤닉네임을 매개변수로 받아 방 안에 사용자 닉네임 추가
		public void addRoomInName2(int roomId,String nickName);
		
		//방 안에 사용자 토큰 추가
		public void addRoomInToken(String roomName,String fcm);
		
		
		
		//사용자 토큰 추가
		public void addFbToken(String studentId, String fcm);
		
		//방 안의 사용자 닉네임 삭제
		public void removeRoomInName(int roomId, String nickName);
		
		//방 안의 사용자 토큰 삭제
		public void removeRoomInToken(int roomId, String fcm);
		
		//정지 회원 추가
		public void addSuspendedUser(String studentId, String period);
}
