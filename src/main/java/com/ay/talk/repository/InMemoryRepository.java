package com.ay.talk.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Repository;

@Repository
public class InMemoryRepository implements ServerRepository{
	private boolean settingFlag=false; //서버세팅동안에 사용자 접속을 막음
	private Map<String,Integer> subjects=null; //모든 과목(과목명, 인덱스)
	private Map<Integer,ArrayList<String>> roomInTokens=null; //방안에 있는 사용자들 토큰(방번호, 유저토큰들)
	private Map<Integer,ArrayList<String>> roomInNames=null; //방안에 있는 사용자들 랜덤닉네임(방번호, 유저닉네임들)
	private Map<String,String> fbTokens=null; //사용자(학번, 파베토큰)
	private boolean[][] checkRoomNames=null; //모든 방안에 있는 랜덤닉네임 체크 중복제거
	private String[] randomNames=null; //랜덤닉네임 200개. 각 방에 랜덤으로 뿌려준다.
	private Map<String,Integer> idxRandomNames=null; //랜덤닉네임(닉네임,인덱스)
	private Map<String,String> suspendedUsers=null; //정지회원(학번,정지기간)
	private final int VERSION=1; //업데이트 버전
	
	
	//모든 변수 초기화
	@PostConstruct
	public void init2() {
		subjects=new HashMap<String, Integer>();
		roomInTokens=new HashMap<Integer, ArrayList<String>>();
		roomInNames=new HashMap<Integer, ArrayList<String>>();
		fbTokens=new HashMap<String, String>();
		idxRandomNames=new HashMap<String, Integer>();
		randomNames=new String[200];
		suspendedUsers=new HashMap<String, String>();
	}
	
	//checkRoomNames 초기화
	public void initCheckRoomNames(int size) { 
		checkRoomNames=new boolean[size][200];
	}
	
	//subjects, roomInNames, roomInTokens 초기화
	public void initRoomIn(String subjectName, int roomId) { 
		subjects.put(subjectName, roomId);
		roomInNames.put(roomId, new ArrayList<String>());
		ArrayList<String> tokens=new ArrayList<String>();
		tokens.add("tmp");
		roomInTokens.put(roomId, tokens); //초기 값이 없으면 메시지 보낼때 에러 뜬다.
	}
	
	//랜덤 닉네임 초기화
	public void initRandomName(String name,int idx) {
		randomNames[idx]=name;
		idxRandomNames.put(name, idx);
	}
	
	//사용자 정보를 가져와서 fbTokens변수에 학번에 대응하는 fcm토큰으로 초기화하고 방에 해당하는 닉네임 초기화
	public void initUser(String fcm, String studentId, int roomId, String nickName) {
		fbTokens.put(studentId,fcm); //학번에 해당하는 파베토큰
		roomInTokens.get(roomId).add(fcm);
		roomInNames.get(roomId).add(nickName);
	}
	
	//정지 회원 정보를 가져와서 suspendedUsers 초기화
	public void initSuspendedUser(String studentId, String period) {
		suspendedUsers.put(studentId, period);
	}
	
	//버전 비교
	public int getVersion() {
		return VERSION;
	}
	
	//정지 회원 날짜 주기 정지 회원이 아니면 null
	public String getSuspendedUser(String studentId) {
		return suspendedUsers.get(studentId);
	}
	
	//정지 기간 풀린 회원 데이터 삭제
	public void removeSuspendedUser(String studentId) {
		suspendedUsers.remove(studentId);
	}
	
	//회원 fcm토큰
	public String getUserFbToken(String studentId) {
		//System.out.println("getUserFbToken "+fbTokens.get(studentId));
		return fbTokens.get(studentId);
	}
	
	//각 방에 대한 처음 랜덤닉네임 인덱스 방 인원의 사이즈만큼 return하는 이유는 순차적으로 랜덤닉네임을 할당하기 때문
	public int getStartRandomNickNameIdx(String roomName) {
		return subjects.get(roomName)==null?300: roomInNames.get(subjects.get(roomName)).size();
	}
	
	//각 방에 대한 랜덤닉네임을 사용중인지 아닌지 확인
	public boolean isCheckRoomName(String roomName,int idx) {
		return checkRoomNames[subjects.get(roomName)][idx];
	}
	
	//방에 대한 랜덤닉네임 체크
	public void setCheckRoomName(String roomName,int idx, boolean check) {
		checkRoomNames[subjects.get(roomName)][idx]=check;
		//System.out.println("checkRoomName"+checkRoomNames[subjects.get(roomName)]);
	}
	
	//랜덤닉네임
	public String getRandomNickName(int idx){
		return randomNames[idx];
	}
	
	//과목명에 대한 방 아이디값
	public int getRoomId(String roomName) {
		return subjects.get(roomName);
	}
	
	//방 안의 사용자 닉네임들
	public ArrayList<String> getRoomInNames(String roomName){
		//System.out.println(roomInNames.get(subjects.get(roomName)));
		return roomInNames.get(subjects.get(roomName));
	}
	
	//방 안의 사용자 토큰들
	public ArrayList<String> getRoomInTokens(String roomId){
		//System.out.println(roomInTokens.get(Integer.parseInt(roomId)));
		return roomInTokens.get(Integer.parseInt(roomId));
	}
	
	//방 안의 사용자 토큰들 roomId로 조회
	public ArrayList<String> getRoomInTokens2(int roomId){
		return roomInTokens.get(roomId);
	}
	
	//randomNames 인덱스로 방 안에 사용자 랜덤닉네임 추가
	public void addRoomInName(String roomName,int idx) {
		roomInNames.get(subjects.get(roomName)).add(randomNames[idx]);
	}
	
	//랜덤닉네임을 매개변수로 받아 방 안에 사용자 닉네임 추가
	public void addRoomInName2(int roomId,String nickName) {
		roomInNames.get(roomId).add(nickName);
		//System.out.println("addRoomInName2:"+roomInNames.get(roomId));
	}
	
	//방 안에 사용자 토큰 추가
	public void addRoomInToken(String roomName,String fcm) {
		roomInTokens.get(subjects.get(roomName)).add(fcm);
		//System.out.println("addRoomInToken"+roomInTokens.get(subjects.get(roomName)));
	}
	
	
	
	//사용자 토큰 추가
	public void addFbToken(String studentId, String fcm) {
		fbTokens.put(studentId, fcm);
	}
	
	//방 안의 사용자 닉네임 삭제
	public void removeRoomInName(int roomId, String nickName) {
		roomInNames.get(roomId).remove(nickName);
		//System.out.println("removeRoomInName"+roomInNames.get(roomId));
	}
	
	//방 안의 사용자 토큰 삭제
	public void removeRoomInToken(int roomId, String fcm) {
		roomInTokens.get(roomId).remove(fcm);
		//System.out.println("removeRoomToken"+roomInTokens.get(roomId));
	}

	//정지 회원 추가
	@Override
	public void addSuspendedUser(String studentId, String period) {
		// TODO Auto-generated method stub
		suspendedUsers.put(studentId, period);
	}
	
	
	
}
